package com.a4basics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        model.shipGroups.ships.forEach(ship -> {
            if (iModel.selectedShips.contains(ship)) {
                gc.setFill(Color.YELLOW);
                gc.setStroke(Color.CORAL);
            } else {
                gc.setStroke(Color.YELLOW);
                gc.setFill(Color.CORAL);
            }
            gc.fillPolygon(((Ship)ship).displayXs, ((Ship)ship).displayYs, ((Ship)ship).displayXs.length);
            gc.strokePolygon(((Ship)ship).displayXs, ((Ship)ship).displayYs, ((Ship)ship).displayXs.length);
        });
        if (iModel.box != null) {
            Paint savePaint = gc.getFill();
            gc.setFill(Color.rgb(255, 186, 3, 0.5));
            gc.fillRect(iModel.box.x, iModel.box.y, iModel.box.width, iModel.box.height);
            gc.setFill(savePaint);
        }
        model.shipGroups.group.forEach(ships -> {
            if (ships instanceof ShipGroup s && s.isSelected) {
                SelectionRectangle rect = s.outline;
                Paint saveStroke = gc.getStroke();
                gc.setStroke(Color.WHITE);
                gc.strokeRect(rect.x, rect.y, rect.width, rect.height);
                gc.setStroke(saveStroke);
            }
        });

    }
    @Override
    public void modelChanged() {
        draw();
    }
}
