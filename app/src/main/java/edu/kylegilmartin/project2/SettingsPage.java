package edu.kylegilmartin.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsPage extends AppCompatActivity {
    Button btback;
    TextView tventerdisyance;
    TextView tvseletednumber;
    Button tvsubmit;
    int d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        btback = findViewById(R.id.tbBackToMain);
        tventerdisyance = findViewById(R.id.tvEnterDisance);
       // tvseletednumber = findViewById(R.id.tvSelectedNumber);
        tvsubmit = findViewById(R.id.btSubmitNumber);
        d = 10;

//        tvsubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////          //  String e;
//    e = String.valueOf(tventerdisyance).toString();
////                tvseletednumber.setText(String.valueOf(e).toString());
//            }
//        });


        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain2();
            }
        });
    }

    public void openMain2(){
        Intent intent = new Intent(this,MainActivity.class);

        d = (int)Integer.valueOf(tventerdisyance.getText().toString());
        intent.putExtra("EnterDisance",d);


        startActivity(intent);
    }
}