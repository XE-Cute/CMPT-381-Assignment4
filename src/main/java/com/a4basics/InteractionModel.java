package com.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    Ship selectedShip;

    public InteractionModel() {
        subscribers = new ArrayList<>();
    }

    public void clearSelection() {
        selectedShip = null;
        notifySubscribers();
    }

    public void setSelected(Ship newSelection) {
        selectedShip = newSelection;
        notifySubscribers();
    }

    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
