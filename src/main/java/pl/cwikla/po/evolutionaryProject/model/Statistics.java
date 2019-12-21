package pl.cwikla.po.evolutionaryProject.model;

import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private int day;
    private int numberOfAnimals;
    private int avgNumberOfAnimals;
    private double avgLifetime;
    private double avgEnergy;
    private double avgNumberOfChildren;
    private Map<AnimalGenotype, Integer> genotypes;

    public Statistics() {
        day = 0;
        avgNumberOfAnimals = 0;
        avgLifetime = 0;
        avgEnergy = 0;
        avgNumberOfChildren = 0;
        genotypes = new HashMap<>();
    }

    public void updatePrimitives(int day, int avgNumberOfAnimals, double avgLifetime, double avgEnergy, double avgNumberOfChildren) {
        this.day = day;
        this.avgNumberOfAnimals += avgNumberOfAnimals;
        this.avgLifetime = avgLifetime;
        this.avgEnergy += avgEnergy;
        this.avgNumberOfChildren += avgNumberOfChildren;
    }

    public void updateGenotypes(AnimalGenotype animalGenotype) {
        if (genotypes.containsKey(animalGenotype)) {
            genotypes.replace(animalGenotype, genotypes.get(animalGenotype) + 1);
        } else {
            genotypes.put(animalGenotype, 1);
        }
    }

    public void updateNumberOfAnimals() {
        numberOfAnimals++;
    }

    @Override
    public String toString() {
        return String.format("-----Statistics of evolutionary simulation----- \n" +
                        "Days passed: %d \n" +
                        "Total number of born animals: %d \n" +
                        "Average number of alive animals per day: %.2f \n" +
                        "Average lifetime : %.2f \n" +
                        "Average energy per animal per day: %.2f \n" +
                        "Average number of children per animal per day: %.2f\n" +
                        "Dominant genotype in all days passed: %s \n",
                day,
                numberOfAnimals,
                1.0 * avgNumberOfAnimals / day,
                avgLifetime,
                avgEnergy / day,
                avgNumberOfChildren / day,
                genotypes.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey().toString()
                );
    }

    public void saveToFile() {
        File file = new File("statistics.txt");
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(this.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("File not created");
        }
    }
}
