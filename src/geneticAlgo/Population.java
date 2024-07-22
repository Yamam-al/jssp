package geneticAlgo;

import geneticAlgo.Chromosome;
import jobShop.Operation;
import jobShop.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {
    private ArrayList<Chromosome> chromosomes;
    private int populationSize;

    public Population(int populationSize, ArrayList<Operation> operations, Random random) {
        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>(populationSize);
        // Initialize the list with null values to avoid IndexOutOfBoundsException
        for (int i = 0; i < populationSize; i++) {
            chromosomes.add(null);
        }
        initializePopulation(operations, random);
    }

    private void initializePopulation(ArrayList<Operation> operations, Random random) {
        for (int i = 0; i < populationSize; i++) {
            Collections.shuffle(operations, random);
            Chromosome newChromosome = new Chromosome(new ArrayList<>(operations));
            chromosomes.set(i, newChromosome);
        }
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void calculateFitness(Scheduler scheduler, double weightScheduledTime, double weightWastedTime) {
        for (Chromosome chromosome : chromosomes) {
            if (chromosome != null) {
                chromosome.calculateFitness(scheduler, weightScheduledTime, weightWastedTime);
            }
        }
    }
}
