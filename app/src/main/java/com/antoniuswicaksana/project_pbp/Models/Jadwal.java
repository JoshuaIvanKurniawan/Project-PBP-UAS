package com.antoniuswicaksana.project_pbp.Models;

import java.io.Serializable;

public class Jadwal implements Serializable {

    private String id, tanggal, waktu, keterangan, gambar;

    public Jadwal(String id, String tanggal, String waktu, String keterangan, String gambar){
        this.id = id;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.keterangan = keterangan;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public String getTanggal() { return tanggal; }

    public String getWaktu() {
        return waktu;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getGambar() {
        return gambar;
    }

}

