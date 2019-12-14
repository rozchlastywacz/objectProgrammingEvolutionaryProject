package pl.cwikla.po.evolutionaryProject.model;

public interface AnimalObserver {
    void onPositionChanged(Animal animal, Position oldPosition);
}
