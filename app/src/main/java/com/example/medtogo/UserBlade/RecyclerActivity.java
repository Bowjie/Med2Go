package com.example.medtogo.UserBlade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medtogo.R;
import com.example.medtogo.UserAuth.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    TextView noappoints;
    private List<ListItem> listItems;
    SessionManager sessionManager;
    ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        recyclerView = findViewById(R.id.recyclerView);
        noappoints= findViewById(R.id.noappoints);
        progressbar = findViewById(R.id.progressbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        loadRecycleViewData();

    }
    private void loadRecycleViewData(){
        progressbar.setVisibility(View.VISIBLE);
        noappoints.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kdtravelandtours.com/grabmed/recyclerview",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressbar.setVisibility(View.GONE);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("gamotdetails");

                            for(int i = 0; i<array.length(); i++){
                                JSONObject o = array.getJSONObject(i);

                                if(o.getString("data1").matches("")){
                                    noappoints.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.GONE);
                                } else {
                                    noappoints.setVisibility(View.GONE);
                                    ListItem item = new ListItem(
                                            o.getString("data1"),
                                            o.getString("data2"),
                                            o.getString("data3"),
                                            o.getString("data4"),
                                            o.getString("data5"),
                                            o.getString("data7")
                                    );
                                    listItems.add(item);
                                }
                            }
                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(RecyclerActivity.this, "Error internet connection"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}