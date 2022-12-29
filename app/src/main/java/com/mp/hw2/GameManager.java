package com.mp.hw2;

import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

    private int wrong = 0;
    private int life;
    static int liveLocation = 0;
    private int coins;
    private ArrayList<Obstacle> obstacles;


    public GameManager(int life, int coins) {
        this.life = life;
        this.coins = coins;
        obstacles = DataManager.getObstacles();
        liveLocation = 2;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getWrong() {
        return wrong;
    }

    public boolean isLose() {
        return life == wrong;
    }

    public int getLiveLocation() {
        return liveLocation;
    }

    public void movePlayerLocation(ShapeableImageView[] locations, int answer) {

        liveLocation = liveLocation + answer;

        if (liveLocation < 0) {
            liveLocation++;
            return;
        } else if (liveLocation > 4) {
            liveLocation--;
            return;
        }


        for (ShapeableImageView imageView : locations) {
            imageView.setVisibility(View.INVISIBLE);
        }
        locations[liveLocation].setVisibility(View.VISIBLE);

    }

    public void moveObstacleLocation(List<ShapeableImageView> img_obstacle) {
        int level;

        for (int i = obstacles.size() - 1; i >= 0; i--) {
            level = obstacles.get(i).getLevel();
            if (obstacles.get(i).isOn() && level > 1 && (i-5) >= 0) {
                obstacles.get(i - 5).setOn(true);
                img_obstacle.get(i - 5).setVisibility(View.VISIBLE);
                obstacles.get(i).setOn(false);
                img_obstacle.get(i).setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    public void setRandomObstacle(List<ShapeableImageView> img_obstacle) {
        int random = new Random().nextInt((24 - 20) + 1) + 20; // [0, 2] + 9 => [9, 11]
        obstacles.get(random).setOn(true);
        img_obstacle.get(random).setVisibility(View.VISIBLE);
    }

    public void setWrong(int wrong){
        this.wrong = wrong;
    }

}

