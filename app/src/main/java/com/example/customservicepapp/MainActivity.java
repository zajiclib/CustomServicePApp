package com.example.customservicepapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private Button btnGet;
    private TextView txtView;
    private static final String TAG = "MY_TAG";

    private final String TEST_URL = "http://192.168.105.89:8080/MyTestProject/TestServlet";
//    private final String TEST_URL = "http://192.168.105.89:8080/MyTestProject/TestServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = findViewById(R.id.btnGet);
        txtView = findViewById(R.id.txtView);


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initOkHttp3();
                } catch (IOException e) {
                    Log.e(TAG, "onClick: " + e.getCause());
                }

            }
        });


    }

    private void initOkHttp3() throws IOException {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();

        FormBody.Builder postBuilder = new FormBody.Builder();

        final int HTTP_REQUEST_TAG = 198;

        Request httpRequest = new Request.Builder()
                .url(TEST_URL)
                .tag(HTTP_REQUEST_TAG)
                .post(postBuilder.build())
                .addHeader("Content-Type", "text/html")
                .build();

//        Response response = okHttpClient.newCall(httpRequest).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        txtView.setText("Error loading site...");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        txtView.setText();
//                    }
//                });
//            }
//        });

        final Request request = new Request.Builder().url(TEST_URL).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder s = new StringBuilder();
                        s.append("Failure: \n");

                        s.append(call.toString())
                                .append("\n" + e.toString());

                        txtView.setText(s.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            txtView.setText(response.body().string());
                        } catch (IOException ioe) {
                            txtView.setText("Error during get body");
                        }
                    }
                });
            }
        });


    }
}