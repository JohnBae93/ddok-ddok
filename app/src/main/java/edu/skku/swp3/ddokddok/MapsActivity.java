package edu.skku.swp3.ddokddok;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import edu.skku.swp3.ddokddok.Config;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static edu.skku.swp3.ddokddok.Config.location_21;
import static edu.skku.swp3.ddokddok.Config.location_27;
import static edu.skku.swp3.ddokddok.Config.location_31;
import static edu.skku.swp3.ddokddok.Config.location_33;
import static edu.skku.swp3.ddokddok.Config.location_85;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        gender = intent.getStringExtra("gender");
        String color;

        if (gender.equals("male")) {
            color = "#91C3FF";
        } else {
            color = "#FF82B2";
        }

        //set status color
        Window window = super.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //set status color_END

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        mMap.setMyLocationEnabled(true);

        LatLng Current = new LatLng(37.295669, 126.976184);
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(Current).title("Marker in Current").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        LatLng BD_21 = new LatLng(location_21[0], location_21[1]);
        LatLng BD_27 = new LatLng(location_27[0], location_27[1]);
        LatLng BD_31 = new LatLng(location_31[0], location_31[1]);
        LatLng BD_33 = new LatLng(location_33[0], location_33[1]);
        LatLng BD_85 = new LatLng(location_85[0], location_85[1]);

        mMap.addMarker(new MarkerOptions().position(BD_21).title("제 1 공학관"));
        mMap.addMarker(new MarkerOptions().position(BD_27).title("제 2 공학관"));
        mMap.addMarker(new MarkerOptions().position(BD_31).title("제 1 자연과학관"));
        mMap.addMarker(new MarkerOptions().position(BD_33).title("화학관"));
        mMap.addMarker(new MarkerOptions().position(BD_85).title("산학협력센터"));

        mMap.addMarker(makerOptions);


        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current,16));

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}

