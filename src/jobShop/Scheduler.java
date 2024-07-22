package jobShop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    private ArrayList<Job> jobs;
    private ArrayList<Machine> availableMachines;
    private ArrayList<Machine> usedMachines;
    private Queue<Operation> operationQueue;
    private int scheduledOperatingTime = 0; // Time when machines are used
    private int wastedTime = 0; // Time when available machines are not used


    public Scheduler(ArrayList<Job> jobs, ArrayList<Machine> machines) {
        this.jobs = jobs;
        this.availableMachines = new ArrayList<>(machines);
        this.usedMachines = new ArrayList<>();
        this.operationQueue = new LinkedList<>();
        initializeOperationQueue();
    }

    private void initializeOperationQueue() {
        for (Job job : jobs) {
            for (Operation operation : job.getOperations()) {
                operationQueue.add(operation);
            }
        }
    }

    // Methode zum Planen und Ausführen der Operationen
    public void schedule() {
        // Solange es Operationen in der Queue gibt oder Maschinen verwendet werden, fahren wir fort
        while (!operationQueue.isEmpty() || !usedMachines.isEmpty()) {
            boolean operationScheduled = false;
            // Solange es Operationen in der Queue gibt, versuchen wir, sie zu planen
            while (!operationQueue.isEmpty()) {
                Operation nextOperation = operationQueue.poll();
                Machine availableMachine = findAvailableMachine(nextOperation);

                // Wenn eine Maschine verfügbar ist, planen wir die JobShop.Operation
                if (availableMachine != null) {
                    executeOperation(nextOperation, availableMachine);
                    nextOperation.markAsCompleted();
                    scheduledOperatingTime += nextOperation.getProcessingTime();
                    availableMachines.remove(availableMachine);
                    usedMachines.add(availableMachine);
                    operationScheduled = true;
                    break;
                } else {
                    // Falls keine Maschine verfügbar ist, wird die JobShop.Operation zurück in die Queue gestellt
                    operationQueue.add(nextOperation);
                    break;
                }
            }

            // Wenn keine JobShop.Operation geplant werden konnte, aber Maschinen verfügbar sind, erhöhen wir die verschwendete Zeit
            if (!operationScheduled && !availableMachines.isEmpty()) {
                wastedTime++;
            }

            // Überprüfe die Maschinen, die gerade genutzt werden
            ArrayList<Machine> completedMachines = new ArrayList<>();
            for (Machine machine : usedMachines) {
                machine.decrementRemainingTime();
                if (machine.isAvailable()) {
                    completedMachines.add(machine);
                }
            }
            usedMachines.removeAll(completedMachines);
            availableMachines.addAll(completedMachines);
        }
    }

    private Machine findAvailableMachine(Operation operation) {
        for (Machine machine : availableMachines) {
            if (operation.getPossibleMachines().contains(machine)) {
                return machine;
            }
        }
        return null;
    }

    private void executeOperation(Operation operation, Machine machine) {
        System.out.println("Executing operation " + operation.getId() + " on machine " + machine.getId());
        machine.setAvailable(false);
        machine.setRemainingTime(operation.getProcessingTime());
        System.out.println("Scheduled operation " + operation.getId() + " on machine " + machine.getName() + " for " + operation.getProcessingTime() + " units.");
    }

    public boolean isJobCompleted(Job job) {
        for (Operation operation : job.getOperations()) {
            if (!operation.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllJobsCompleted() {
        for (Job job : jobs) {
            if (!isJobCompleted(job)) {
                return false;
            }
        }
        return true;
    }

    public int getScheduledOperatingTime() {
        return scheduledOperatingTime;
    }

    public int getWastedTime() {
        return wastedTime;
    }
}
