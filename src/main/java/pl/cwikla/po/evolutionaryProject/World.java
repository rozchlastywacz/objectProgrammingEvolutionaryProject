package pl.cwikla.po.evolutionaryProject;

import pl.cwikla.po.evolutionaryProject.model.Position;

import java.util.*;

public class World {
    public static void main(String[] args) {
        HashMap<Integer, TreeSet<String>> mapa = new HashMap<>();
        mapa.put(1, new TreeSet<>());
        mapa.get(1).add("dupa");
        mapa.get(1).add("chuj");
        System.out.println(mapa.get(1).first());
        System.out.println(mapa.get(1).last());
        System.out.println(mapa.get(1));

        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        lista.add(1);
        System.out.println(lista);
        lista.remove(Integer.valueOf(1));
        System.out.println(lista);

        HashMap<Integer, Integer> mapahasz = new HashMap<>();
        mapahasz.put(1,2);
        System.out.println(mapahasz);
        mapahasz.put(1, mapahasz.remove(1) +1);
        System.out.println(mapahasz);

        Set<Position> jungleEmptyPositions = new TreeSet<>(Comparator.comparing(Position::getX).thenComparing(Position::getY));
        jungleEmptyPositions.add(new Position(1,2));
        jungleEmptyPositions.add(new Position(1,3));
        jungleEmptyPositions.add(new Position(2,2));
        jungleEmptyPositions.add(new Position(2,3));
        jungleEmptyPositions.add(new Position(2,1));
        System.out.println(jungleEmptyPositions);
        System.out.println(((TreeSet<Position>) jungleEmptyPositions).first());
        System.out.println(((TreeSet<Position>) jungleEmptyPositions).last());
        System.out.println(((TreeSet<Position>) jungleEmptyPositions).higher(((TreeSet<Position>) jungleEmptyPositions).first()));
        System.out.println(((TreeSet<Position>) jungleEmptyPositions).higher(((TreeSet<Position>) jungleEmptyPositions).last()));

    }
}
