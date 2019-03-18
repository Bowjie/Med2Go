package com.example.medtogo.UserBlade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    String value;

    SessionManager sessionManager;

    public MyAdapter(List<ListItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
        final ListItem listItem = listItems.get(i);

        viewHolder.pk.setText(listItem.getPrimary_id());
        String itemprice = "P "+listItem.getPrice();
        viewHolder.price.setText(itemprice);
        viewHolder.imgdesc.setText(listItem.getImg_desc());
        viewHolder.miligram.setText(listItem.getMiligram());
        String exp_date = "Expiry date: "+listItem.getExpiry();
        viewHolder.expiry.setText(exp_date);

        Picasso.with(context)
                .load(listItem.getImg_url())
                .placeholder(R.drawable.loader)
                .into(viewHolder.img);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Opening "+ listItem.getPrimary_id(), Toast.LENGTH_SHORT).show();
                value = listItem.getPrimary_id();
                gamotdetails();
            }
        });
    }
    private void gamotdetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://kdtravelandtours.com/grabmed/gamotfulldetails?primary_id="+value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("gemotdetails");

                            for(int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String pk = object.getString("data1");
                                String imgdesc = object.getString("data2");
                                String imgurl = object.getString("data3");
                                String price = object.getString("data4");
                                String mg = object.getString("data5");
                                String product_desc = object.getString("data6");
                                String expiry_date = object.getString("data7");

                                Intent intent = new Intent(context, gamotfulldetails.class);

                                intent.putExtra("pk", pk);
                                intent.putExtra("imgdesc", imgdesc);
                                intent.putExtra("imgurl", imgurl);
                                intent.putExtra("price", price);
                                intent.putExtra("mg", mg);
                                intent.putExtra("product_desc",product_desc);
                                intent.putExtra("expiry_date", expiry_date);

                                context.startActivity(intent);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView img;
        TextView imgdesc,price, pk, miligram, expiry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.image_view);
            imgdesc = itemView.findViewById(R.id.img_description);
            price = itemView.findViewById(R.id.Price);
            pk = itemView.findViewById(R.id.primary_id);
            miligram = itemView.findViewById(R.id.miligram);
            cardView = itemView.findViewById(R.id.cardView);
            expiry = itemView.findViewById(R.id.expiry);

            sessionManager = new SessionManager(context);
            sessionManager.checkLogin();
        }
    }
}
