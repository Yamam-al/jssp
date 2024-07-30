package geneticAlgo;

import jobShop.Job;
import jobShop.Operation;
import jobShop.Scheduler;

import java.util.ArrayList;

public class Chromosome {
    private ArrayList<Operation> genes;
    private double fitness;

    public Chromosome(ArrayList<Operation> genes) {
        this.genes = new ArrayList<>(genes);
        this.fitness = 0;
    }

    public ArrayList<Operation> getGenes() {
        return genes;
    }

    public double getFitness() {
        return fitness;
    }

    public void calculateFitness(Scheduler scheduler, double weightScheduledTime, double weightWastedTime) {
        Scheduler tempScheduler = new Scheduler(new ArrayList<>(scheduler.getJobs()), scheduler.getMachines());
        tempScheduler.schedule(this.genes);

        int scheduledOperatingTime = tempScheduler.getScheduledOperatingTime();
        int wastedTime = tempScheduler.getWastedTime();
        int totalJobs = scheduler.getJobs().size();
        int completedJobs = (int) scheduler.getJobs().stream().filter(Job::isCompleted).count();

        // Normiere die Werte
        double normalizedScheduledTime = scheduledOperatingTime / (double) (scheduledOperatingTime + wastedTime);
        double normalizedWastedTime = wastedTime / (double) (scheduledOperatingTime + wastedTime);

        // Berechne die Fitness
        // Hier sorgen wir dafür, dass die Fitness positiv ist und höhere Werte besser sind
        this.fitness = (weightScheduledTime * (1 - normalizedScheduledTime)) + (weightWastedTime * (1 - normalizedWastedTime));

        // Belohnen abgeschlossene Jobs
        this.fitness += completedJobs / (double) totalJobs;

        // Um sicherzustellen, dass die Fitness nicht negativ ist
        if (this.fitness < 0) {
            this.fitness = 0;
        }
        System.out.println("Fitness: " + this.fitness);
    }


}

