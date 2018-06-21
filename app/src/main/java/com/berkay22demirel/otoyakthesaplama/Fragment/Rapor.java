package com.berkay22demirel.otoyakthesaplama.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.berkay22demirel.otoyakthesaplama.Adapter.RaporListAdapter;
import com.berkay22demirel.otoyakthesaplama.Database.DatabaseManager;
import com.berkay22demirel.otoyakthesaplama.Kayit;
import com.berkay22demirel.otoyakthesaplama.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BerkayDemirel on 08.02.2018.
 */

public class Rapor extends Fragment {

    private DateFormat dateFormat;
    private Calendar calendar;
    Calendar calendarYardimci;

    TextView textViewBaslangicTarihi;
    TextView textViewBitisTarihi;
    TextView textViewPlakalar;

    TextView textViewToplamKatedilenYol;
    TextView textViewToplamOdenenUcret;
    TextView textViewToplamTuketilenYakit;
    TextView textViewGundeOdenenOrtalamaUcret;
    TextView textViewBirKmYeOdenenOrtalamaUcret;

    RaporListAdapter raporListAdapter;
    ArrayList<String> plakalar;

    DatabaseManager databaseManager;

    //Baslangic Bitiş Tarihleri
    Date baslangicDate = null;
    Date bitisDate = null;

    //Main Activity
    ViewPager viewPager;

    //Custom Dialog Sonuc
    Dialog dialogSonuc;
    ListView listViewCustomDialogPlakalar;

    //Seçilen Plaka
    String secilenPlaka = "Tümü";

    //Double Basamak Sayısı Belirleme
    private NumberFormat numberFormat = NumberFormat.getInstance();

    DatePickerDialog.OnDateSetListener dateBaslangic = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            baslangicDate = new Date(calendar.getTime().getTime() - (calendar.getTime().getTime() % 86400000));
            textViewBaslangicTarihi.setText(dateFormat.format(baslangicDate.getTime()));
            if(tarihKontrol() == 1){
                if(bilgiKontrol()){
                    bilgiDondur();
                }
            }
            else if(tarihKontrol() == 0){
                Toast.makeText(getContext(),"Lütfen bitiş tarihini başlangıç tarihinden ileri bir tarih olarak seçiniz!!!",Toast.LENGTH_LONG).show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dateBitis = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            bitisDate = new Date(calendar.getTime().getTime() - (calendar.getTime().getTime() % 86400000));
            textViewBitisTarihi.setText(dateFormat.format(bitisDate.getTime()));
            if(tarihKontrol() == 1){
                if(bilgiKontrol()){
                    bilgiDondur();
                }
            }
            else if(tarihKontrol() == 0){
                Toast.makeText(getContext(),"Lütfen bitiş tarihini başlangıç tarihinden ileri bir tarih olarak seçiniz!!!",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_rapor,container,false);

        numberFormat.setMaximumFractionDigits(1);

        dateFormat = DateFormat.getDateInstance();
        calendar = Calendar.getInstance();
        calendarYardimci = Calendar.getInstance();

        textViewBaslangicTarihi = (TextView) view.findViewById(R.id.textViewRaporBaslangicTarih);
        textViewBitisTarihi = (TextView) view.findViewById(R.id.textViewRaporBitisTarih);
        textViewPlakalar = (TextView) view.findViewById(R.id.textViewRaporPlakalar);

        textViewToplamKatedilenYol = (TextView) view.findViewById(R.id.textViewRaporToplamKatedilenYolSonuc);
        textViewToplamOdenenUcret = (TextView) view.findViewById(R.id.textViewRaporToplamOdenenUcretSonuc);
        textViewToplamTuketilenYakit = (TextView) view.findViewById(R.id.textViewRaporToplamTüketilenYakitMiktariSonuc);
        textViewGundeOdenenOrtalamaUcret = (TextView) view.findViewById(R.id.textViewRaporGundeOdenenOrtalamaUcretSonuc);
        textViewBirKmYeOdenenOrtalamaUcret = (TextView) view.findViewById(R.id.textViewRaporOrtalamaBirKmYeOdenenUcretSonuc);

        dialogSonuc = new Dialog(getContext());
        dialogSonuc.setContentView(R.layout.custom_dialog_rapor_plakalar);
        listViewCustomDialogPlakalar = (ListView) dialogSonuc.findViewById(R.id.listViewCustomDialogRapor);

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerMain);

        databaseManager = new DatabaseManager(getContext());

        boolean sonuc = tarihGetir();
        if(sonuc){
            bilgiDondur();
        }else{
            bilgiTemizle();
        }


        textViewBaslangicTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baslangicDate != null){
                    calendarYardimci.setTime(baslangicDate);
                }
                else{
                    calendarYardimci = calendar;
                }
                new DatePickerDialog(getContext(), dateBaslangic, calendarYardimci.get(Calendar.YEAR),calendarYardimci.get(Calendar.MONTH),
                        calendarYardimci.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        textViewBitisTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitisDate != null){
                    calendarYardimci.setTime(bitisDate);
                }
                else{
                    calendarYardimci = calendar;
                }
                new DatePickerDialog(getContext(), dateBitis, calendarYardimci.get(Calendar.YEAR),calendarYardimci.get(Calendar.MONTH),
                        calendarYardimci.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        textViewPlakalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plakalar = databaseManager.plakalariGetir();
                if(plakalar.size() == 0){
                    viewPager.setCurrentItem(1);
                    Toast.makeText(getContext(),"Lütfen öncelikle bir plaka ekleyiniz!",Toast.LENGTH_LONG).show();
                }
                else {
                    showMyCustomDailog(plakalar);
                }
            }
        });

        listViewCustomDialogPlakalar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textViewPlakalar.setText(plakalar.get(position));
                secilenPlaka = plakalar.get(position);
                boolean sonuc = tarihGetir();
                if(sonuc){
                    bilgiDondur();
                }else{
                    bilgiTemizle();
                }
                dialogSonuc.dismiss();
            }
        });


        return view;
    }

    //Plaka seçme dailog'unun oluşturulması
    public void showMyCustomDailog(ArrayList<String> plakalar){
        plakalar.add(0,"Tümü");

        raporListAdapter = new RaporListAdapter(getContext(),R.layout.list_item_rapor_plakalar,plakalar);

        listViewCustomDialogPlakalar.setAdapter(raporListAdapter);

        dialogSonuc.show();
    }

    //Seçilen plaka için başlangıç ve bitiş tarihlerinin getirilip ekrana girilmesi
    public boolean tarihGetir(){
        ArrayList<Date> baslangicBitisTarihi = databaseManager.baslangicBitisTarihGetir(secilenPlaka);
        if(baslangicBitisTarihi.get(0) != null){
            baslangicDate = baslangicBitisTarihi.get(0);
            bitisDate = baslangicBitisTarihi.get(1);

            textViewBaslangicTarihi.setText(dateFormat.format(baslangicDate));
            textViewBitisTarihi.setText(dateFormat.format(bitisDate));

            return true;
        }
        else {
            textViewBaslangicTarihi.setText("Başlangıç");
            textViewBitisTarihi.setText("Bitiş");
            return false;
        }
    }

    //Seçilen başlangıç tarihinin bitiş tarihinden önceki bir tarih olmasının kontrolü
    public int tarihKontrol(){
        if(baslangicDate == null || bitisDate == null){
            return -1;
        }
        Long gun = (bitisDate.getTime() - baslangicDate.getTime()) / 86400000 + 1;

        if(gun < 1){
            return 0;
        }else {
            return 1;
        }
    }

    //Seçilen plakanın verilerinin gösterilmesi
    public void bilgiDondur(){

        Long gun = (bitisDate.getTime() - baslangicDate.getTime()) / 86400000 + 1;

        Kayit kayit = databaseManager.raporGetir(textViewPlakalar.getText().toString(),baslangicDate,bitisDate);
        if(kayit.getYol() == 0.0 && kayit.getFiyat() == 0.0 && kayit.getUcret() == 0.0){
            bilgiTemizle();
        }
        else {
            textViewToplamKatedilenYol.setText(numberFormat.format(kayit.getYol()).toString() + " KM");
            if(kayit.getUcret() < 1){
                textViewToplamOdenenUcret.setText(String.valueOf((int) ( kayit.getUcret() * 100 )) + " Kuruş");
            }
            else{
                textViewToplamOdenenUcret.setText(numberFormat.format(kayit.getUcret()).toString() + " TL");
            }
            textViewToplamTuketilenYakit.setText(String.valueOf(numberFormat.format(kayit.getFiyat())) + " Litre");
            if((kayit.getUcret() / gun) < 1){
                textViewGundeOdenenOrtalamaUcret.setText(String.valueOf((int) ( (kayit.getUcret() / gun) * 100 )) + " Kuruş");
            }
            else{
                textViewGundeOdenenOrtalamaUcret.setText(String.valueOf(numberFormat.format(kayit.getUcret() / gun)) + " TL");
            }
            if((kayit.getUcret() / kayit.getYol()) < 1){
                textViewBirKmYeOdenenOrtalamaUcret.setText(String.valueOf((int) ( (kayit.getUcret() / kayit.getYol()) * 100 )) + " Kuruş");
            }
            else{
                textViewBirKmYeOdenenOrtalamaUcret.setText(String.valueOf(numberFormat.format(kayit.getUcret() / kayit.getYol())) + " TL");
            }
        }
    }

    //Eğer seçilen plakada veri yoksa gösterilen bilgilerin 0 olarak ayarlanması
    public void bilgiTemizle(){
        textViewToplamKatedilenYol.setText("0 KM");
        textViewToplamOdenenUcret.setText("0 TL");
        textViewToplamTuketilenYakit.setText("0 Litre");
        textViewGundeOdenenOrtalamaUcret.setText("0 TL");
        textViewBirKmYeOdenenOrtalamaUcret.setText("0 TL");
    }

    //Tarih seçimi yapıldığında eğer plakada veri yoksa bilgiDoldur fonksiyonunun çağırılmasını önlemek için oluşturulmuş fonksiyon
    public boolean bilgiKontrol(){
        ArrayList<Date> baslangicBitisTarihi = databaseManager.baslangicBitisTarihGetir(secilenPlaka);
        if(baslangicBitisTarihi.get(0) != null){
            return true;
        }
        else {
            return false;
        }
    }

}
