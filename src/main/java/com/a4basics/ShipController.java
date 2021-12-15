package com.a4basics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
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
                    else {
                        if (!iModel.selectedShips.contains(hit.get())) {
                            ShipGroup iterator = hit.get().getParentGroup();
                            if (iterator != null)
                                while (iterator.getParentGroup() != null)
                                    iterator = iterator.getParentGroup();
                            try {
                                iterator.ships.forEach(ship -> iModel.addSelected(ship));
                                iterator.isSelected = true;
                            } catch (NullPointerException e) {
                                iModel.addSelected(hit.get());
                            }
                        }
                        model.shipGroups.group.forEach(Groupable::setBoxSize);
                        iModel.drawGroupBox();
                    }
                    currentState = State.DRAGGING;
                } else {
                    iModel.selectedShips.forEach(s -> {
                        if (s.getParentGroup() != null) {
                            ShipGroup iterator = s.getParentGroup();
                            while (iterator.getParentGroup() != null)
                                iterator = iterator.getParentGroup();
                            iterator.isSelected = false;
                        }
                        iModel.drawGroupBox();
                    });
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
           case DRAGGING -> {
               iModel.selectedShips.forEach(e ->
                       model.moveShip(e, dX, dY));
               model.shipGroups.group.forEach(Groupable::setBoxSize);
           }
            case SELECTION -> {
                iModel.drawSelection(x, y);
                iModel.drawGroupBox();
            }
        }
    }

    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case DRAGGING ->
                currentState = State.READY;
            case SELECTION -> {
                model.shipGroups.ships.forEach(ship -> {
                    if (ship.isContained(iModel.box)) {
                        if (iModel.selectedShips.contains(ship))
                            iModel.removeSelected(ship);
                        else
                            iModel.addSelected(ship);
                    }
                });
                ShipGroup temp = new ShipGroup(new ArrayList<>(iModel.selectedShips));
                temp.ships.forEach(ship -> {
                    ShipGroup iterator = ship.getParentGroup();
                    if (iterator != null) {
                        while (iterator.getParentGroup() != null)
                            iterator = iterator.getParentGroup();
                        iterator.isSelected = true;
                        iterator.ships.forEach(s -> {
                            if (!iModel.selectedShips.contains(s))
                                iModel.addSelected(s);
                        });
                        model.shipGroups.group.forEach(Groupable::setBoxSize);
                        iModel.drawGroupBox();
                    }
                });
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
                model.shipGroups.group.removeAll(temp.ships);
                ArrayList<ShipGroup> groups = new ArrayList<>();
                temp.ships.forEach(ship -> {
                    if (!groups.contains(ship.getParentGroup()))
                        if (ship.getParentGroup() != null)
                            groups.add(ship.getParentGroup());
                });
                ArrayList<Groupable>tempGroup = new ArrayList<>(temp.group);
                tempGroup.forEach(g -> {
                    if (g.getParentGroup() != null)
                        temp.group.remove(g);
                });
                temp.group.addAll(groups);
                temp.ships.remove(null);
                groups.forEach(g -> {
                    model.shipGroups.group.remove(g);
                    temp.ships.remove(g);
                    iModel.selectedShips.remove(g);
                    g.updateParentGroup(temp);
                });
                model.shipGroups.group.add(temp);
                temp.ships.forEach(ship -> {
                    if (ship.getParentGroup() == null)
                        ship.updateParentGroup(temp);
                });
                model.shipGroups.group.forEach(Groupable::setBoxSize);
                iModel.drawGroupBox();
            }

        if (keyEvent.getCode().equals(KeyCode.U)) {
            if (iModel.selectedShips.size() > 1) {
                Groupable iterator = iModel.selectedShips.get(0);
                while (iterator.getParentGroup() != null)
                    iterator = iterator.getParentGroup();
                ArrayList<Groupable> groups = ((ShipGroup) iterator).group;
                model.shipGroups.group.remove(iterator);
                groups.forEach(g -> {
                    g.updateParentGroup(null);
                    if (g instanceof ShipGroup)
                        model.shipGroups.group.add(g);
                });
                model.shipGroups.group.forEach(Groupable::setBoxSize);
                iModel.drawGroupBox();
            }
        }
    }
}
