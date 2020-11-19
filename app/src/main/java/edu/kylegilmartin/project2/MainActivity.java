package edu.kylegilmartin.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    SensorManager sensorManager;

    Boolean runnning = false;
    public Float totalSteps = 0f;
    public Float prevuTotalSteps = 0f;


    TextView tvStep;
    CircularProgressBar ttProgress_circular;

    private Chronometer chronometer;
    private  boolean ChronometerRunnin;
    private  long pauseOffset;

    private Button buttonStats;
    private ImageButton btsettings;
float q;

TextView tvoutoff;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ButterKnife.bind(this);
        loadData();
        saveData();
        showDialog();
        checkPermission();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tvStep = findViewById(R.id.tvStep);
        tvoutoff = findViewById(R.id.tvOutOff);
        ttProgress_circular = findViewById(R.id.ttProgress_circular);
        q = getIntent().getIntExtra("EnterDisance",0);
        if (q <= 0){
            q = 10;
        }
        ttProgress_circular.setProgressMax(q);
        tvoutoff.setText("/"+ q);
        ttProgress_circular.setProgressBarColorStart(Color.GREEN);
        ttProgress_circular.setProgressBarColorEnd(Color.RED);
        chronometer = findViewById(R.id.chronometerT);

        buttonStats = findViewById(R.id.btRun);
        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStatsPage();

            }
        });

        btsettings = (ImageButton)findViewById(R.id.btSettings1);
        btsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPage();
            }
        });

    }



    public void openSettingsPage(){
        Intent intent = new Intent(this,SettingsPage.class);



        startActivity(intent);
    }

    public void openStatsPage(){
        Intent intent = new Intent(this, StatsPage.class);

        // changes the timer to seconds and pushes to second page
        int elapsedTimeInSec = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        intent.putExtra("timedata", elapsedTimeInSec);

        // pushe the steps the the second page
        int TotalSteps = (int)Integer.valueOf(tvStep.getText().toString());
        intent.putExtra("steps",TotalSteps);

        startActivity(intent);
    }



    public void doReset(View view) {
        doStop(chronometer);
        Toast.makeText(this,"Steps and timer reset",Toast.LENGTH_LONG).show();
        prevuTotalSteps = totalSteps;
        tvStep.setText("0");
        ttProgress_circular.setProgressWithAnimation(0f);
        saveData();

        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;

    }

    public void doStart(View view) {
        if (!ChronometerRunnin){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            ChronometerRunnin = true;
        }
    }

    public void doStop(View view) {
        if (ChronometerRunnin){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            ChronometerRunnin = false;

        }
    }





    @Override
    public void onSensorChanged(SensorEvent event) {
        if (runnning){
            totalSteps = event.values[0];
            int current = totalSteps.intValue() - prevuTotalSteps.intValue();
            tvStep.setText(String.valueOf(current));
            ttProgress_circular.setProgressWithAnimation((float) current);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        runnning = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            Toast.makeText(this, "No Sensor Detected", Toast.LENGTH_LONG).show();
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    void checkPermission(){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 11);
        }
    }


    private void saveData() {

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editer = sharedPref.edit();
        editer.putFloat("key1", prevuTotalSteps);
        editer.apply ();




    }

   private void loadData() {

        SharedPreferences sharedPreference = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Float savedNumber = sharedPreference.getFloat("key1", 0f);
        prevuTotalSteps = savedNumber;




    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogCustomTheme);
        builder.setMessage("").setTitle("reqired").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}