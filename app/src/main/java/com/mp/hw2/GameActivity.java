package com.mp.hw2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
//                                                  ^ implements SensorEventListener

    ExtendedFloatingActionButton player_FAB_right;
    ExtendedFloatingActionButton player_FAB_left;
    LocationManager locationManager;
    double latitude;
    double longitude;

    private ShapeableImageView[] game_IMG_locations;
    private ShapeableImageView[] game_IMG_hearts;
    private List<ShapeableImageView> game_IMG_obstacle = new ArrayList<>();
    //    private SensorManager senSensorManager;
//    private Sensor senAccelerometer;
    private MediaPlayer mediaPlayer;
    private GameManager gameManager;
    Uri eatUri, loseUri, coinUri;
    private TextView coinText;
    OrientationEventListener orientationListener;
    private FusedLocationProviderClient fusedLocationClient;
    private DataManager dataManager;
    boolean sensorMode;
    List<HighModel> coinList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        mediaPlayer = new MediaPlayer();

        eatUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.crash);
        loseUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lose);
        coinUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.coin);

        gameManager = new GameManager(game_IMG_hearts.length, 0);
//        startTimer();
//        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        dataManager = new DataManager();
        orientationListener = createOrientationListener();
        orientationListener.enable();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);

        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude =  location.getLatitude();
                                longitude=  location.getLongitude();
                                System.out.println("find location " + latitude + "  " + longitude);
                            } else {
                                System.out.println("Still null " + latitude + "  " + longitude);

                            }
                        }
                    });
        }
        startRandom();
        checkStatus();
        coinList = new SPManager(this).getL("coins");
        if (coinList == null) coinList = new ArrayList<>();
    }

    private void initViews() {
        player_FAB_right.setOnClickListener(view -> {
            clicked(1);
        });
        player_FAB_left.setOnClickListener(view -> {
            clicked(-1);
        });
    }

    private void findViews() {
        coinText = findViewById(R.id.coin);
        player_FAB_right = findViewById(R.id.FAB_right);
        player_FAB_left = findViewById(R.id.FAB_left);

        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

        game_IMG_locations = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_player_left0), // 0
                findViewById(R.id.game_IMG_player_left), // 1
                findViewById(R.id.game_IMG_player_center),// 2
                findViewById(R.id.game_IMG_player_right),// 2
                findViewById(R.id.game_IMG_player_right0)// 2
        };

        game_IMG_obstacle.add(findViewById(R.id.game_OB_11)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_12)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_13)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_14)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_15));
        game_IMG_obstacle.add(findViewById(R.id.game_OB_21)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_22)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_23)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_24)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_25)); // level 1
        game_IMG_obstacle.add(findViewById(R.id.game_OB_31)); // level 2
        game_IMG_obstacle.add(findViewById(R.id.game_OB_32)); // level 2
        game_IMG_obstacle.add(findViewById(R.id.game_OB_33)); // level 2
        game_IMG_obstacle.add(findViewById(R.id.game_OB_34)); // level 2
        game_IMG_obstacle.add(findViewById(R.id.game_OB_35)); // level 2
        game_IMG_obstacle.add(findViewById(R.id.game_OB_41)); // level 3
        game_IMG_obstacle.add(findViewById(R.id.game_OB_42)); // level 3
        game_IMG_obstacle.add(findViewById(R.id.game_OB_43)); // level 3
        game_IMG_obstacle.add(findViewById(R.id.game_OB_44)); // level 3
        game_IMG_obstacle.add(findViewById(R.id.game_OB_45)); // level 3
        game_IMG_obstacle.add(findViewById(R.id.game_OB_51)); // level 4
        game_IMG_obstacle.add(findViewById(R.id.game_OB_52)); // level 4
        game_IMG_obstacle.add(findViewById(R.id.game_OB_53)); // level 4
        game_IMG_obstacle.add(findViewById(R.id.game_OB_54)); // level 4
        game_IMG_obstacle.add(findViewById(R.id.game_OB_55));  // level 4
        atPlayerStart();
        atObstacleStart();
    }

    private void clicked(int answer) {
        if (gameManager == null) return;
        gameManager.movePlayerLocation(game_IMG_locations, answer);
    }

    public void atPlayerStart() {
        game_IMG_locations[0].setImageResource(R.drawable.car);
        game_IMG_locations[1].setImageResource(R.drawable.car);
        game_IMG_locations[2].setImageResource(R.drawable.car);
        game_IMG_locations[3].setImageResource(R.drawable.car);
        game_IMG_locations[4].setImageResource(R.drawable.car);
        game_IMG_locations[0].setVisibility(View.INVISIBLE);
        game_IMG_locations[1].setVisibility(View.INVISIBLE);
        game_IMG_locations[2].setVisibility(View.VISIBLE);
        game_IMG_locations[3].setVisibility(View.INVISIBLE);
        game_IMG_locations[4].setVisibility(View.INVISIBLE);
    }

    public void atObstacleStart() {

        for (ShapeableImageView s : game_IMG_obstacle) {
//            s.setImageResource(R.drawable.obstacle);
            s.setVisibility(View.INVISIBLE);
        }
    }


    private void refreshUI() {

        List<Obstacle> obstacleList = dataManager.getObstacleList();
        for (int i = 0; i < obstacleList.size(); i++) {
            Obstacle obstacle = obstacleList.get(i);
            if (obstacle.isOn()) {
                int pos = obstacle.getPosition();
                int listPos = i;
                int nPos = pos - 5;
                System.out.println("new pos :" + nPos);
                if (nPos >= 0) {

                    if (obstacle.getType() == 0) {
                        game_IMG_obstacle.get(nPos).setImageResource(R.drawable.obstacle);
                    } else {
                        game_IMG_obstacle.get(nPos).setImageResource(R.drawable.coin);
                    }

                    game_IMG_obstacle.get(pos).setVisibility(View.INVISIBLE);
                    game_IMG_obstacle.get(nPos).setVisibility(View.VISIBLE);
                } else {
                    obstacle.setOn(false);
                    game_IMG_obstacle.get(pos).setVisibility(View.INVISIBLE);
                    checkAnswer(obstacle);
                }
                obstacle.setPosition(nPos);
                dataManager.setObstacle(listPos, obstacle);
            }
        }
//


    }

    private void checkAnswer(Obstacle obstacle) {
        int playerPos = GameManager.liveLocation;
        int oPos = Math.abs(obstacle.getPosition());
        System.out.println("player pos " + playerPos);
        if (playerPos == oPos) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            System.out.println("crashed ");
            if (obstacle.getType() == 0) {
                gameManager.setWrong(gameManager.getWrong() + 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(getApplicationContext(), eatUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (gameManager.isLose()) {
                    System.out.println("lose ");

                    refreshTimer.cancel();
                    refreshTimer = null;
                    randomTimer.cancel();
                    randomTimer = null;

                    try {

                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(getApplicationContext(), loseUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        mediaPlayer.setLooping(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(GameActivity.this, HighScoreActivity.class));

                } else if (gameManager.getWrong() != 0) {
                    if (game_IMG_hearts.length - gameManager.getWrong() >= 0)
                        game_IMG_hearts[game_IMG_hearts.length - gameManager.getWrong()].setVisibility(View.INVISIBLE);
                } else {

                }
            } else {
//                coin
                try {

                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(getApplicationContext(), coinUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setLooping(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                gameManager.setCoins(gameManager.getCoins() + 1);
                coinText.setText(String.valueOf(gameManager.getCoins()));
            }
        } else {

        }
    }

    private Timer randomTimer;

    private void startRandom() {
        int speed = dataManager.getSpeed(this);

        if (randomTimer == null) randomTimer = new Timer();
        System.out.println("size " + game_IMG_obstacle.size());
        randomTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                dataManager.addObstacle(new Obstacle().setLevel(5).setOn(true));
                runOnUiThread(() -> {
                    int random = new Random().nextInt((24 - 20) + 1) + 20; // [0, 2] + 9 => [9, 11]
                    dataManager.createAndAddRandomObstacle(random, game_IMG_obstacle);

                });

            }
        }, speed * 1000L, speed * 1000L);
    }

    private Timer refreshTimer;

    private void checkStatus() {
        int speed = dataManager.getSpeed(this);

        if (refreshTimer == null) refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> refreshUI());
            }
        }, (speed * 1000L) / 2, (speed * 1000L) / 2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (refreshTimer != null)
            refreshTimer.cancel();
        refreshTimer = null;
        if (randomTimer != null)
            randomTimer.cancel();
        randomTimer = null;
        if (gameManager.getCoins() > 0) {
            HighModel model = new HighModel(gameManager.getCoins(), latitude, longitude);
            coinList.add(model);
            new SPManager(this).setList("coins", coinList);
        }
        gameManager = null;
        coinText.setText("0");


//        senSensorManager.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameManager = new GameManager(3, 0);
        dataManager.getObstacleList().clear();
        sensorMode = new SPManager(this).getB("sensor");
        atPlayerStart();
        atObstacleStart();
        startRandom();
        checkStatus();
        if (sensorMode) {
            player_FAB_right.setVisibility(View.GONE);
            player_FAB_left.setVisibility(View.GONE);
            orientationListener.enable();
        } else {
            player_FAB_right.setVisibility(View.VISIBLE);
            player_FAB_left.setVisibility(View.VISIBLE);
            orientationListener.disable();
        }
//        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    //    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//
//    }
    private OrientationEventListener createOrientationListener() {
        return new OrientationEventListener(this) {
            public void onOrientationChanged(int orientation) {
                if (!sensorMode) return;
                System.out.println("orientation................." + orientation);
                if (orientation < 300) {
                    clicked(1);
                } else {
                    clicked(-1);
                }
            }
        };
    }


    public void menu(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}

