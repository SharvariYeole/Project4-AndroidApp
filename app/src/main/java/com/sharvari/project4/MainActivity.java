package com.sharvari.project4;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //private static final String WEB_URL = "https://super-tribble-j9747qj4pxr2pqq5-8000.app.github.dev/accounts";
    private static final String WEB_URL = "http://192.168.1.9:8080/webserv-1.0-SNAPSHOT/hello-servlet";
    EditText etSearchText;
    Button btSubmit;
    List<DisplayItem> dataListDefault = Collections.singletonList(new DisplayItem("0","No Result", "", ""));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchText = (EditText) findViewById(R.id.etSearchBox);
        btSubmit = (Button) findViewById(R.id.bt_Submit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearchText.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "Enter minimum 5 chars...", Toast.LENGTH_LONG).show();
                } else {
                    makeRequest(WEB_URL, etSearchText.getText().toString());
                }
            }
        });
    }

    private void makeRequest(String url, String key) {
        OkHttpClient client = new OkHttpClient();
        String json = "{\"key\":\"" + key + "\"}";
        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("TAG", "Error occurred: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed to make request", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView recyclerView = findViewById(R.id.rvSearchResult);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                DisplayAdapter adapter = new DisplayAdapter(dataListDefault);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String responseData = response.body().string();

                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        List<DisplayItem> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String title = jsonObject.getString("title");
                            String author = jsonObject.getString("author");
                            String date = jsonObject.getString("date");
                            final String TAG = "MainActivity";
                            Log.i(TAG, "data: " + title + " " + date   );
                            dataList.add(new DisplayItem(String.valueOf(i + 1), title, author, date));
                        }

                        if (dataList.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView recyclerView = findViewById(R.id.rvSearchResult);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    recyclerView.setLayoutManager(layoutManager);
                                    DisplayAdapter adapter = new DisplayAdapter(dataListDefault);
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     RecyclerView recyclerView = findViewById(R.id.rvSearchResult);
                                     LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                     recyclerView.setLayoutManager(layoutManager);
                                     DisplayAdapter adapter = new DisplayAdapter(dataList);
                                     recyclerView.setAdapter(adapter);
                                 }
                             });
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Response: " + responseData, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.e("TAG", "Error occurred: " + response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = findViewById(R.id.rvSearchResult);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            DisplayAdapter adapter = new DisplayAdapter(dataListDefault);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }
}