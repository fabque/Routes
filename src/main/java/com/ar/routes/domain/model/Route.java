package com.ar.routes.domain.model;

public class Route {

    private Station origin;
    private Station destination;
    private double cost;

    public Route(Station origin, Station destination, double cost) {
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Station getOrigin() {
        return origin;
    }

    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }
}
