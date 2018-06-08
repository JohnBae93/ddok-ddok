package edu.skku.swp3.ddokddok.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.model.Acknowledgement;
import cloud.artik.model.ActionOut;
import cloud.artik.model.MessageOut;
import cloud.artik.model.UserEnvelope;
import cloud.artik.model.WebSocketError;
import cloud.artik.websocket.ArtikCloudWebSocketCallback;
import cloud.artik.websocket.ConnectionStatus;
import cloud.artik.websocket.FirehoseWebSocket;
import edu.skku.swp3.ddokddok.R;
import edu.skku.swp3.ddokddok.models.Location;
import edu.skku.swp3.ddokddok.models.Message;
import edu.skku.swp3.ddokddok.utils.AuthStateDAL;
import okhttp3.OkHttpClient;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    String gender;
    private UsersApi mUsersApi = null;
    private String mAccessToken;
    private String userId;
    private Button openFirehoseButton;
    private TextView fireSensorText;
    private Message responseMessage;
    private HashMap<String, Boolean> SensorStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        gender = intent.getStringExtra("gender");
        String color;
        if (gender.equals("male")) {
            color = "#91C3FF";
        } else {
            color = "#FF82B2";
        }

        AuthStateDAL authStateDAL = new AuthStateDAL(this);
        mAccessToken = authStateDAL.readAuthState().getAccessToken();
        Log.v("jh", "::onCreate get access token = " + mAccessToken);

        setupArtikCloudApi();

        getUserInfo();

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

        SensorStatus = new HashMap<>();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        mMap.setMyLocationEnabled(true);

        LatLng Current = new LatLng(37.295669, 126.976184);
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(Current).title("Marker in Current").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        ArrayList<Location> locationList = Location.getDefaultLocationList(this,gender);
        for (Location location : locationList){
            mMap.addMarker(new MarkerOptions().position(location.getLatLng()).title(location.getName()));
            for (String roomID : location.getRoomList()) {
                try {
                    connectWebSocket(roomID);
                } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }
        mMap.setOnMarkerClickListener(this);

        mMap.addMarker(makerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 16));


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }

    private void setupArtikCloudApi() {
        ApiClient mApiClient = new ApiClient();
        mApiClient.setAccessToken(mAccessToken);
        mUsersApi = new UsersApi(mApiClient);
    }

    private void getUserInfo() {
        final String tag = "MapActivity" + " getSelfAsync";
        try {
            mUsersApi.getSelfAsync(new ApiCallback<UserEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> map) {
                    processFailure(tag, exc);
                }

                @Override
                public void onSuccess(UserEnvelope result, int statusCode, Map<String, List<String>> map) {
                    Log.v("jh", "getSelfAsync::setup Artik CloudApi self name = " + result.getData().getFullName());
                    updateWelcomeViewOnUIThread("Welcome " + result.getData().getFullName());
                    userId = result.getData().getId();
                }

                @Override
                public void onUploadProgress(long bytes, long contentLen, boolean done) {
                }

                @Override
                public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                }
            });
        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }

    static void showErrorOnUIThread(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

    private void updateWelcomeViewOnUIThread(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });

    }

    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showErrorOnUIThread(context + errorDetail, MapsActivity.this);
    }

    private void connectWebSocket(final String device_id)
            throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.retryOnConnectionFailure();

        FirehoseWebSocket ws = new FirehoseWebSocket(client, mAccessToken, device_id, null, null, userId, new ArtikCloudWebSocketCallback() {
            @Override
            public void onOpen(int httpStatus, String httpStatusMessage)
            {
                SensorStatus.put(device_id, Boolean.FALSE);
            }

            @Override
            public void onMessage(MessageOut message) {
                Map<String, Object> data = message.getData();

                Log.d("yhj", " data is :" + data.toString());
                responseMessage.setContent(data.toString());
                Boolean status = Boolean.FALSE;

                for(String key : data.keySet()) {
                    Log.d("yhj", data.get(key).toString());

                    if(data.get(key).toString().equals("true")){
                        status = Boolean.TRUE;
                        Log.d("@@", "@@@");
                    }
                }

                SensorStatus.put(device_id, status);
            }

            @Override
            public void onAction(ActionOut action) {
                Log.d("jh", "onAction");
            }

            @Override
            public void onAck(Acknowledgement ack) {
                Log.d("jh", "onAck");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("jh", "onClose");
            }

            @Override
            public void onError(WebSocketError error) {
            }

            @Override
            public void onPing(long timestamp) {
                Log.d("jh", "onPing");
            }
        });
        if (ws.getConnectionStatus() == ConnectionStatus.CLOSED)
            ws.connect();
        else
            Toast.makeText(getApplication(), "connection already established", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
        try {
//            connectWebSocket(ma);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    private void updateListenedResponseOnUIThread(final int textId) {
//        this.runOnUiThread(new Runnable() {
//            String ret;
//
//            @Override
//            public void run() {
//                TextView listenedText = (TextView) findViewById(textId);
//                ret = msg;
//                listenedText.setText("Listened Data : \n" + ret);
//            }
//        });
//    }
}

