package com.antoniuswicaksana.project_pbp;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Event {
    public String nama;
    public int harga;
    public String kapasitas;
    public String imgURL;

    public Event(String nama, int harga, String kapasitas, String imgURL) {
        this.nama = nama;
        this.harga = harga;
        this.kapasitas = kapasitas;
        this.imgURL = imgURL;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getStringHarga() { return String.valueOf(harga); }

    public void setStringHarga(String harga) {
        if(!harga.isEmpty()) {
            this.harga = Integer.parseInt(harga);
        }else{
            this.harga = 0;
        }
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView, String imgURL) {
        Glide.with(imageView)
                .load(imgURL)
//                .apply(new RequestOptions().override(600, 200))
                .into(imageView);
    }
}
