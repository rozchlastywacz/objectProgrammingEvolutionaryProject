public class AnimalGenotype {
    private int[] genes;

    public AnimalGenotype(){
        genes = createRandomGenotype();
    }

    public AnimalGenotype(Animal firstParent, Animal secondParent){
        genes = mixParentsGenotypes(firstParent.getGenotype(), secondParent.getGenotype());
    }

    private int[] createRandomGenotype() {
        return null;
    }

    private int[] mixParentsGenotypes(AnimalGenotype firstParentGenotype, AnimalGenotype secondParentGenotype) {
        return null;
    }

}
