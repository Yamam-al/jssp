package helpers;

import jobShop.Job;
import jobShop.Machine;
import jobShop.Operation;

import java.util.ArrayList;
import java.util.Random;

public class TestDataGenerator {
    private ArrayList<Job> jobs;
    private ArrayList<Machine> machines;
    private static int operationIdCounter = 0; // Globaler Zähler für eindeutige IDs

    public TestDataGenerator() {
        this.jobs = new ArrayList<>();
        this.machines = new ArrayList<>();
    }

    public void generateTestData(int machineCount, int jobCount, int operationsPerJob, Random random) {
        // Maschinen generieren
        for (int i = 0; i < machineCount; i++) {
            machines.add(new Machine(i, "Maschine " + (i)));
        }

        // Jobs und Operationen generieren
        for (int i = 0; i < jobCount; i++) {
            ArrayList<Operation> operations = generateOperations(operationsPerJob, random);
            jobs.add(new Job(i, operations));
        }
    }

    private ArrayList<Operation> generateOperations(int operationCount, Random random) {
        ArrayList<Operation> operations = new ArrayList<>();
        for (int i = 0; i < operationCount; i++) {
            int workTime = random.nextInt(10) + 1; // Zufällige Arbeitszeit zwischen 1 und 10
            int priority = random.nextInt(5) + 1; // Zufällige Priorität zwischen 1 und 5
            ArrayList<Machine> possibleMachines = new ArrayList<>(machines);
            operations.add(new Operation(operationIdCounter++, priority, possibleMachines, workTime)); // Eindeutige ID zuweisen
        }
        return operations;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }
}
