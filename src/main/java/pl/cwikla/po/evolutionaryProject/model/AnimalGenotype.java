package pl.cwikla.po.evolutionaryProject.model;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class AnimalGenotype {
    private static final int GENOTYPE_LENGTH = 32;
    private static final int NUMBER_OF_GENES = 8;
    private static final Random RANDOM = new Random();
    private final List<Gene> genes;

    private AnimalGenotype(List<Gene> genes) {
        this.genes = genes;
    }

    //region creating random i mixed genotype with normalization
    public static AnimalGenotype random() {
        List<Gene> genes = new LinkedList<>();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genes.add(Gene.getByCode(RANDOM.nextInt(NUMBER_OF_GENES)));
        }

        return new AnimalGenotype(normalize(genes));
    }

    public static AnimalGenotype mix(AnimalGenotype first, AnimalGenotype second) {
        int firstCut = RANDOM.nextInt(GENOTYPE_LENGTH);
        int secondCut = RANDOM.nextInt(GENOTYPE_LENGTH);
        List<Gene> childGenes = new LinkedList<>();
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            if (i < firstCut || i >= secondCut) childGenes.add(first.genes.get(i));
            else childGenes.add(second.genes.get(i));
        }
        return new AnimalGenotype(normalize(childGenes));
    }

    private static List<Gene> normalize(List<Gene> genes) {
        Map<Gene, Integer> sumGenes = new EnumMap<>(Gene.class);
        for (Gene gene : Gene.values()) {
            sumGenes.put(gene, 0);
        }
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            Gene gene = genes.get(i);
            sumGenes.replace(gene, sumGenes.get(gene) + 1);
        }
        sumGenes.entrySet().stream()
                .filter(a -> a.getValue() == 0)
                .forEach(a -> {
                    Map.Entry<Gene, Integer> maximum = sumGenes.entrySet().stream().max(Map.Entry.comparingByValue()).get();
                    genes.remove(maximum.getKey());
                    genes.add(a.getKey());
                    sumGenes.replace(maximum.getKey(), maximum.getValue() - 1);
                });

        genes.sort(Enum::compareTo);
        return Collections.unmodifiableList(new ArrayList<>(genes));
    }
    //endregion

    public int angle(){
        return genes.get(RANDOM.nextInt(GENOTYPE_LENGTH)).getCode();
    }

    @Override
    public String toString() {
        return  genes.stream()
                .collect(groupingBy(Function.identity(), TreeMap::new, counting()))
                .entrySet().stream()
                .map(e -> e.getKey().toString() + e.getValue().toString())
                .collect(joining());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimalGenotype)) return false;
        AnimalGenotype that = (AnimalGenotype) o;
        return Objects.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genes);
    }
}
