package pl.cwikla.po.evolutionaryProject.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Animal {
    private static final AtomicInteger ID_GEN = new AtomicInteger();
    private final int id;
    private AnimalGenotype genotype;
    private MapDirection orientation;
    private Position position;
    private int energy;
    private int age;
    private int numberOfChildren;
    private boolean isPreFather;
    private int numberOfDescendants;
    private Animal preFather;
    private int dayOfDeath;
    private final List<AnimalObserver> observers;

    public Animal(AnimalGenotype genotype, MapDirection orientation, Position position, int energy, Animal preFather) {
        this.id = ID_GEN.incrementAndGet();
        this.genotype = genotype;
        this.orientation = orientation;
        this.position = position;
        this.energy = energy;
        this.age = 0;
        this.numberOfChildren = 0;
        this.isPreFather = false;
        this.preFather = preFather;
        this.numberOfDescendants = 0;
        this.dayOfDeath = 0;
        this.observers = new LinkedList<>();
    }

    //region Getters and Setters

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public int getEnergy() {
        return energy;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public AnimalGenotype getGenotype() {
        return genotype;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public int getAge() {
        return age;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public int getNumberOfDescendants(){
        return numberOfDescendants;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public void preFatherIt(){
        isPreFather = true;
    }

    //endregion
    //region Observers handling
    public void register(AnimalObserver observer) {
        observers.add(observer);
    }

    public void unregister(AnimalObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Position oldPosition) {
        for (AnimalObserver observer : observers) {
            observer.onPositionChanged(this, oldPosition);
        }
    }

    //endregion
    //region Movement and life
    public void moveTo(Position position, MapDirection orientation, int energyDrained) {
        Position oldPosition = this.position;
        this.position = position;
        this.orientation = orientation;
        this.energy -= energyDrained;
        this.age++;
        notifyObservers(oldPosition);
    }

    public boolean isDead() {
        return energy < 0;
    }

    public void eatGrass(int energy) {
        this.energy += energy;
    }

    public Animal makeAChild(Animal secondParent, Position position) {
        this.numberOfChildren++;
        secondParent.numberOfChildren++;
        this.energy /= 2;
        secondParent.energy /= 2;
        Animal preFather = getPreFather(this, secondParent);
        if(preFather != null){
            preFather.numberOfDescendants++;
        }
        return new Animal(
                AnimalGenotype.mix(this.getGenotype(), secondParent.getGenotype()),
                MapDirection.random(),
                position,
                this.getEnergy() / 2 + secondParent.getEnergy() / 2,
                preFather
        );
    }

    private Animal getPreFather(Animal firstParent, Animal secondParent) {
        if(firstParent.isPreFather){
            return firstParent;
        }
        if(secondParent.isPreFather){
            return secondParent;
        }
        if(secondParent.preFather != null){
            return secondParent.preFather;
        }
        if(firstParent.preFather != null){
            return firstParent.preFather;
        }
        return null;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
