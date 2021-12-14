package com.a4basics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class ShipController {
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY;
    double dX, dY;

    protected enum State {
        READY, DRAGGING
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
                Optional<Ship> hit = model.detectHit(x, y);
                if (hit.isPresent()) {
                    // on ship, so select
                    iModel.setSelected(hit.get());
                    currentState = State.DRAGGING;
                } else {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Ship newShip = model.createShip(x, y);
                        iModel.setSelected(newShip);
                        currentState = State.DRAGGING;
                    } else {
                        // clear selection
                        iModel.clearSelection();
                        currentState = State.READY;
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
           case DRAGGING -> model.moveShip(iModel.selectedShip, dX, dY);
        }
    }

    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case DRAGGING -> {
                currentState = State.READY;
            }
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
    }
}
