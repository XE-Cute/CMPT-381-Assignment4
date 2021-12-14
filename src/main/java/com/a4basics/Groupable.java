package com.a4basics;


public interface Groupable {
    void moveShip(double dx, double dy);
    boolean contains(double x, double y);
    boolean isContained(SelectionRectangle rect);
}
