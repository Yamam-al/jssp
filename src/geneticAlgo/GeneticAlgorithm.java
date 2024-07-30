package geneticAlgo;

import jobShop.Operation;
import jobShop.Scheduler;

import java.util.*;

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
        ArrayList<Operation> offspringGenes = new ArrayList<>(Collections.nCopies(parent1.getGenes().size(), null));

        int start = random.nextInt(parent1.getGenes().size());
        int end = random.nextInt(parent1.getGenes().size() - start) + start;

        for (int i = start; i <= end; i++) {
            offspringGenes.set(i, parent1.getGenes().get(i));
        }

        int currentIndex = (end + 1) % parent2.getGenes().size();
        HashSet<Operation> subSegment = new HashSet<>(offspringGenes.subList(start, end + 1));
        for (int i = 0; i < parent2.getGenes().size(); i++) {
            int parent2Index = (end + 1 + i) % parent2.getGenes().size();
            Operation gene = parent2.getGenes().get(parent2Index);
            if (!subSegment.contains(gene)) {
                offspringGenes.set(currentIndex, gene);
                currentIndex = (currentIndex + 1) % parent2.getGenes().size();
            }
        }

        Chromosome offspring = new Chromosome(offspringGenes);
        System.out.println("Crossover result: " + offspring.getGenes());
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
            System.out.println("Mutation result: " + genes);
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
