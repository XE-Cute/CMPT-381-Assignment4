package com.a4basics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class ShipView extends StackPane implements ShipModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;

    public ShipView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: black");
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
    }

    public void draw() {
        double start = System.nanoTime()*10e-5;
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        model.ships.forEach(ship -> {
            if (iModel.selectedShip == ship) {
                gc.setFill(Color.YELLOW);
                gc.setStroke(Color.CORAL);
            } else {
                gc.setStroke(Color.YELLOW);
                gc.setFill(Color.CORAL);
            }
            gc.fillPolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
            gc.strokePolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
        });
        System.out.println(System.nanoTime()*10e-5 - start);
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
