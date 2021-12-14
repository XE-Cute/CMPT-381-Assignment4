package com.a4basics;

import java.util.ArrayList;

public class ShipGroup implements Groupable{
    public ArrayList<Groupable> group;  // This will contain combination of ships and groups
    public ArrayList<Groupable> ships;  // This will only contain ships

    public ShipGroup(){
        group = new ArrayList<>();
        ships = new ArrayList<>();
    }

    public ShipGroup(ArrayList<Groupable> g){
        group = g;
        ships = g;
    }

    public void addToGroup(ShipGroup g){
        group.add(g);
        this.ships.addAll(g.ships);

    }

    @Override
    public void moveShip(double dx, double dy) {

    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public boolean isContained(SelectionRectangle rect) {
        return false;
    }
}
