package edu.kylegilmartin.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StatsPage extends AppCompatActivity {

    private Button buttonBack;
    TextView TvTimerStats, tvsteps, tvdata;
    TextView tvcal,tvdistance;
    int t;
    int s;


    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_page);

        tvdata = findViewById(R.id.tvDate);

        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        tvdata.setText(date);


        buttonBack = findViewById(R.id.btBack);
        TvTimerStats = findViewById(R.id.tvTimerStats);
        tvsteps = findViewById(R.id.tvSteps1);
        tvdistance =findViewById(R.id.tvDistance);
        tvcal = findViewById(R.id.tvCal);






        t = getIntent().getIntExtra("timedata",-1);
        s = getIntent().getIntExtra("steps",-1);

        TvTimerStats.setText(String.valueOf((t)+"S"));
        tvsteps.setText(String.valueOf((s)));

        double dis;
        double cal;
        dis = s * 0.04;
        cal = t * 0.8;

        tvdistance.setText(String.valueOf( ( dis)));
        tvcal.setText(String.valueOf(((int) cal)));


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }



    public void openMain(){
        Intent intent = new Intent(this,MainActivity.class);


        startActivity(intent);
    }
}