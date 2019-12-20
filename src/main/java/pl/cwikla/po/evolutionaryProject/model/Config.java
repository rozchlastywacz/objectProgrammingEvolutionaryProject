package pl.cwikla.po.evolutionaryProject.model;

import java.util.List;

public class Config {
    private MapConfig map;
    private AnimalsConfig animals;

    //region Getters And Setters

    public MapConfig getMap() {
        return map;
    }

    public void setMap(MapConfig map) {
        this.map = map;
    }

    public AnimalsConfig getAnimals() {
        return animals;
    }

    public void setAnimals(AnimalsConfig animals) {
        this.animals = animals;
    }

    //endregion

    //region Subclasses

    public static class MapConfig{
        private int width;
        private int height;
        private double jungleRatio;

        //region Getters And Setters
        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public double getJungleRatio() {
            return jungleRatio;
        }

        public void setJungleRatio(double jungleRatio) {
            this.jungleRatio = jungleRatio;
        }

        //endregion
    }

    public static class AnimalsConfig{
        private int startEnergy;
        private int moveEnergy;
        private int plantEnergy;
        private int numberOfAnimalsAtStart;
        private List<Animal> animals;

        //region Getters And Setters

        public int getStartEnergy() {
            return startEnergy;
        }

        public void setStartEnergy(int startEnergy) {
            this.startEnergy = startEnergy;
        }

        public int getMoveEnergy() {
            return moveEnergy;
        }

        public void setMoveEnergy(int moveEnergy) {
            this.moveEnergy = moveEnergy;
        }

        public int getPlantEnergy() {
            return plantEnergy;
        }

        public void setPlantEnergy(int plantEnergy) {
            this.plantEnergy = plantEnergy;
        }

        public int getNumberOfAnimalsAtStart() {
            return numberOfAnimalsAtStart;
        }

        public void setNumberOfAnimalsAtStart(int numberOfAnimalsAtStart) {
            this.numberOfAnimalsAtStart = numberOfAnimalsAtStart;
        }

        public List<Animal> getAnimals() {
            return animals;
        }

        public void setAnimals(List<Animal> animals) {
            this.animals = animals;
        }

        //endregion
    }

    //endregion

}


