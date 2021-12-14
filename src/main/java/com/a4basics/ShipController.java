package com.a4basics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Optional;

public class ShipController {
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY;
    double dX, dY;

    protected enum State {
        READY, DRAGGING, SELECTION
    }

    protected State currentState;

    public ShipController() {
        currentState = State.READY;
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void handlePressed(double x, double y, MouseEvent event) {
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                // context: on a ship?
                Optional<Groupable> hit = model.detectHit(x, y);
                if (hit.isPresent()) {
                    // on ship, so select
                    if (!event.isControlDown() && !iModel.selectedShips.contains(hit.get()))
                        iModel.clearSelection();
                    if (event.isControlDown() && iModel.selectedShips.contains(hit.get()))
                        iModel.removeSelected(hit.get());
                    else
                        if (!iModel.selectedShips.contains(hit.get())) {
                            try{
                                ((Ship)hit.get()).group.ships.forEach(ship -> iModel.addSelected(ship));
                            } catch (NullPointerException e) {
                                iModel.addSelected(hit.get());
                            }
                        }
                    currentState = State.DRAGGING;
                } else {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Ship newShip = model.createShip(x, y);
                        iModel.clearSelection();
                        iModel.addSelected(newShip);
                        currentState = State.DRAGGING;
                    } else {
                        // clear selection
                        if (!event.isControlDown())
                            iModel.clearSelection();
                        iModel.box = new SelectionRectangle(x, y, 0, 0);
                        currentState = State.SELECTION;
                    }
                }
            }
        }
    }

    public void handleDragged(double x, double y, MouseEvent event) {
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {
           case DRAGGING -> iModel.selectedShips.forEach(e ->
                   model.moveShip(e, dX, dY));
            case SELECTION ->
                iModel.drawSelection(x, y);
        }
    }

    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case DRAGGING ->
                currentState = State.READY;
            case SELECTION -> {
                model.ships.forEach(ship -> {
                    if (ship.isContained(iModel.box)) {
                        if (iModel.selectedShips.contains(ship))
                            iModel.removeSelected(ship);
                        else
                            iModel.addSelected(ship);
                    }
                });
                try {
                    iModel.selectedShips.forEach(ship -> {
                        if (((Ship) ship).group != null)
                            ((Ship)ship).group.ships.forEach(s -> {
                                if (!iModel.selectedShips.contains(s))
                                    iModel.addSelected(s);
                            });
                    });
                }
                catch (NullPointerException | ConcurrentModificationException ignored){}
                iModel.clearSelectionRectangle();
                currentState = State.READY;
            }
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode().equals(KeyCode.G))
            if (iModel.selectedShips.size() > 1) {
                ShipGroup temp = new ShipGroup(new ArrayList<>(iModel.selectedShips));
                iModel.selectedShips.forEach(ship -> ((Ship)ship).group = temp);
                model.shipGroup.addToGroup(temp);
            }
    }
}
