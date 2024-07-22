import jobShop.Job;
import jobShop.Machine;
import jobShop.Operation;
import jobShop.Scheduler;
import helpers.TestDataGenerator;

import java.util.ArrayList;
import java.util.Random;

public class TestFIFO {

    private static final Random random = new Random(1234);

    public static void main(String[] args) {
        // Generiere Testdaten
        TestDataGenerator generator = new TestDataGenerator();
        generator.generateTestData(10, 25, 10, random);

        ArrayList<Job> jobs = generator.getJobs();
        ArrayList<Machine> machines = generator.getMachines();
        ArrayList<Operation> operations = new ArrayList<>();
        for (Job job : jobs) {
            operations.addAll(job.getOperations());
        }


        // Initialisiere den JobShop.Scheduler
        Scheduler scheduler = new Scheduler(jobs, machines);

        // Plane die Operationen
        scheduler.schedule();

        // Überprüfe, ob alle Jobs abgeschlossen sind
        if (scheduler.isAllJobsCompleted()) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Alle Jobs wurden abgeschlossen in " + scheduler.getScheduledOperatingTime() + " Zeiteinheiten.");
            System.out.println("Verschwendete Zeit: " + scheduler.getWastedTime() + " Zeiteinheiten.");
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Anzahl der Maschinen: " + machines.size());
            System.out.println("Anzahl der Jobs: " + jobs.size());
            System.out.println("Anzahl Aller Operationen: " + operations.size());
            System.out.println("---------------------------------------------------------------------");
        } else {
            System.out.println("Es sind noch nicht alle Jobs abgeschlossen.");
        }
    }

}