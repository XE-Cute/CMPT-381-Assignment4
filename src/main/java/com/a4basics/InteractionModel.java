package com.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Groupable> selectedShips;
    SelectionRectangle box;


    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShips = new ArrayList<>();
        box = null;
    }

    public void clearSelection() {
        selectedShips.clear();
        notifySubscribers();
    }

    public void addSelected(Groupable newSelection) {
        selectedShips.add(newSelection);
        notifySubscribers();
    }

    public void removeSelected(Groupable newSelection){
        selectedShips.remove(newSelection);
        notifySubscribers();
    }
    public void drawGroupBox(){
        notifySubscribers();
    }

    public void drawSelection(double newX, double newY){
        if (box.originX < newX)
            box.width = newX-box.x;
        else {
            box.x = newX;
            box.width = box.originX - newX;
        }
        if (box.originY < newY)
            box.height = newY-box.y;
        else{
            box.y = newY;
            box.height = box.originY - newY;
        }
        notifySubscribers();
    }

    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    public void clearSelectionRectangle(){
        box = null;
        notifySubscribers();
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
