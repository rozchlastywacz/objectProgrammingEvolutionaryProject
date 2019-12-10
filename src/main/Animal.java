public class Animal implements IWorldMapElement {
    private TorusWorldMap worldMap;
    private MapDirection direction;
    private Position position;
    private AnimalGenotype genotype;
    private int energy;

    @Override
    public Position getPosition() {
        return position;
    }

    public AnimalGenotype getGenotype(){return genotype;}

}
