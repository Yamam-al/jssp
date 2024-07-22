package geneticAlgo;

import jobShop.Operation;
import jobShop.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {
    private Population population;
    private double crossoverRate;
    private double mutationRate;
    private int elitismCount;
    private Random random;
    private int populationSize;
    private double weightScheduledTime;
    private double weightWastedTime;

    public GeneticAlgorithm(int populationSize, double crossoverRate, double mutationRate, int elitismCount, ArrayList<Operation> operations, double weightScheduledTime, double weightWastedTime, Random random) {
        this.crossoverRate = crossoverRate;
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
            Chromosome offspring = crossover(parent1, parent2);
            newPopulation.getChromosomes().set(i, offspring);
        }

        // Mutation
        for (int i = elitismCount; i < newPopulation.getChromosomes().size(); i++) {
            mutate(newPopulation.getChromosomes().get(i));
        }

        population = newPopulation;
    }

    private Chromosome selectParent() {
        // Implementiere ein Auswahlverfahren wie Turnierauswahl
        // Einfachheitshalber wählen wir zufällig
        return population.getChromosomes().get(random.nextInt(population.getChromosomes().size()));
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

    private void mutate(Chromosome chromosome) {
        // Einfache Mutationsimplementierung: Zwei Gene vertauschen
        for (int i = 0; i < chromosome.getGenes().size(); i++) {
            if (random.nextDouble() < mutationRate) {
                int j = random.nextInt(chromosome.getGenes().size());
                Collections.swap(chromosome.getGenes(), i, j);
            }
        }
    }

    public Chromosome getBestChromosome() {
        double bestFitness = Double.MAX_VALUE;
        Chromosome bestChromosome = null;
        for (Chromosome chromosome : population.getChromosomes()) {
            if (chromosome.getFitness() < bestFitness) {
                bestFitness = chromosome.getFitness();
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
    }
}
