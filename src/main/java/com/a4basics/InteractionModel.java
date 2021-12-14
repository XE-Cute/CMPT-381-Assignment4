package com.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Ship> selectedShips;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShips = new ArrayList<>();
    }

    public void clearSelection() {
        selectedShips.clear();
        notifySubscribers();
    }

    public void addSelected(Ship newSelection) {
        selectedShips.add(newSelection);
        notifySubscribers();
    }

    public void removeSelected(Ship newSelection){
        selectedShips.remove(newSelection);
        notifySubscribers();
    }

    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
