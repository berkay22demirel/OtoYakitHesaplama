package com.berkay22demirel.otoyakthesaplama.Database;

import android.provider.BaseColumns;

/**
 * Created by BerkayDemirel on 14.02.2018.
 */

public class OtoYakitHesaplamaDatabaseContract {
    public static final String DATABASE_NAME = "otoYakitHesaplama";
    public static final String FIRST_TABLE_NAME = "plakalar";
    public static final String SECOND_TABLE_NAME = "kayitlar";
    public static final int DATABASE_VERSION = 1;
    public static class Kayit implements BaseColumns {
        private Kayit(){}
        public static final String COLUMN_UCRET = "ucret";
        public static final String COLUMN_YOL = "yol";
        public static final String COLUMN_FIYAT = "fiyat";
        public static final String COLUMN_BASLANGIC_TARIH = "baslangic_tarih";
        public static final String COLUMN_BITIS_TARIH = "bitis_tarih";
        public static final String COLUMN_YUZ_KM_YAKIT_SONUC = "yuz_km_yakit_sonuc";
        public static final String COLUMN_BIR_KM_UCRET_SONUC = "bir_km_ucret_sonuc";
        public static final String COLUMN_BIR_GUN_UCRET_SONUC = "bir_gun_ucret_sonuc";
        public static final String COLUMN_PLAKA = "plaka";
        public static final String[] FULL_PROJECTION_KAYIT = new String[] {_ID,COLUMN_UCRET,COLUMN_YOL,COLUMN_FIYAT,COLUMN_BASLANGIC_TARIH,COLUMN_BITIS_TARIH,COLUMN_YUZ_KM_YAKIT_SONUC,COLUMN_BIR_KM_UCRET_SONUC,COLUMN_BIR_GUN_UCRET_SONUC,COLUMN_PLAKA};
    }

    public static class Plaka implements BaseColumns{
        private Plaka(){}
        public static final String COLUMN_PLAKA = "plaka";
        public static final String[] FULL_PROJECTION_PLAKA = new String[] {_ID,COLUMN_PLAKA};
    }
}
