package com.berkay22demirel.otoyakthesaplama;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by BerkayDemirel on 14.02.2018.
 */

public class Kayit {
    private Double ucret;
    private Double yol;
    private Double fiyat;
    private String baslangicDate;
    private String bitisDate;
    private Double yuzKmYakitSonuc;
    private Double birKmUcretSonuc;
    private Double birGunUcretSonuc;
    private String plaka;

    public Kayit(Double ucret,Double yol, Double fiyat, String baslangicDate, String bitisDate, Double birGunUcretSonuc, Double birKmUcretSonuc, Double yuzKmYakitSonuc) {
        this.ucret = ucret;
        this.birGunUcretSonuc = birGunUcretSonuc;
        this.birKmUcretSonuc = birKmUcretSonuc;
        this.yuzKmYakitSonuc = yuzKmYakitSonuc;
        this.bitisDate = bitisDate;
        this.baslangicDate = baslangicDate;
        this.fiyat = fiyat;
        this.yol = yol;
    }

    public Kayit(Double ucret, Double yol, Double fiyat, String baslangicDate, String bitisDate, Double yuzKmYakitSonuc, Double birKmUcretSonuc, Double birGunUcretSonuc, String plaka) {
        this.ucret = ucret;
        this.yol = yol;
        this.fiyat = fiyat;
        this.baslangicDate = baslangicDate;
        this.bitisDate = bitisDate;
        this.yuzKmYakitSonuc = yuzKmYakitSonuc;
        this.birKmUcretSonuc = birKmUcretSonuc;
        this.birGunUcretSonuc = birGunUcretSonuc;
        this.plaka = plaka;
    }

    public Kayit() {
    }

    public Double getUcret() {
        return ucret;
    }

    public void setUcret(Double ucret) {
        this.ucret = ucret;
    }

    public Double getYol() {
        return yol;
    }

    public void setYol(Double yol) {
        this.yol = yol;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public void setFiyat(Double fiyat) {
        this.fiyat = fiyat;
    }

    public String getBaslangicDate() {
        return baslangicDate;
    }

    public void setBaslangicDate(String baslangicDate) {
        this.baslangicDate = baslangicDate;
    }

    public String getBitisDate() {
        return bitisDate;
    }

    public void setBitisDate(String bitisDate) {
        this.bitisDate = bitisDate;
    }

    public Double getYuzKmYakitSonuc() {
        return yuzKmYakitSonuc;
    }

    public void setYuzKmYakitSonuc(Double yuzKmYakitSonuc) {
        this.yuzKmYakitSonuc = yuzKmYakitSonuc;
    }

    public Double getBirKmUcretSonuc() {
        return birKmUcretSonuc;
    }

    public void setBirKmUcretSonuc(Double birKmUcretSonuc) {
        this.birKmUcretSonuc = birKmUcretSonuc;
    }

    public Double getBirGunUcretSonuc() {
        return birGunUcretSonuc;
    }

    public void setBirGunUcretSonuc(Double birGunUcretSonuc) {
        this.birGunUcretSonuc = birGunUcretSonuc;
    }

    public String getPlaka() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka = plaka;
    }

}
