package edu.skku.swp3.ddokddok;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import okhttp3.OkHttpClient;

import static edu.skku.swp3.ddokddok.Config.DEVICE_ID_21_F_1;

public class MessageActivity extends Activity {

    private static final String TAG = "MessageActivity";

    private UsersApi mUsersApi = null;

    private String mAccessToken;

    private String userId;
    private Button openFirehoseButton;
    private TextView fireSensorText;

    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        AuthStateDAL authStateDAL = new AuthStateDAL(this);
        mAccessToken = authStateDAL.readAuthState().getAccessToken();
        Log.v(TAG, "::onCreate get access token = " + mAccessToken);

        setupArtikCloudApi();

        getUserInfo();

        openFirehoseButton = (Button) findViewById(R.id.listen_button);
        fireSensorText = (TextView) findViewById(R.id.sensor_response);

        openFirehoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, ": listen button is clicked.");
                try {
                    connectFirehoseWebSocket(DEVICE_ID_21_F_1);
                    updateListenedResponseOnUIThread(R.id.sensor_response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        openFirehoseButton.setClickable(false);
    }

    private void setupArtikCloudApi() {
        ApiClient mApiClient = new ApiClient();
        mApiClient.setAccessToken(mAccessToken);

        mUsersApi = new UsersApi(mApiClient);
    }

    private void getUserInfo() {
        final String tag = TAG + " getSelfAsync";
        try {
            mUsersApi.getSelfAsync(new ApiCallback<UserEnvelope>() {
                @Override
                public void onFailure(ApiException exc, int statusCode, Map<String, List<String>> map) {
                    processFailure(tag, exc);
                }

                @Override
                public void onSuccess(UserEnvelope result, int statusCode, Map<String, List<String>> map) {
                    Log.v(TAG, "getSelfAsync::setupArtikCloudApi self name = " + result.getData().getFullName());
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
                fireSensorText.setText(text);
                openFirehoseButton.setClickable(true);
            }
        });

    }

    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showErrorOnUIThread(context + errorDetail, MessageActivity.this);
    }

    private void connectFirehoseWebSocket(final String device_id)
            throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.retryOnConnectionFailure();

        FirehoseWebSocket ws = new FirehoseWebSocket(client, mAccessToken, device_id, null, null, userId, new ArtikCloudWebSocketCallback() {
            @Override
            public void onOpen(int httpStatus, String httpStatusMessage) {
                Log.d(TAG, "onOpen");
            }

            @Override
            public void onMessage(MessageOut message) {
                Log.d(TAG, "onMessage");
                Map<String, Object> data = message.getData();
                StringBuilder sb = new StringBuilder();
                for (String key : data.keySet()) {
                        msg = data.get(key).toString();
                }
                Log.d(TAG, data.toString());

            }

            @Override
            public void onAction(ActionOut action) {
                Log.d(TAG, "onAction");
            }

            @Override
            public void onAck(Acknowledgement ack) {
                Log.d(TAG, "onAck");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d(TAG, "onClose");
            }

            @Override
            public void onError(WebSocketError error) {
                Log.d(TAG, "onError : " + error.getMessage());
                msg = "read error";
            }

            @Override
            public void onPing(long timestamp) {
                Log.d(TAG, "onPing");
            }
        });
        if (ws.getConnectionStatus() == ConnectionStatus.CLOSED)
            ws.connect();
        else
            Toast.makeText(getApplication(), "connection already established", Toast.LENGTH_LONG).show();
    }



    private void updateListenedResponseOnUIThread(final int textId) {
        this.runOnUiThread(new Runnable() {
            String ret;

            @Override
            public void run() {

                TextView listenedText = (TextView) findViewById(textId);
                ret = msg;


                listenedText.setText("Listened Data : \n" + ret);
            }
        });
    }
} //MessageActivity
