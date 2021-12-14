package com.a4basics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.stream.DoubleStream;

public class Ship {
    double translateX, translateY;
    double[] xs = {0,20,0,-20,0};
    double[] ys = {24,-20,-12,-20,24};
    double shipWidth, shipHeight;
    double[] displayXs, displayYs;
    WritableImage buffer;
    PixelReader reader;
    double clickX, clickY;


    public Ship(double newX, double newY) {
        Canvas shipCanvas;
        GraphicsContext gc;

        translateX = newX;
        translateY = newY;
        double minVal = DoubleStream.of(xs).min().getAsDouble();
        double maxVal = DoubleStream.of(xs).max().getAsDouble();
        shipWidth = maxVal - minVal;
        minVal = DoubleStream.of(ys).min().getAsDouble();
        maxVal = DoubleStream.of(ys).max().getAsDouble();
        shipHeight = maxVal - minVal;
        displayXs = new double[xs.length];
        displayYs = new double[ys.length];
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + shipWidth/2;
            displayYs[i] = ys[i] + shipHeight/2;
        }

        shipCanvas = new Canvas(shipWidth,shipHeight);
        gc = shipCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillPolygon(displayXs, displayYs, displayXs.length);
        buffer = shipCanvas.snapshot(null,null);
        reader = buffer.getPixelReader();

        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + translateX;
            displayYs[i] = ys[i] + translateY;
        }
    }

    public boolean contains(double x, double y) {
        clickX = x - translateX + shipWidth/2;
        clickY = y - translateY + shipHeight/2;
        // check bounding box first, then bitmap
        boolean inside = false;
        if (clickX >= 0 && clickX <= shipWidth && clickY >= 0 && clickY <= shipHeight) {
            if (reader.getColor((int) clickX, (int) clickY).equals(Color.BLACK)) inside = true;
        }
        return inside;
    }

    public void moveShip(double dx, double dy) {
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] += dx;
            displayYs[i] += dy;
        }
        translateX += dx;
        translateY += dy;
    }

    public void rotate(double a) {
        rotate(a,translateX,translateY);
    }

    public void rotate(double a, double cx, double cy) {
        double x, y;
        double radians = a * Math.PI / 180;
        for (int i = 0; i < displayXs.length; i++) {
            x = displayXs[i] - cx;
            y = displayYs[i] - cy;
            displayXs[i] = rotateX(x, y, radians) + cx;
            displayYs[i] = rotateY(x, y, radians) + cy;
        }
    }

    private double rotateX(double x, double y, double thetaR) {
        return Math.cos(thetaR) * x - Math.sin(thetaR) * y;
    }

    private double rotateY(double x, double y, double thetaR) {
        return Math.sin(thetaR) * x + Math.cos(thetaR) * y;
    }

    public boolean isContained(SelectionRectangle rect) {
        if (rect.x < translateX && translateX < rect.x+ rect.width)
            return rect.y < translateY && translateY < rect.y + rect.height;
        return false;
    }
}
