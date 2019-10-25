package com.originalstocks.antinomctask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.originalstocks.antinomctask.Adapters.UserRecyclerAdapter;
import com.originalstocks.antinomctask.Utils.MyUtils;
import com.originalstocks.antinomctask.Utils.MyVolleySingleton;
import com.originalstocks.antinomctask.model.UsersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "http://demo8716682.mockable.io/cardData";
    private static final String TAG = "MainActivity";
    private UserRecyclerAdapter recyclerAdapter;
    private RecyclerView usersRecyclerView;
    private List<UsersModel> usersList;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersRecyclerView = findViewById(R.id.user_details_recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        if (MyUtils.isNetworkAvailable()) {
            gettingUserData();
        } else {
            Toast.makeText(this, "Please Check your Internet Connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void gettingUserData() {
        usersList = new ArrayList<>();
        StringRequest userDataRequest = new StringRequest(Request.Method.GET, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse_fetched_data = " + response);

                try {
                    Log.i(TAG, "onResponse_fetched_data = " + response);
                    if (response != null) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        JSONArray rootArray = new JSONArray(response);
                        for (int i = 0; i < rootArray.length(); i++) {
                            UsersModel usersModel = new UsersModel(i);
                            JSONObject rootObject = rootArray.getJSONObject(i);

                            String imageURL = rootObject.getString("url");
                            String name = rootObject.getString("name");
                            String age = rootObject.getString("age");
                            String location = rootObject.getString("location");

                            usersModel.setUserImageLink(imageURL);
                            usersModel.setUserName(name);
                            usersModel.setUserAge(age);
                            usersModel.setUserLocation(location);
                            usersList.add(usersModel);

                        }

                        // setting up recycler data

                        recyclerAdapter = new UserRecyclerAdapter(MainActivity.this, usersList);
                        usersRecyclerView.setHasFixedSize(true);
                        usersRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                        usersRecyclerView.setAdapter(recyclerAdapter);

                    } else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "onResponse: response is null");
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse_exception = " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }
        };

        userDataRequest.setRetryPolicy(
                new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Access the RequestQueue through your singleton class.
        MyVolleySingleton.getInstance(this).addToRequestQueue(userDataRequest);
    }
}
