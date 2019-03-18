package com.example.medtogo.UserBlade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medtogo.R;
import com.example.medtogo.UserAuth.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class gamotfulldetails extends AppCompatActivity implements View.OnClickListener{
    FloatingActionButton floatingActionButton;
    ImageView itemimage;
    TextView itemprice, itemname,itemdesc, product_pk, expiry;
    Button proceed;
    SessionManager sessionManager;
    String mName, mEmail,mPhone,mID, mmAddress,mmCity,mmLat,mmLng;
    String data1,data2,data3,data4,data5,data6,data7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamotfulldetails);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        mEmail = user.get(sessionManager.EMAIL);
        mPhone = user.get(sessionManager.PHONE);
        mID = user.get(sessionManager.USERID);
        mmAddress = user.get(sessionManager.ADDRESS);
        mmCity = user.get(sessionManager.CITY);
        mmLat = user.get(sessionManager.LAT);
        mmLng = user.get(sessionManager.LNG);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        itemimage = findViewById(R.id.itemimage);
        itemprice = findViewById(R.id.itemprice);
        itemname = findViewById(R.id.itemname);
        itemdesc = findViewById(R.id.itemdesc);
        product_pk = findViewById(R.id.product_pk);
        proceed = findViewById(R.id.proceed);
        expiry = findViewById(R.id.expiry_date);

        Intent intent = getIntent();

         data1 = intent.getStringExtra("pk");
         data2 = intent.getStringExtra("imgdesc");
         data3 = intent.getStringExtra("imgurl");
         data4 = intent.getStringExtra("price");
         data5 = intent.getStringExtra("mg");
         data6 = intent.getStringExtra("product_desc");
         data7 = intent.getStringExtra("expiry_date");

        String gamot = data2 + ", "+ data5;
        String price_each = "P "+data4 + "/each";
        itemprice.setText(price_each);
        itemname.setText(gamot);
        itemdesc.setText(data6);
        product_pk.setText(data1);
        String expiry_date = "Expiry date: " + data7 ;
        expiry.setText(expiry_date);

        Picasso.with(this)
                .load(data3)
                .placeholder(R.drawable.loader)
                .into(itemimage);

        floatingActionButton.setOnClickListener(this);
        proceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == floatingActionButton){
            startActivity(new Intent(gamotfulldetails.this, RecyclerActivity.class));
        }
        if(v == proceed){
            gotocheckout();
        }
    }
    public void gotocheckout(){

        Intent intent = new Intent(gamotfulldetails.this, CheckOut.class);

        intent.putExtra("pk", data1);
        intent.putExtra("imgdesc", data2);
        intent.putExtra("imgurl", data3);
        intent.putExtra("price", data4);
        intent.putExtra("mg", data5);
        intent.putExtra("product_desc",data6);
        intent.putExtra("expiry_date", data7);
        intent.putExtra("name", mName);
        intent.putExtra("email", mEmail);
        intent.putExtra("phone", mPhone);
        intent.putExtra("userid", mID);
        intent.putExtra("address", mmAddress);
        intent.putExtra("city", mmCity);
        intent.putExtra("lat", mmLat);
        intent.putExtra("lng", mmLng);

        startActivity(intent);

    }
}
