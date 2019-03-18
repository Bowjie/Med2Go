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
import com.example.medtogo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerUser;
    private EditText eTextname, eTextemail, eTextphone, eTextpassword, eTextconfpassword;
    private ProgressBar progressbar;
    String getname, getemail, getphone, getpword, getconfpword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        eTextname = findViewById(R.id.txtname);
        eTextemail = findViewById(R.id.txtemail);
        eTextphone = findViewById(R.id.txtphone);
        eTextpassword = findViewById(R.id.txtpassword);
        eTextconfpassword = findViewById(R.id.txtconfpwd);
        registerUser = findViewById(R.id.registerAcc);
        progressbar = findViewById(R.id.progressbar);

        registerUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == registerUser){
            user_get_details();
        }
    }

    public void user_get_details(){
         getname = eTextname.getText().toString();
         getemail = eTextemail.getText().toString().trim();
         getphone = eTextphone.getText().toString().trim();
         getpword = eTextpassword.getText().toString().trim();
         getconfpword = eTextconfpassword.getText().toString().trim();

        if(TextUtils.isEmpty(getname) || TextUtils.isEmpty(getemail) || TextUtils.isEmpty(getphone)
                || TextUtils.isEmpty(getpword) || TextUtils.isEmpty(getconfpword)){
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else if(getpword.matches(getconfpword)){
            registerUser.setVisibility(View.GONE);
            progressbar.setVisibility(View.VISIBLE);
            account_register();
        }
    }
    public void account_register(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://kdtravelandtours.com/grabmed/user_register?getname="+getname+"&getemail="+getemail+"&getphone="+getphone+"&getpword="+getpword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(SignupActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            }
                            else{
                                Toast.makeText(SignupActivity.this, "Error registering account! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
