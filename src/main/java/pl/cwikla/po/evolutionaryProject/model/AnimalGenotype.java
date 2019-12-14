package pl.cwikla.po.evolutionaryProject.model;

import java.util.*;

public class AnimalGenotype {
    private static final int GENOTYPE_LENGTH = 32;
    private static final int NUMBER_OF_GENES = 8;
    private static final Random RANDOM = new Random();
    private final List<Integer> genes;

    private AnimalGenotype(List<Integer> genes) {
        this.genes = genes;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public static AnimalGenotype random() {
        List<Integer> genes = new LinkedList<>();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genes.add(RANDOM.nextInt(NUMBER_OF_GENES));
        }

        return new AnimalGenotype(normalize(genes));
    }

    public static AnimalGenotype mix(AnimalGenotype first, AnimalGenotype second) {
        int firstCut = RANDOM.nextInt(GENOTYPE_LENGTH);
        int secondCut = RANDOM.nextInt(GENOTYPE_LENGTH);
        List<Integer> childGenes = new LinkedList<>();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            if (i < firstCut || i >= secondCut) childGenes.add(first.genes.get(i));
            else childGenes.add(second.genes.get(i));
        }
        return new AnimalGenotype(normalize(childGenes));
    }

    private static List<Integer> normalize(List<Integer> genes) {
        HashMap<Integer, Integer> sumGenes = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_GENES; i++) {
            sumGenes.put(i, 0);
        }
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            sumGenes.replace(i, sumGenes.get(i) + 1);
        }
        sumGenes.entrySet().stream()
                .filter(a -> a.getValue() == 0)
                .forEach(a -> {
                    Map.Entry<Integer, Integer> maximum = sumGenes.entrySet().stream().max(Map.Entry.comparingByValue()).get();
                    genes.remove(maximum.getKey());
                    genes.add(a.getKey());
                    sumGenes.replace(maximum.getKey(), maximum.getValue() - 1);
                });

        genes.sort(Integer::compareTo);
        return Collections.unmodifiableList(new ArrayList<>(genes));
    }

}
