package com.example.medtogo.UserBlade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.medtogo.MainActivity;
import com.example.medtogo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CheckOut extends AppCompatActivity implements View.OnClickListener{

    EditText uname, uemail, uaddress, ucity, add_notes, quantity;
    ImageView image_view, prescriptionimg;
    TextView img_description,Price,miligram, expiry;
    TextView product_id, primary_id;
    Button saveinfo, choose_image;
    final int CODE_GALLERY_REQUEST = 999;
    private static String URL_PROCESS = "https://kdtravelandtours.com/grabmed/final_order";
    Bitmap bitmap;

    String data1,data2,data3,data4,data5,data6,data7,data8,data9,data10,data11,data12,data13,data14,data15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        uaddress = findViewById(R.id.uaddress);
        ucity = findViewById(R.id.ucity);
        image_view = findViewById(R.id.image_view);
        img_description = findViewById(R.id.img_description);
        Price = findViewById(R.id.Price);
        miligram = findViewById(R.id.miligram);
        product_id = findViewById(R.id.product_id);
        primary_id = findViewById(R.id.primary_id);
        saveinfo = findViewById(R.id.saveinfo);
        add_notes = findViewById(R.id.add_notes);
        prescriptionimg = findViewById(R.id.prescriptionimg);
        choose_image = findViewById(R.id.chooseimg);
        expiry = findViewById(R.id.expiry);
        quantity = findViewById(R.id.quantity);

        Intent intent = getIntent();

        data1 = intent.getStringExtra("pk");
        data2 = intent.getStringExtra("imgdesc");
        data3 = intent.getStringExtra("imgurl");
        data4 = intent.getStringExtra("price");
        data5 = intent.getStringExtra("mg");
        data6 = intent.getStringExtra("product_desc");
        data7 = intent.getStringExtra("name");
        data8 = intent.getStringExtra("email");
        data9 = intent.getStringExtra("phone");
        data10 = intent.getStringExtra("userid");
        data11 = intent.getStringExtra("address");
        data12 = intent.getStringExtra("city");
        data13 = intent.getStringExtra("lat");
        data14 = intent.getStringExtra("lng");
        data15 = intent.getStringExtra("expiry_date");

        product_id.setText(data1);
        primary_id.setText(data10);
        uname.setText(data7);
        uemail.setText(data8);
        uaddress.setText(data11);
        ucity.setText(data12);
        miligram.setText(data5);
        String item_price = "P "+data4;
        Price.setText(item_price);
        img_description.setText(data2);
        String expiry_date = "Expiry date: " + data15 ;
        expiry.setText(expiry_date);

        Picasso.with(this)
                .load(data3)
                .placeholder(R.drawable.loader)
                .into(image_view);

        saveinfo.setOnClickListener(this);
        choose_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == saveinfo){
            process();
        }
        if(v == choose_image){
            choose_image_process();
        }
    }

    public void process(){
        String md1 = primary_id.getText().toString();
        String md2 = product_id.getText().toString();
        String md9 = add_notes.getText().toString();
        String m10 = quantity.getText().toString();
        if(TextUtils.isEmpty(md9) || TextUtils.isEmpty(m10)){
            Toast.makeText(this, "Notes and quantity is must not be empty!.", Toast.LENGTH_SHORT).show();
        }
        else {
            confirm_order(md1,md2,md9,m10);
        }
    }
    public void choose_image_process(){
        ActivityCompat.requestPermissions(
                CheckOut.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                CODE_GALLERY_REQUEST);
    }
    public void confirm_order(final String d1, final String d2, final String d9, final String d10){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                startActivity(new Intent(CheckOut.this, MainActivity.class));
                                Toast.makeText(CheckOut.this, "Successfully submit!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(CheckOut.this, "Error Saving Account! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(CheckOut.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CheckOut.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("primary_id", d1);
                params.put("product_id", d2);
                params.put("notes", d9);
                params.put("quantity", d10);
                String imageData = imageToString(bitmap);
                params.put("image",imageData);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"),CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "You dont have permission to access gallery", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            if(filepath != null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap  = BitmapFactory.decodeStream(inputStream);
                prescriptionimg.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
