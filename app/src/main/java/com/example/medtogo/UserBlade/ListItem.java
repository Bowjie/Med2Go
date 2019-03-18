package com.example.medtogo.UserBlade;

public class ListItem {
    private String primary_id, img_desc, img_url, price, miligram, expiry;

    public ListItem(String primary_id, String img_desc, String img_url, String price, String miligram, String expiry)
    {
        this.primary_id = primary_id;
        this.img_desc = img_desc;
        this.img_url = img_url;
        this.price = price;
        this.miligram = miligram;
        this.expiry = expiry;
    }

    public String getPrimary_id(){ return  primary_id; }
    public String getImg_desc(){ return img_desc; }
    public String getImg_url(){ return img_url; }
    public String getPrice(){ return price; }
    public String getMiligram(){ return miligram; }
    public String getExpiry(){ return  expiry; }

}