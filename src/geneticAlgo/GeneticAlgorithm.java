package geneticAlgo;

import jobShop.Operation;
import jobShop.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {
    private Population population;
    private double mutationRate;
    private int elitismCount;
    private Random random;
    private int populationSize;
    private double weightScheduledTime;
    private double weightWastedTime;

    public GeneticAlgorithm(int populationSize, double mutationRate, int elitismCount, ArrayList<Operation> operations, double weightScheduledTime, double weightWastedTime, Random random) {
        this.mutationRate = mutationRate;
        this.elitismCount = elitismCount;
        this.populationSize = populationSize;
        this.weightScheduledTime = weightScheduledTime;
        this.weightWastedTime = weightWastedTime;
        this.population = new Population(populationSize, new ArrayList<>(operations), random);
        this.random = random;
    }

    public void evolvePopulation(Scheduler scheduler) {
        Population newPopulation = new Population(populationSize, new ArrayList<>(scheduler.getOperations()), random);

        // Berechne Fitness
        population.calculateFitness(scheduler, weightScheduledTime, weightWastedTime);

        // Elitismus
        for (int i = 0; i < elitismCount; i++) {
            newPopulation.getChromosomes().set(i, population.getChromosomes().get(i));
        }

        // Crossover
        for (int i = elitismCount; i < population.getChromosomes().size(); i++) {
            Chromosome parent1 = selectParent();
            Chromosome parent2 = selectParent();
            Chromosome offspring = orderCrossover(parent1, parent2);
            newPopulation.getChromosomes().set(i, offspring);
        }

        // Mutation
        for (int i = elitismCount; i < newPopulation.getChromosomes().size(); i++) {
            mutate(newPopulation.getChromosomes().get(i));
        }

        population = newPopulation;
    }

    private Chromosome selectParent() {
        // Turnierauswahl
        int tournamentSize = 5; // Beispielgröße für das Turnier
        ArrayList<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize; i++) {
            Chromosome randomChromosome = population.getChromosomes().get(random.nextInt(population.getChromosomes().size()));
            tournament.add(randomChromosome);
        }
        return tournament.stream().min(Comparator.comparingDouble(Chromosome::getFitness)).orElse(null);
    }

    private Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        // Einfache Einpunkt-Crossover-Implementierung
        ArrayList<Operation> offspringGenes = new ArrayList<>();
        int crossoverPoint = random.nextInt(parent1.getGenes().size());
        for (int i = 0; i < parent1.getGenes().size(); i++) {
            if (i < crossoverPoint) {
                offspringGenes.add(parent1.getGenes().get(i));
            } else {
                offspringGenes.add(parent2.getGenes().get(i));
            }
        }
        return new Chromosome(offspringGenes);
    }

    public Chromosome orderCrossover(Chromosome parent1, Chromosome parent2) {
        ArrayList<Operation> genes = parent1.getGenes();
        int chromosomeLength = genes.size();
        Chromosome offspring = new Chromosome(new ArrayList<>(genes));


        // Select two random crossover points
        int startPos = random.nextInt(chromosomeLength);
        int endPos = random.nextInt(chromosomeLength);

        // Ensure startPos is less than endPos
        if (startPos > endPos) {
            int temp = startPos;
            startPos = endPos;
            endPos = temp;
        }

        // Copy the segment from parent1 to offspring
        for (int i = startPos; i <= endPos; i++) {
            offspring.getGenes().set(i, parent1.getGenes().get(i));
        }

        // Fill the remaining genes from parent2 while maintaining job order
        int currentPos = (endPos + 1) % chromosomeLength;
        for (int i = 0; i < chromosomeLength; i++) {
            Operation parent2Gene = parent2.getGenes().get((endPos + 1 + i) % chromosomeLength);
            if (!offspring.getGenes().contains(parent2Gene)) {
                offspring.getGenes().set(currentPos, parent2Gene);
                currentPos = (currentPos + 1) % chromosomeLength;
            }
        }

        return offspring;
    }


    private void mutate(Chromosome chromosome) {
        // Inversion mutation
        if (random.nextDouble() < mutationRate) {
            int length = chromosome.getGenes().size();
            int start = random.nextInt(length);
            int end = random.nextInt(length - start) + start;

            // Reverse the order of the genes in the selected segment
            ArrayList<Operation> genes = chromosome.getGenes();
            while (start < end) {
                Collections.swap(genes, start, end);
                start++;
                end--;
            }
        }
    }


    public Chromosome getBestChromosome() {
        double bestFitness = 0;
        Chromosome bestChromosome = null;
        for (Chromosome chromosome : population.getChromosomes()) {
            if (chromosome.getFitness() > bestFitness) {
                bestFitness = chromosome.getFitness();
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
    }
}
