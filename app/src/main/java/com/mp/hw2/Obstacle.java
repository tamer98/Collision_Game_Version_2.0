package com.mp.hw2;

public class Obstacle {
    private int level;
    private boolean isOn;
    private int type;
    private int position;


    public Obstacle() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public Obstacle setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean isOn() {
        return isOn;
    }

    public Obstacle setOn(boolean on) {
        isOn = on;
        return this;
    }
}
