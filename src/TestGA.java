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
    private static final Random random = new Random(875);
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

        // Initialisiere den Scheduler
        Scheduler scheduler = new Scheduler(jobs, machines);

        // Initialisiere den genetischen Algorithmus
        double weightScheduledTime = 0.7;
        double weightWastedTime = 0.3;
        GeneticAlgorithm ga = new GeneticAlgorithm(50, 0.7, 10, operations, weightScheduledTime, weightWastedTime, random);

        // Evolviere die Population über mehrere Generationen
        int generationCount = 100;
        for (int i = 0; i < generationCount; i++) {
            System.out.println("Generation: " + i);
            scheduler = new Scheduler(jobs, machines); // Reset Scheduler
            ga.evolvePopulation(scheduler);
        }

        // Beste Lösung finden
        Chromosome bestChromosome = ga.getBestChromosome();
        scheduler.schedule(bestChromosome.getGenes());

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
