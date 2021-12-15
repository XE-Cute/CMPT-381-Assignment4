package com.a4basics;

import java.util.ArrayList;

public class ShipGroup implements Groupable{
    public ArrayList<Groupable> group;  // This will contain combination of ships and groups
    public ArrayList<Groupable> ships;  // This will only contain ships
    public SelectionRectangle outline;
    ShipGroup parentGroup;
    boolean isSelected;

    public ShipGroup(){
        group = new ArrayList<>();
        ships = new ArrayList<>();
        outline = new SelectionRectangle(0, 0, 0, 0);
        isSelected = false;
    }

    public ShipGroup(ArrayList<Groupable> g){
        group = new ArrayList<>(g);
        ships = new ArrayList<>(g);
        setBoxSize();
        isSelected = true;
    }

    public void setBoxSize(){
        final double[] tempX = new double[1];
        final double[] tempY = new double[1];
        final double[] tempWidth = new double[1];
        final double[] tempHeight = new double[1];

        tempX[0] = tempY[0] = Double.POSITIVE_INFINITY;
        tempWidth[0] = tempHeight[0] = Double.NEGATIVE_INFINITY;
        ships.forEach(ship -> {
            if (ship instanceof Ship s) {
                for (int i = 0; i < s.displayXs.length; i++) {
                    if (s.displayXs[i] < tempX[0])
                        tempX[0] = s.displayXs[i];
                    if (s.displayXs[i] > tempWidth[0])
                        tempWidth[0] = s.displayXs[i];
                }
                for (int i = 0; i < s.displayYs.length; i++) {
                    if (s.displayYs[i] < tempY[0])
                        tempY[0] = s.displayYs[i];
                    if (s.displayYs[i] > tempHeight[0])
                        tempHeight[0] = s.displayYs[i];
                }
            }
        });
        tempWidth[0] = tempWidth[0] - tempX[0];
        tempHeight[0] = tempHeight[0] - tempY[0];
        outline = new SelectionRectangle(tempX[0], tempY[0], tempWidth[0], tempHeight[0]);
    }

    @Override
    public void moveShip(double dx, double dy) {
        group.forEach(group -> {
            if (group instanceof Ship s) {
                for (int i = 0; i < s.displayXs.length; i++) {
                    s.displayXs[i] += dx;
                    s.displayYs[i] += dy;
                }
                s.translateX += dx;
                s.translateY += dy;
            }
            else
                group.moveShip(dx, dy);
        });
        setBoxSize();
    }

    @Override
    public boolean contains(double x, double y) {
        final boolean[] findTrue= {false};
        group.forEach(group -> {
           if (group instanceof Ship)
               if (((Ship)group).contains(x, y))
                    findTrue[0] = true;
           else
               group.contains(x, y);
        });
        return findTrue[0];
    }

    @Override
    public boolean isContained(SelectionRectangle rect) {
        final boolean[] findTrue= {false};
        group.forEach(group -> {
            if (group.isContained(rect))
                findTrue[0] = true;
        });
        return findTrue[0];
    }
    public void updateParentGroup(ShipGroup g){
        parentGroup = g;
    }
    public ShipGroup getParentGroup(){
        return parentGroup;
    }
}
