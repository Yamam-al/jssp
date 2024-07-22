import geneticAlgo.Chromosome;
import geneticAlgo.GeneticAlgorithm;
import helpers.TestDataGenerator;
import jobShop.Job;
import jobShop.Machine;
import jobShop.Operation;
import jobShop.Scheduler;

import java.util.ArrayList;
import java.util.Random;

public class TestGA {
    private static final Random random = new Random(3434);
    public static void main(String[] args) {
        // Generiere Testdaten
        TestDataGenerator generator = new TestDataGenerator();
        generator.generateTestData(5, 10, 5, random); // 5 Maschinen, 10 Jobs, 5 Operationen pro Job

        ArrayList<Job> jobs = generator.getJobs();
        ArrayList<Machine> machines = generator.getMachines();
        ArrayList<Operation> operations = new ArrayList<>();
        for (Job job : jobs) {
            operations.addAll(job.getOperations());
        }

        // Initialisiere den Scheduler
        Scheduler scheduler = new Scheduler(jobs, machines);

        // Initialisiere den genetischen Algorithmus
        double weightScheduledTime = 0.7;
        double weightWastedTime = 0.3;
        GeneticAlgorithm ga = new GeneticAlgorithm(50, 0.95, 0.01, 5, operations, weightScheduledTime, weightWastedTime, random);

        // Evolviere die Population über mehrere Generationen
        int generationCount = 100;
        for (int i = 0; i < generationCount; i++) {
            System.out.println("Generation: " + i);
            scheduler = new Scheduler(jobs, machines); // Reset Scheduler
            ga.evolvePopulation(scheduler);
        }

        // Beste Lösung finden
        Chromosome bestChromosome = ga.getBestChromosome();
        scheduler.initializeOperationQueue(bestChromosome.getGenes());
        scheduler.schedule();

        // Überprüfe, ob alle Jobs abgeschlossen sind
        if (scheduler.isAllJobsCompleted()) {
            System.out.println("Alle Jobs wurden abgeschlossen in " + scheduler.getScheduledOperatingTime() + " Zeiteinheiten.");
            System.out.println("Verschwendete Zeit: " + scheduler.getWastedTime() + " Zeiteinheiten.");
        } else {
            System.out.println("Es sind noch nicht alle Jobs abgeschlossen.");
        }
    }
}
