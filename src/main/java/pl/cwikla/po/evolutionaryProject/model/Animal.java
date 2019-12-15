package pl.cwikla.po.evolutionaryProject.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Animal {
    private static final AtomicInteger ID_GEN = new AtomicInteger();
    private final int id;
    private AnimalGenotype genotype;
    private MapDirection orientation;
    private Position position;
    private int energy;
    private final List<AnimalObserver> observers;

    public Animal(AnimalGenotype genotype, MapDirection orientation, Position position, int energy) {
        this.id = ID_GEN.incrementAndGet();
        this.genotype = genotype;
        this.orientation = orientation;
        this.position = position;
        this.energy = energy;
        this.observers = new LinkedList<>();
    }

    //region Getters and Setters
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
    //endregion
    //region Observers handling
    public void register(AnimalObserver observer) {
        observers.add(observer);
    }

    public void unregister(AnimalObserver observer){
        observers.remove(observer);
    }

    private void notifyObservers(Position oldPosition) {
        for (AnimalObserver observer : observers) {
            observer.onPositionChanged(this, oldPosition);
        }
    }
    //endregion
    //region Movement and life
    public void moveTo(Position position, MapDirection orientation) {
        Position oldPosition = this.position;
        this.position = position;
        this.orientation = orientation;
        notifyObservers(oldPosition);
    }

    public boolean isDead(){
        return energy <0;
    }
    //endregion
}
