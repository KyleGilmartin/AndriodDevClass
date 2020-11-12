package edu.kylegilmartin.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    SensorManager sensorManager;

    Boolean runnning = false;
    public Float totalSteps = 0f;
    public Float prevuTotalSteps = 0f;
    private static final int FINE_LOC = 101;

    TextView tvStep;
    CircularProgressBar ttProgress_circular;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tvStep = findViewById(R.id.tvStep);
        ttProgress_circular = findViewById(R.id.ttProgress_circular);
    }

        

    @OnClick(R.id.tvStep)
    void clickStep(){
        Toast.makeText(this, "Long click to reset", Toast.LENGTH_LONG).show();
    }


    @OnLongClick(R.id.tvStep)
    void longClick(){
        prevuTotalSteps = totalSteps;
        tvStep.setText("0");
        ttProgress_circular.setProgressWithAnimation(0f);
        saveData();
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

    private  void saveData() {
        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editer = sharedPref.edit();
        editer.putFloat("key1", prevuTotalSteps);
        editer.apply ();
    }

    private void loadData(){
        SharedPreferences sharedPreference = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Float savedNumber = sharedPreference.getFloat("key1", 0f);
        prevuTotalSteps = savedNumber;
    }

    private void showDialog(String permission,String name, int code){
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