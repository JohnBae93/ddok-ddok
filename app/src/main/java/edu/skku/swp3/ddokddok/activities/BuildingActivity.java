package edu.skku.swp3.ddokddok.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.skku.swp3.ddokddok.R;
import edu.skku.swp3.ddokddok.models.Location;
import edu.skku.swp3.ddokddok.models.SensorState;

/**
 * Created by John on 2018-06-12.
 */

public class BuildingActivity extends AppCompatActivity {

    String gender;

    ImageView mImageView;
    TextView mText1;
    TextView mText2;
    TextView mText3;
    ImageButton mButton1;
    ImageButton mButton2;
    ImageButton mButton3;


    int current_floor;
    private ArrayList<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_27);

        mImageView = findViewById(R.id.imageView);
        mText1 = findViewById(R.id.textView1);
        mText2 = findViewById(R.id.textView2);
        mText3 = findViewById(R.id.textView3);
        mButton1 = findViewById(R.id.imageButton1);
        mButton2 = findViewById(R.id.imageButton2);
        mButton3 = findViewById(R.id.imageButton3);


        current_floor = 1;

        mText1.setText("(1 / 3)");
        mText2.setText("(1 / 3)");
        mText3.setText("(3 / 3)");
        mText3.setTextColor(Color.RED);

        Intent intent = getIntent();
        gender = intent.getStringExtra("gender");
        String color;
        if (gender.equals("male")) {
            color = "#91C3FF";
        } else {
            color = "#FF82B2";
        }

        locationList = Location.getDefaultLocationList(this, gender);


        Window window = super.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        SetRestroomStateText(0,2, mText2);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("yh", "onclick");
                mImageView.setImageResource(R.drawable.image_271);
                current_floor = 1;
                SetRestroomStateText(0,3, mText2);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageResource(R.drawable.image_272);
                current_floor = 2;
                SetRestroomStateText(3,6, mText2);
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageResource(R.drawable.image_273);
                current_floor = 3;
                SetRestroomStateText(6,9, mText2);
            }
        });

    }

    private int SetRestroomState(ArrayList<String> roomList) {
        int count = 0;
        for (String room_device_id : roomList){
            if(SensorState.getInstance().getState(room_device_id)){
                count ++;
            }
        }
        return count;
    }

    private void SetRestroomStateText(int fromindex, int toindex, TextView textView){
        int available_room_num;
        available_room_num = SetRestroomState(new ArrayList<>(locationList.get(1).getRoomList().subList(fromindex,toindex)));
        textView.setText(String.format("(%d / 3)", available_room_num));
        if(available_room_num==3){
            textView.setTextColor(Color.RED);
        }else{
            textView.setTextColor(Color.BLACK);
        }

    }
}