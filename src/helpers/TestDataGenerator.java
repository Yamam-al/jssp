package helpers;

import jobShop.Job;
import jobShop.Machine;
import jobShop.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TestDataGenerator {
    private ArrayList<Job> jobs;
    private ArrayList<Machine> machines;
    private Random rand = new Random();

    public void generateTestData(int machineCount, int jobCount, int operationsPerJob) {
        machines = generateMachines(machineCount);
        jobs = generateJobs(jobCount, operationsPerJob, machines);
    }
    public void generateTestData(int machineCount, int jobCount, int operationsPerJob, int seed) {
        rand = new Random(seed);
        generateTestData(machineCount, jobCount, operationsPerJob);
    }

    private ArrayList<Machine> generateMachines(int machineCount) {
        ArrayList<Machine> machines = new ArrayList<>();
        for (int i = 0; i < machineCount; i++) {
            machines.add(new Machine(i, "JobShop.Machine " + i));
        }
        return machines;
    }

    private ArrayList<Operation> generateOperations(int operationCount, ArrayList<Machine> machines) {
        ArrayList<Operation> operations = new ArrayList<>();
        for (int i = 0; i < operationCount; i++) {
            int workTime = rand.nextInt(10) + 1; // Random work time between 1 and 10
            int priority = rand.nextInt(5) + 1; // Random priority between 1 and 5

            // Wähle eine zufällige Teilmenge von Maschinen
            ArrayList<Machine> possibleMachines = new ArrayList<>(machines);
            Collections.shuffle(possibleMachines, rand);
            int subsetSize = rand.nextInt(3) + 1; // Zufällige Anzahl von möglichen Maschinen (1 bis 3)
            ArrayList<Machine> selectedMachines = new ArrayList<>(possibleMachines.subList(0, subsetSize));

            operations.add(new Operation(i, priority, selectedMachines, workTime));
        }
        return operations;
    }

    private ArrayList<Job> generateJobs(int jobCount, int operationPerJob, ArrayList<Machine> machines) {
        ArrayList<Job> jobs = new ArrayList<>();
        for (int i = 0; i < jobCount; i++) {
            ArrayList<Operation> operations = generateOperations(operationPerJob, machines);
            jobs.add(new Job(i, operations));
        }
        return jobs;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }
}

