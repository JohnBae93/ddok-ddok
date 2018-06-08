package edu.skku.swp3.ddokddok.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import edu.skku.swp3.ddokddok.R;

public class MainActivity extends AppCompatActivity {

    ImageButton button_m;
    ImageButton button_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_m = findViewById(R.id.imageButton_M);
        button_f = findViewById(R.id.imageButton_F);

        button_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("gender", "male");
                startActivity(intent);
            }
        });
        button_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("gender", "female");
                startActivity(intent);
            }
        });


    }
}