package com.example.medtogo.UserBlade;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressBookActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    SessionManager sessionManager;
    String mName, mEmail, mPhone, mID;
    String datapk,dataname,dataemail,dataAdd,dataCity,datamobile, datalat, datalng;
    TextView primary_id, latitude, longtitude;
    EditText mUsername, mAddress, mCity, mMobile, mUserEmail;
    Button saveinfo, btnButton;
    ProgressBar progressBar1, progressBar2;

    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.VISIBLE);

        sessionManager = new SessionManager(this);


        latitude = findViewById(R.id.latitude);
        longtitude = findViewById(R.id.longitude);
        primary_id = findViewById(R.id.primary_id);
        mUsername = findViewById(R.id.uname);
        mUserEmail = findViewById(R.id.uemail);
        mAddress = findViewById(R.id.uaddress);
        mCity = findViewById(R.id.ucity);
        mMobile = findViewById(R.id.uphone);
        saveinfo = findViewById(R.id.saveinfo);
        btnButton = findViewById(R.id.udetectlocation);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        mEmail = user.get(sessionManager.EMAIL);
        mPhone = user.get(sessionManager.PHONE);
        mID = user.get(sessionManager.USERID);

        primary_id.setText(mID);
        mUsername.setText(mName);
        mUserEmail.setText(mEmail);
        mMobile.setText(mPhone);

        mAddress.setVisibility(View.GONE);
        mCity.setVisibility(View.GONE);

        saveinfo.setOnClickListener(this);
        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("mylog", "Not granted");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else
                        requestLocation();
                } else
                    requestLocation();
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);
                progressBar2.setVisibility(View.GONE);
                mCity.setVisibility(View.VISIBLE);
                mCity.setText(cityName);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 13.0f));

                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(myCoordinates));
                } else
                    marker.setPosition(myCoordinates);
            }
        };

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("mylog", "Getting Location Permission");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                requestLocation();
        } else
            requestLocation();

    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(AddressBookActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            progressBar1.setVisibility(View.GONE);
            mAddress.setVisibility(View.VISIBLE);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + myCoordinates.longitude);
            Log.d("mylog", "Address: " + myCoordinates.latitude);
            String stringlat = String.valueOf(myCoordinates.latitude);
            String stringlng = String.valueOf(myCoordinates.longitude);
            latitude.setText(stringlat);
            longtitude.setText(stringlng);
            mAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            String cityName = getCityName(myCoordinates);
            progressBar2.setVisibility(View.GONE);
            mCity.setVisibility(View.VISIBLE);
            mCity.setText(cityName);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onClick(View v) {
        if(v == saveinfo){
            getinfoAddress();
        }
    }

    public void getinfoAddress(){
         datapk = primary_id.getText().toString();
         dataname = mUsername.getText().toString();
         dataemail = mUserEmail.getText().toString();
         dataAdd = mAddress.getText().toString();
         dataCity = mCity.getText().toString();
         datamobile = mMobile.getText().toString();
         datalat = latitude.getText().toString();
         datalng = longtitude.getText().toString();

        if(TextUtils.isEmpty(dataAdd) || TextUtils.isEmpty(dataCity)){
            Toast.makeText(this, "Address is not available. Please wait...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
            saveinfoaddress();
        }
    }
    public void saveinfoaddress(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://kdtravelandtours.com/grabmed/updateAccount?datapk="+datapk+"&dataname="+dataname+"&dataemail="+dataemail+"&dataAdd="+dataAdd+"&dataCity="+dataCity+"&datamobile="+datamobile+"&datalat="+datalat+"&datalng="+datalng,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                sessionManager.createSession(dataname,dataemail,datamobile,datapk,dataAdd,dataCity,datalat,datalng);
                                Toast.makeText(AddressBookActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AddressBookActivity.this, "Error Saving Account! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(AddressBookActivity.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddressBookActivity.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
