package com.berkay22demirel.otoyakthesaplama.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.berkay22demirel.otoyakthesaplama.Kayit;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BerkayDemirel on 14.02.2018.
 */

public class DatabaseManager {

    public static final int BASARILI = 1;
    public static final int BILINMEYEN_HATA = -1;
    public static final int PROFIL_VALIDASYON_HATASI = -2;
    private DatabaseHelper helper;
    private Context context;
    private DateFormat dateFormat = DateFormat.getDateInstance();

    public DatabaseManager(Context context){
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    public ArrayList<String> plakalariGetir(){
        ArrayList<String> plakalar = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Plaka.FULL_PROJECTION_PLAKA,null,null,null,null,null);
        if(cursor == null){
            return null;
        }
        if(cursor.moveToFirst()){
            do{
                int plakaIndex = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Plaka.COLUMN_PLAKA);
                plakalar.add(cursor.getString(plakaIndex));
            }while (cursor.moveToNext());
        }
        db.close();
        return plakalar;
    }

    public HashMap<String, List<String>> kayitlariGetir(ArrayList<String> plakalar){
        HashMap<String, List<String>> kayitlar = new HashMap<>();

        Date baslangicTarihiDate;
        Date bitisTarihiDate;

        SQLiteDatabase db = helper.getReadableDatabase();
        String where = OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + "= ?";
        String [] whereArgs = null;
        for(int i=0;i<plakalar.size();i++){
            whereArgs = new String[] {plakalar.get(i)};
            List<String> plakaKayit = new ArrayList<>();
            Cursor cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,where,whereArgs,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    int baslangicTarihiIndex = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH);
                    int bitisTarihiIndex = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH);
                    baslangicTarihiDate = new Date(cursor.getString(baslangicTarihiIndex));
                    bitisTarihiDate = new Date(cursor.getString(bitisTarihiIndex));
                    plakaKayit.add(dateFormat.format(baslangicTarihiDate)+ " - " + dateFormat.format(bitisTarihiDate));
                }while (cursor.moveToNext());
            }
            kayitlar.put(plakalar.get(i),plakaKayit);
        }
        db.close();
        return kayitlar;
    }

    public int plakaEkle(String plaka){
        if(plakaSorgula(plaka) == 1){
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Plaka.COLUMN_PLAKA,plaka);
        SQLiteDatabase db = helper.getWritableDatabase();
        Long profilId = db.insert(OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME,null,contentValues);
        db.close();
        if(profilId == -1){
            return BILINMEYEN_HATA;
        }
        return BASARILI;
    }

    public int kayitEkle(Kayit kayit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_UCRET,kayit.getUcret());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YOL,kayit.getYol());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_FIYAT,kayit.getFiyat());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH,kayit.getBaslangicDate());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH,kayit.getBitisDate());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YUZ_KM_YAKIT_SONUC,kayit.getYuzKmYakitSonuc());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_KM_UCRET_SONUC,kayit.getBirKmUcretSonuc());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_GUN_UCRET_SONUC,kayit.getBirGunUcretSonuc());
        contentValues.put(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA,kayit.getPlaka());

        SQLiteDatabase db = helper.getWritableDatabase();
        Long profilId = db.insert(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,null,contentValues);
        db.close();
        if(profilId == -1){
            return BILINMEYEN_HATA;
        }
        return BASARILI;
    }

    public int plakaSil(String plaka){
        int sonuc = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        int resultSecond = db.delete(OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Plaka.COLUMN_PLAKA + " = ?",new String[]{plaka});
        if(resultSecond == 1){
            int resultFirst = db.delete(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + " = ?",new String[]{plaka});
            if(resultFirst >= 1){
                sonuc = 1;
            }else {
                sonuc = -1;
            }
        }
        db.close();
        return sonuc;
    }

    public int kayitSil(int groupPosition, int childPosition, ArrayList<String> plakalar){
        int kayitId = kayitIdSorgula(groupPosition,childPosition,plakalar);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = OtoYakitHesaplamaDatabaseContract.Kayit._ID + "= ?";
        String [] whereArgs = {String.valueOf(kayitId)};
        int result = db.delete(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,where,whereArgs);
        db.close();
        return result;
    }

    public Kayit kayitGetir(int groupPosition,int childPosition,ArrayList<String> plakalar){
        int kayitId = kayitIdSorgula(groupPosition,childPosition,plakalar);
        Kayit kayit = new Kayit();
        SQLiteDatabase db = helper.getReadableDatabase();
        String where = OtoYakitHesaplamaDatabaseContract.Kayit._ID + "= ?";
        String [] whereArgs = {String.valueOf(kayitId)};

        Cursor cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        int fiyat = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_FIYAT);
        int yol = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YOL);
        int ucret = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_UCRET);
        int yuzkm = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YUZ_KM_YAKIT_SONUC);
        int birkm = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_KM_UCRET_SONUC);
        int birgun = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BIR_GUN_UCRET_SONUC);
        int baslangicTarih = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH);
        int bitisTarih = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH);
        int plaka = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA);

        kayit.setFiyat(cursor.getDouble(fiyat));
        kayit.setYol(cursor.getDouble(yol));
        kayit.setUcret(cursor.getDouble(ucret));
        kayit.setYuzKmYakitSonuc(cursor.getDouble(yuzkm));
        kayit.setBirKmUcretSonuc(cursor.getDouble(birkm));
        kayit.setBirGunUcretSonuc(cursor.getDouble(birgun));
        kayit.setBaslangicDate(cursor.getString(baslangicTarih));
        kayit.setBitisDate(cursor.getString(bitisTarih));
        kayit.setPlaka(cursor.getString(plaka));

        db.close();

        return kayit;
    }

    public int kayitIdSorgula(int groupPosition, int childPosition,ArrayList<String> plakalar){
        HashMap<String, List<Integer>> kayitlar = new HashMap<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        String where = OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + "= ?";
        String [] whereArgs = null;
        for(int i=0;i<plakalar.size();i++){
            whereArgs = new String[] {plakalar.get(i)};
            List<Integer> plakaKayit = new ArrayList<>();
            Cursor cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,where,whereArgs,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    int id = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit._ID);
                    plakaKayit.add(cursor.getInt(id));
                }while (cursor.moveToNext());
            }
            kayitlar.put(plakalar.get(i),plakaKayit);
        }
        db.close();
        return kayitlar.get(plakalar.get(groupPosition)).get(childPosition);
    }

    public int plakaSorgula(String plaka){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(OtoYakitHesaplamaDatabaseContract.SECOND_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Plaka.FULL_PROJECTION_PLAKA,OtoYakitHesaplamaDatabaseContract.Plaka.COLUMN_PLAKA + " = ?",new String[]{plaka},null,null,null);
        if(cursor == null){
            return 0;
        }
        else if(cursor.getCount() <= 0){
            return 0;
        }
        else{
            return 1;
        }
    }

    public ArrayList<Date> baslangicBitisTarihGetir(String plaka){
        ArrayList<Date> baslangicBitisTarih = new ArrayList<>(2);
        baslangicBitisTarih.add(null);
        baslangicBitisTarih.add(null);

        Date baslangicTarihiDate;
        Date bitisTarihiDate;

        SQLiteDatabase db = helper.getReadableDatabase();
        String where = OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + "= ?";

        Cursor cursor;

        if(plaka == "Tümü"){
            cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,null,null,null,null,null);
        }
        else {
            String [] whereArgs = null;
            whereArgs = new String[] {plaka};
            cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,where,whereArgs,null,null,null);
        }

        if(cursor.moveToFirst()){
            do{
                int baslangicId = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH);
                int bitisId = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH);

                String baslangicTarihi = cursor.getString(baslangicId);
                String bitisTarihi = cursor.getString(bitisId);

                baslangicTarihiDate = new Date(baslangicTarihi);
                bitisTarihiDate = new Date(bitisTarihi);

                if(baslangicBitisTarih.get(0) == null){
                    baslangicBitisTarih.set(0,baslangicTarihiDate);
                    baslangicBitisTarih.set(1,bitisTarihiDate);

                }
                else{
                    if(baslangicBitisTarih.get(0).getTime() > baslangicTarihiDate.getTime()){
                        baslangicBitisTarih.set(0,baslangicTarihiDate);
                    }

                    if(baslangicBitisTarih.get(1).getTime() < bitisTarihiDate.getTime()){
                        baslangicBitisTarih.set(1,bitisTarihiDate);
                    }

                }
            }while (cursor.moveToNext());
        }

        db.close();

        return baslangicBitisTarih;
    }

    public Kayit raporGetir(String gelenPlaka,Date gelenBaslangicTarihiDate,Date gelenBitisTarihiDate){

        Kayit kayit = new Kayit();
        kayit.setYol(0.0);
        kayit.setFiyat(0.0);
        kayit.setUcret(0.0);
        kayit.setBaslangicDate(gelenBaslangicTarihiDate.toString());
        kayit.setBitisDate(gelenBitisTarihiDate.toString());
        kayit.setPlaka(gelenPlaka);

        Date kontrolBaslangicTarihiDate;
        Date kontrolBitisTarihiDate;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;

        if(gelenPlaka.matches("Tümü")){
            cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,null,null,null,null,null);

        }
        else {
            String where = OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_PLAKA + "= ?";
            String [] whereArgs = new String[] {gelenPlaka};
            cursor = db.query(OtoYakitHesaplamaDatabaseContract.FIRST_TABLE_NAME,OtoYakitHesaplamaDatabaseContract.Kayit.FULL_PROJECTION_KAYIT,where,whereArgs,null,null,null);
        }

        //kayitKontrol = 0 ise kayıt eklenmeyecek, 1 ise başlangıç tarihi istediğimiz bitiş tarihi sınırı aşıyor,
        //2 ise bitiş tarihi istediğimiz başlangıç tarihi sınırı aşıyor, 3 ise başlangıç ve bitiş istediğimiz sınırların içerisinde
        int kayitKontrol;

        if(cursor == null){
            return kayit;
        }
        if(cursor.moveToFirst()){
            do{
                int baslangicTarih = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BASLANGIC_TARIH);
                int bitisTarih = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_BITIS_TARIH);
                int fiyat = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_FIYAT);
                int yol = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_YOL);
                int ucret = cursor.getColumnIndex(OtoYakitHesaplamaDatabaseContract.Kayit.COLUMN_UCRET);

                kontrolBaslangicTarihiDate = new Date(cursor.getString(baslangicTarih));
                kontrolBitisTarihiDate = new Date(cursor.getString(bitisTarih));

                if(gelenBaslangicTarihiDate.getTime() > kontrolBitisTarihiDate.getTime() || gelenBitisTarihiDate.getTime() < kontrolBaslangicTarihiDate.getTime()){

                }
                else {
                    long basGun = 0;
                    long bitisGun = 0;

                    if(gelenBaslangicTarihiDate.getTime() > kontrolBaslangicTarihiDate.getTime()){
                        basGun = (gelenBaslangicTarihiDate.getTime() - kontrolBaslangicTarihiDate.getTime()) / 86400000;
                    }
                    if(gelenBitisTarihiDate.getTime() < kontrolBitisTarihiDate.getTime()){
                        bitisGun = (kontrolBitisTarihiDate.getTime() - gelenBitisTarihiDate.getTime()) / 86400000;
                    }

                    if(basGun == 0 && bitisGun == 0){
                        kayit.setFiyat(  ( cursor.getDouble(ucret)/cursor.getDouble(fiyat) ) + kayit.getFiyat());
                        kayit.setYol(cursor.getDouble(yol) + kayit.getYol());
                        kayit.setUcret(cursor.getDouble(ucret) + kayit.getUcret());
                    }
                    else {
                        long gelenKayitGun = basGun + bitisGun;
                        long olanKayitGun = (kontrolBitisTarihiDate.getTime() - kontrolBaslangicTarihiDate.getTime()) / 86400000 + 1;
                        long girilecekKayitGun = olanKayitGun - gelenKayitGun;
                        kayit.setFiyat( ((( cursor.getDouble(ucret)/cursor.getDouble(fiyat) ) / olanKayitGun) * girilecekKayitGun)  + kayit.getFiyat());
                        kayit.setYol((  (cursor.getDouble(yol) / olanKayitGun) * girilecekKayitGun  ) + kayit.getYol());
                        kayit.setUcret((  (cursor.getDouble(ucret) / olanKayitGun) * girilecekKayitGun  ) + kayit.getUcret());
                    }
                }


                //Fiyat değişkenine toplam harcanan yakıt miktarını giriyorum çünkü fiyat bilgisini alarak bunu hesaplamam mümkün değil
                //Ayrıca fiyat bilgisini rapor kısmında kullanmıyorum bu yüzden bunun yerine yazdım
            }while (cursor.moveToNext());
        }

        db.close();
        return kayit;
    }


}
