package com.example.medtogo.UserAuth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medtogo.MainActivity;
import com.example.medtogo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText mtxtemail, mtxtpwd;
    Button mloginbtn;
    ProgressBar progressBar;
    String getemail, getpwd;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        mtxtemail = findViewById(R.id.txtemail);
        mtxtpwd = findViewById(R.id.txtpassword);
        mloginbtn = findViewById(R.id.loginbtn);
        progressBar = findViewById(R.id.progressbar);

        mloginbtn.setOnClickListener(this);
    }

    public void gotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == mloginbtn){
            get_details();
        }
    }

    public void get_details(){
        getemail = mtxtemail.getText().toString().trim();
        getpwd = mtxtpwd.getText().toString().trim();

        if(TextUtils.isEmpty(getemail) || TextUtils.isEmpty(getpwd)){
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else{
            mloginbtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            login_process();
        }
    }
    public void login_process(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://kdtravelandtours.com/grabmed/login?email="+getemail+"&password="+getpwd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name");
                                    String email = object.getString("email").trim();
                                    String phone = object.getString("phone").trim();
                                    String userid = object.getString("userid").trim();
                                    String address = object.getString("address");
                                    String city = object.getString("city").trim();
                                    String lat = object.getString("lat").trim();
                                    String lng = object.getString("lng").trim();

                                    sessionManager.createSession(name,email,phone,userid,address,city,lat,lng);

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                mloginbtn.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Username or password is incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            mloginbtn.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Username or password is incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
