package com.a4basics;

import java.util.ArrayList;

public class ShipGroup implements Groupable{
    public ArrayList<Groupable> groups;

    public ShipGroup(){
        groups = new ArrayList<>();
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
