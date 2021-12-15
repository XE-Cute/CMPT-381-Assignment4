package com.a4basics;

import java.util.ArrayList;

import static java.lang.Double.POSITIVE_INFINITY;

public class ShipClipboard{
    Groupable element;

    public ShipClipboard(){
        this.element = null;
    }
    public ShipClipboard(Groupable element){
        this.element = element;
    }
    public boolean isClipboardEmpty(){
        return element == null;
    }
    public void addToClipboard(Groupable element){
        this.element = element;
    }
    public Groupable getClipboard(){
        return this.element;
    }
    public Groupable copy(){
        return copy(this.element);
    }
    public Groupable copy(Groupable element){
        this.element = element;
        double randX = (Math.random()+0.1)*800,
                randY = (Math.random()+0.1)*600;
        return copyHelper(element, randX, randY, POSITIVE_INFINITY, POSITIVE_INFINITY);
    }

    private Groupable copyHelper(Groupable element, double randX, double randY, double diffX, double diffY) {
        if (element instanceof Ship)
            return new Ship(randX, randY);

        ShipGroup temp = new ShipGroup();
        temp.group = new ArrayList<>(((ShipGroup) element).group);
        Groupable iterator = temp.group.get(0);
        while (iterator instanceof ShipGroup)
            iterator = ((ShipGroup) iterator).group.get(0);
        if (diffX == POSITIVE_INFINITY)
            diffX = randX - ((Ship) iterator).translateX;
        if (diffY == POSITIVE_INFINITY)
            diffY = randY - (((Ship) iterator).translateY);

        for (int x = 0; x < temp.group.size(); x++) {
            if (temp.group.get(x) instanceof ShipGroup) {
                ShipGroup tempShipGroup = (ShipGroup) this.copyHelper(temp.group.get(x), randX, randY, diffX, diffY);
                temp.group.set(x, tempShipGroup);
                tempShipGroup.ships.forEach(ship -> {
                    if (!temp.ships.contains(ship))
                        temp.ships.add(ship);
                });
                tempShipGroup.updateParentGroup(temp);
            } else {
                Ship tempShip = new Ship(((Ship) temp.group.get(x)).translateX + diffX,
                        ((Ship) temp.group.get(x)).translateY + diffY);
                tempShip.updateParentGroup(temp);
                temp.group.set(x, tempShip);
                if (!temp.ships.contains(tempShip))
                    temp.ships.add(tempShip);
            }
        }
        return temp;
    }
}
