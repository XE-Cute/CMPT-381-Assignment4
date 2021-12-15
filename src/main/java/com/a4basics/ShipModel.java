package com.a4basics;

import java.util.ArrayList;
import java.util.Optional;

public class ShipModel {
    public ShipGroup shipGroups;
    ArrayList<ShipModelSubscriber> subscribers;

    public ShipModel() {
        subscribers = new ArrayList<>();
        shipGroups = new ShipGroup();
    }

    public Ship createShip(double x, double y) {
        Ship s = new Ship(x,y);
        shipGroups.group.add(s);
        shipGroups.ships.add(s);
        notifySubscribers();
        return s;
    }

    public Optional<Groupable> detectHit(double x, double y) {
        return shipGroups.ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    public void moveShip(Groupable b, double dX, double dY) {
        b.moveShip(dX,dY);
        if (b instanceof ShipGroup s)
            s.setBoxSize();

        notifySubscribers();
    }
    public void updateModel(){
        notifySubscribers();
    }


    public void addSubscriber (ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
