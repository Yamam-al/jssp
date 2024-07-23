package geneticAlgo;

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

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void calculateFitness(Scheduler scheduler, double weightScheduledTime, double weightWastedTime) {
        Scheduler tempScheduler = new Scheduler(new ArrayList<>(scheduler.getJobs()), scheduler.getMachines());
        tempScheduler.schedule(this.genes);
        tempScheduler.schedule();
        int scheduledOperatingTime = tempScheduler.getScheduledOperatingTime();
        int wastedTime = tempScheduler.getWastedTime();
        this.fitness = weightScheduledTime * scheduledOperatingTime + weightWastedTime * wastedTime;
    }
}

