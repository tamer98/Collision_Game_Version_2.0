package com.mp.hw2;


import android.content.Context;
import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataManager {

     final static int oNum = 25;
     static int level=1;

     private List<Obstacle> obstacleList = new ArrayList<>();


     public DataManager(){

     }

     public void addObstacle(Obstacle obstacle){
         obstacleList.add(obstacle);
     }

     public void setObstacle(int pos, Obstacle obstacle){
         obstacleList.set(pos, obstacle);
     }

    public List<Obstacle> getObstacleList() {
        return obstacleList;
    }

    public int getSpeed(Context context){
         SPManager spManager = new SPManager(context);
         if(spManager.getB("slow")){
             return 5;
         } else {
             return 2;
         }
    }

    public void createAndAddRandomObstacle(int pos, List<ShapeableImageView> game_IMG_obstacle){
         Obstacle obstacle = new Obstacle();
         if(getRandomBoolean()){
             obstacle.setType(0);
         } else {
             obstacle.setType(1);
         }
         obstacle.setPosition(pos);
         obstacle.setOn(true);
         addObstacle(obstacle);
        if (obstacle.getType() == 0) {
            game_IMG_obstacle.get(pos).setImageResource(R.drawable.obstacle);
        } else {
            game_IMG_obstacle.get(pos).setImageResource(R.drawable.coin);
        }

        game_IMG_obstacle.get(pos).setVisibility(View.VISIBLE);
     }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

     public static ArrayList<Obstacle> getObstacles(){
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        for (int i = 0; i < oNum; i++) {
            Obstacle o = new Obstacle()
                    .setLevel(level)
                    .setOn(false);

                if((i + 1) % 5 == 0) {
                    level++;
                }

            obstacles.add(o);
        }
        return obstacles;
     }
}
