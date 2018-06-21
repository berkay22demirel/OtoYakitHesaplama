package com.berkay22demirel.otoyakthesaplama.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.berkay22demirel.otoyakthesaplama.Interface.OnButtonPressListener;
import com.berkay22demirel.otoyakthesaplama.Kayit;
import com.berkay22demirel.otoyakthesaplama.MainActivity;
import com.berkay22demirel.otoyakthesaplama.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BerkayDemirel on 08.02.2018.
 */

public class Hesapla extends Fragment {
    private DateFormat dateFormat;
    private Calendar calendar;
    private EditText editTextHesaplaUcret;
    private EditText editTextHesaplaYol;
    private EditText editTextHesaplaFiyat;
    private TextView textViewHesaplaBaslangicTarih;
    private TextView textViewHesaplaBitisTarih;
    private Button buttonHesapla;

    //Custom Dialog Elemanları
    private Button buttonCustomDialogKaydet;
    private Button buttonCustomDialogKapat;
    private TextView textViewCustomDialog100KmYakitSonuc;
    private TextView textViewCustomDialog1KmUcretSonuc;
    private TextView textViewCustomDialog1GunUcretSonuc;

    //Baslangic Bitiş Tarihleri
    Date baslangicDate;
    Date bitisDate;

    //Main Activity
    ViewPager viewPager;
    OnButtonPressListener onButtonPressListener;

    //Double Basamak Sayısı Belirleme
    private NumberFormat numberFormat = NumberFormat.getInstance();

    //Kaydedilecek Kayit
    Kayit kayitHesapla = new Kayit();

    DatePickerDialog.OnDateSetListener dateBaslangic = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            baslangicDate = new Date(calendar.getTime().getTime() - (calendar.getTime().getTime() % 86400000));
            textViewHesaplaBaslangicTarih.setText(dateFormat.format(baslangicDate.getTime()));
            kayitHesapla.setBaslangicDate(baslangicDate.toString());

        }
    };

    DatePickerDialog.OnDateSetListener dateBitis = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            bitisDate = new Date(calendar.getTime().getTime() - (calendar.getTime().getTime() % 86400000));
            textViewHesaplaBitisTarih.setText(dateFormat.format(bitisDate.getTime()));
            kayitHesapla.setBitisDate(bitisDate.toString());

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_hesapla,container,false);

        //Double sayıların maximum basamak ayarı
        numberFormat.setMaximumFractionDigits(1);

        dateFormat = DateFormat.getDateInstance();
        calendar = Calendar.getInstance();

        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPagerMain);
        onButtonPressListener = (OnButtonPressListener) getActivity();

        editTextHesaplaUcret = (EditText) view.findViewById(R.id.editTextHesaplaUcret);
        editTextHesaplaYol = (EditText) view.findViewById(R.id.editTextHesaplaYol);
        editTextHesaplaFiyat = (EditText) view.findViewById(R.id.editTextHesaplaFiyat);
        textViewHesaplaBaslangicTarih = (TextView) view.findViewById(R.id.textViewHesaplaBaslangicTarih);
        textViewHesaplaBitisTarih = (TextView) view.findViewById(R.id.textViewHesaplaBitisTarih);
        buttonHesapla = (Button) view.findViewById(R.id.buttonHesapla);

        textViewHesaplaBaslangicTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateBaslangic, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        textViewHesaplaBitisTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateBitis, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        buttonHesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyCustomDailog();
            }
        });

        return view;
    }

    //Eklenen değerlerin sonucunun ekranda gösterildiği dailog
    public void showMyCustomDailog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_hesapla_sonuc);

        buttonCustomDialogKaydet = (Button) dialog.findViewById(R.id.buttonCustomDialogKaydet);
        buttonCustomDialogKapat = (Button) dialog.findViewById(R.id.buttonCustomDialogKapat);
        textViewCustomDialog100KmYakitSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog100KmYakitSonuc);
        textViewCustomDialog1KmUcretSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog1KmUcretSonuc);
        textViewCustomDialog1GunUcretSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog1GunUcretSonuc);

        if(editTextHesaplaUcret.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Lütfen ödenen ücreti giriniz!!!",Toast.LENGTH_LONG).show();
            return;
        }
        if(editTextHesaplaYol.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Lütfen katedilen yolu giriniz!!!",Toast.LENGTH_LONG).show();
            return;
        }
        if(editTextHesaplaFiyat.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Lütfen yakıtın litre fiyatını giriniz!!!",Toast.LENGTH_LONG).show();
            return;
        }

        Double ucret = Double.parseDouble(editTextHesaplaUcret.getText().toString());
        Double yol = Double.parseDouble(editTextHesaplaYol.getText().toString());
        Double fiyat = Double.parseDouble(editTextHesaplaFiyat.getText().toString());

        if(baslangicDate == null){
            Toast.makeText(getContext(),"Lütfen başlangıç tarihini giriniz!!!",Toast.LENGTH_LONG).show();
            return;
        }
        if(bitisDate == null){
            Toast.makeText(getContext(),"Lütfen bitiş tarihini giriniz!!!",Toast.LENGTH_LONG).show();
            return;
        }

        Long gun = (bitisDate.getTime() - baslangicDate.getTime()) / 86400000 + 1;

        if(gun < 0){
            Toast.makeText(getContext(),"Lütfen bitiş tarihini başlangıç tarihinden ileri bir tarih olarak seçiniz!!!",Toast.LENGTH_LONG).show();
            return;
        }

        textViewCustomDialog100KmYakitSonuc.setText(numberFormat.format(((ucret/fiyat) / yol) * 100.0) + " Litre");
        if ((ucret/yol) < 1) {
            textViewCustomDialog1KmUcretSonuc.setText(String.valueOf((int) ( (ucret / yol) * 100 )) + " Kuruş");
        }
        else{
            textViewCustomDialog1KmUcretSonuc.setText(numberFormat.format( ucret / yol ) + " TL");
        }
        textViewCustomDialog1GunUcretSonuc.setText(numberFormat.format( ucret / gun ) + " TL");

        buttonCustomDialogKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        kayitHesapla.setUcret(ucret);
        kayitHesapla.setYol(yol);
        kayitHesapla.setFiyat(fiyat);
        kayitHesapla.setBirGunUcretSonuc( ucret / gun );
        kayitHesapla.setBirKmUcretSonuc( ucret / yol);
        kayitHesapla.setYuzKmYakitSonuc(((ucret/fiyat) / yol) * 100.0);

        buttonCustomDialogKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                viewPager.setCurrentItem(1);
                onButtonPressListener.onButtonPressed(kayitHesapla);
            }
        });

        dialog.show();
    }
}
