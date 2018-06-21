package com.berkay22demirel.otoyakthesaplama.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.berkay22demirel.otoyakthesaplama.Adapter.KayitlarimListAdapter;
import com.berkay22demirel.otoyakthesaplama.Database.DatabaseManager;
import com.berkay22demirel.otoyakthesaplama.Database.OtoYakitHesaplamaDatabaseContract;
import com.berkay22demirel.otoyakthesaplama.Kayit;
import com.berkay22demirel.otoyakthesaplama.MainActivity;
import com.berkay22demirel.otoyakthesaplama.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BerkayDemirel on 08.02.2018.
 */

public class Kayitlarim extends Fragment {

    private ExpandableListView listViewKayitlarim;
    private Button buttonPlakaEkle;
    private Button buttonKayitIptal;
    private EditText editTextPlaka;

    private DatabaseManager databaseManager;

    //List Elemanları
    private ArrayList<String> plakalarHeader;
    private HashMap<String, List<String>> kayitlarChild;
    private KayitlarimListAdapter kayitlarimListAdapter;
    private int last_position = -1;

    //İlk eklemede eklenen plaka
    private String ilkEklemePlaka;

    private Kayit kayit;

    //Double Basamak Sayısı Belirleme
    private NumberFormat numberFormat = NumberFormat.getInstance();

    // 0 = Normal Mod ----- 1 = Kayit Modu
    //Normal modda kayitlar gösterilir Kayit modta kullanıcı kaydı ekleyeceği plakayı seçer
    private static int mod = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_kayitlarim,container,false);

        //Double sayıların basamak ayarı
        numberFormat.setMaximumFractionDigits(1);

        listViewKayitlarim = (ExpandableListView) view.findViewById(R.id.listViewKayitlarim);
        buttonPlakaEkle = (Button) view.findViewById(R.id.buttonKayitlarimPlakaEkle);
        buttonKayitIptal = (Button) view.findViewById(R.id.buttonKayitlarimIptal);
        editTextPlaka = (EditText) view.findViewById(R.id.editTextKayitlarimPlaka);

        databaseManager = new DatabaseManager(getActivity());

        plakalarHeader = databaseManager.plakalariGetir();
        kayitlarChild = databaseManager.kayitlariGetir(plakalarHeader);

        kayitlarimListAdapter = new KayitlarimListAdapter(getContext(),plakalarHeader,kayitlarChild);
        listViewKayitlarim.setAdapter(kayitlarimListAdapter);
        listViewKayitlarim.setClickable(true);

        //------------------Kayıt İptal Butonu gösterilip gizlenmesi------------------------
        kayitIptalButonGorunurluguGir();
        //----------------------------------------------------------------------------------

        //------------------------------List içerisinde header listener-------------------------------------------
        listViewKayitlarim.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if(last_position != -1 && last_position != groupPosition)
                {
                    listViewKayitlarim.collapseGroup(last_position);
                }
                last_position = groupPosition;

                if(mod == 1){
                    kayitEkle(plakalarHeader.get(groupPosition));
                }
            }
        });
        //----------------------------------------------------------------------------------

        //------------------------------List içerisinde child listener-------------------------------------------
        listViewKayitlarim.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition,int childPosition, long id) {
                Kayit tiklananKayit = databaseManager.kayitGetir(groupPosition,childPosition,plakalarHeader);
                showChildCustomDailog(tiklananKayit,groupPosition,childPosition);
                return false;
            }
        });
        //----------------------------------------------------------------------------------

        //------------------------------List içerisinde Long Click listener----------------------------------------
        listViewKayitlarim.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                //If bloğunun içerisine girerse child girmesse header seçilmiştir
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    final int childPosition = ExpandableListView.getPackedPositionChild(id);
                    AlertDialog.Builder kayitSilDialog = new AlertDialog.Builder(getContext(),R.style.MyAlertDialog);
                    kayitSilDialog.setTitle("Seçilen kaydı silmek istediğinize emin misiniz?");
                    kayitSilDialog.setPositiveButton("Sil", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int arg1){
                            kayitSil(groupPosition,childPosition);
                        }
                    });
                    kayitSilDialog.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int arg1){

                        }
                    });
                    kayitSilDialog.show();
                    return true;
                }
                AlertDialog.Builder plakaSilDialog = new AlertDialog.Builder(getContext(),R.style.MyAlertDialog);
                plakaSilDialog.setTitle("Seçilen plakayı silmek istediğinize emin misiniz?");
                plakaSilDialog.setPositiveButton("Sil", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int arg1){
                        int sonuc = databaseManager.plakaSil(plakalarHeader.get(position));
                        plakalarHeader = databaseManager.plakalariGetir();
                        kayitlarChild = databaseManager.kayitlariGetir(plakalarHeader);
                        kayitlarimListAdapter.setPlakalarHeader(plakalarHeader);
                        kayitlarimListAdapter.setKayitlarChild(kayitlarChild);
                        kayitlarimListAdapter.notifyDataSetChanged();
                        if(sonuc == 1){
                            Toast.makeText(getContext(),"Plaka başarıyla silinmiştir...",Toast.LENGTH_LONG).show();
                        }
                        else if(sonuc == 0){
                            Toast.makeText(getContext(),"Plaka silinirken bir sorun oluştu!!!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(),"Beklenmedik bir durum oluştu!!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                plakaSilDialog.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int arg1){

                    }
                });
                plakaSilDialog.show();
                return false;
            }
        });
        //----------------------------------------------------------------------------------

        //------------------Kayıt İptal Buton listener--------------------------------------
        buttonKayitIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod = 0;
                kayitIptalButonGorunurluguGir();
                Toast.makeText(getContext(),"Kayıt ekleme işlemi iptal edilmiştir!!!",Toast.LENGTH_LONG).show();
            }
        });
        //----------------------------------------------------------------------------------

        //------------------Plaka Ekle Buton listener---------------------------------------
        buttonPlakaEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plaka = editTextPlaka.getText().toString();
                plakaEkle(plaka);
            }
        });
        //----------------------------------------------------------------------------------

        return view;
    }

    //Kayıt ekleme modunda kaydın girilmesi
    public void setKayit(Kayit kayit){
        this.kayit = kayit;
        mod = 1;
        if(listViewKayitlarim.getCount() == 0){
            Toast.makeText(getContext(),"Lütfen kaydı eklemek istediğiniz plakayı giriniz!!!",Toast.LENGTH_LONG).show();
            showIlkEklemeCustomDailog();
        }else {
            Toast.makeText(getContext(),"Lütfen kaydı eklemek istediğiniz plakayı seçiniz!!!",Toast.LENGTH_LONG).show();
        }
        kayitIptalButonGorunurluguGir();

    }

    //Mod durumuna göre alt taraftaki kayıt ekleme işlemini iptal et butonunun gösterilmesi ve gizlenmesi işlemi
    public void kayitIptalButonGorunurluguGir(){
        if(mod == 0){
            buttonKayitIptal.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    buttonKayitIptal.getLayoutParams();
            params.weight = 1.0f;
            buttonKayitIptal.setLayoutParams(params);
        }
        else {
            buttonKayitIptal.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    buttonKayitIptal.getLayoutParams();
            params.weight = 0f;
            buttonKayitIptal.setLayoutParams(params);
        }
    }

    //Fragment yok edilirken modun normal moda dönmesi
    @Override
    public void onDestroyView() {
        mod = 0;
        super.onDestroyView();;
    }

    //Plaka ekleme işlemi
    public void plakaEkle(String plaka){
        if(plaka.isEmpty()){
            Toast.makeText(getContext(),"Lütfen bir plaka giriniz!!!",Toast.LENGTH_LONG).show();
        }
        else {
            int sonuc = databaseManager.plakaEkle(plaka);
            if(sonuc == 0){
                Toast.makeText(getContext(),"Böyle bir plaka mevcut!!!",Toast.LENGTH_LONG).show();
            }
            else if(sonuc == -1){
                Toast.makeText(getContext(),"Plaka eklenirken beklenmedik bir sorun oluştu!!!",Toast.LENGTH_LONG).show();
            }
            else if(sonuc == 1){
                plakalarHeader = databaseManager.plakalariGetir();
                kayitlarChild = databaseManager.kayitlariGetir(plakalarHeader);
                kayitlarimListAdapter.setPlakalarHeader(plakalarHeader);
                kayitlarimListAdapter.setKayitlarChild(kayitlarChild);
                kayitlarimListAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Plaka başarıyla eklenmiştir...",Toast.LENGTH_LONG).show();
                editTextPlaka.setText("");
            }
            else {
                Toast.makeText(getContext(),"Plaka eklenirken beklenmedik bir sorun oluştu!!!",Toast.LENGTH_LONG).show();
            }
        }
    }

    //Kayıt modunda eklenecek plaka seçildikten sonra bu plakaya kaydın eklenmesi
    public void kayitEkle(String plaka){
        kayit.setPlaka(plaka);
        int sonuc = databaseManager.kayitEkle(kayit);
        if(sonuc == 1){
            Toast.makeText(getContext(),"Kayıt başarıyla eklenmiştir...",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(),"Kayıt eklenirken bir sorun oluştu!!!",Toast.LENGTH_LONG).show();
        }
        mod = 0;
        kayitIptalButonGorunurluguGir();
        kayitlarChild = databaseManager.kayitlariGetir(plakalarHeader);
        kayitlarimListAdapter.setKayitlarChild(kayitlarChild);
        kayitlarimListAdapter.notifyDataSetChanged();
    }

    //Eğer listede hiç plaka yoksa kayıt modu ile fragment açıldığında plaka ekle dailog'unun gösterilmesi
    public void showIlkEklemeCustomDailog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_kayitlarim_ilk_ekleme);

        Button buttonKayitlarimIlkEkleme = (Button) dialog.findViewById(R.id.buttonKayitlarimIlkEkleme);
        final EditText editTextKayitlarimIlkEkleme = (EditText) dialog.findViewById(R.id.editTextKayitlarimIlkEkleme);

        buttonKayitlarimIlkEkleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextKayitlarimIlkEkleme.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Lütfen bir plaka giriniz!!!",Toast.LENGTH_LONG).show();
                }
                else {
                    ilkEklemePlaka =  editTextKayitlarimIlkEkleme.getText().toString();
                    plakaEkle(ilkEklemePlaka);
                    kayitEkle(ilkEklemePlaka);
                    mod = 0;
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    //Kayıt silme işlemi
    public void kayitSil(int groupPosition,int childPosition){
        int sonuc = databaseManager.kayitSil(groupPosition,childPosition,plakalarHeader);
        kayitlarChild = databaseManager.kayitlariGetir(plakalarHeader);
        kayitlarimListAdapter.setKayitlarChild(kayitlarChild);
        kayitlarimListAdapter.notifyDataSetChanged();
        if(sonuc == 1){
            Toast.makeText(getContext(),"Kayıt başarıyla silinmiştir...",Toast.LENGTH_LONG).show();
        }
        else if(sonuc < 1){
            Toast.makeText(getContext(),"Kayıt silinirken bir sorun oluştu!!!",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(),"Beklenmedik bir durum oluştu!!!",Toast.LENGTH_LONG).show();
        }
    }

    //Listeden seçilen kaydı gösteren dailog ekranı
    public void showChildCustomDailog(Kayit kayit, final int groupPosition, final int childPosition){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_hesapla_sonuc);

        Button buttonCustomDialogKaydet = (Button) dialog.findViewById(R.id.buttonCustomDialogKaydet);
        Button buttonCustomDialogKapat = (Button) dialog.findViewById(R.id.buttonCustomDialogKapat);
        TextView textViewCustomDialog100KmYakitSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog100KmYakitSonuc);
        TextView textViewCustomDialog1KmUcretSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog1KmUcretSonuc);
        TextView textViewCustomDialog1GunUcretSonuc = (TextView) dialog.findViewById(R.id.textViewCustomDialog1GunUcretSonuc);

        textViewCustomDialog100KmYakitSonuc.setText(String.valueOf(numberFormat.format(kayit.getYuzKmYakitSonuc())) + " Litre");
        if (kayit.getBirKmUcretSonuc() < 1) {
            textViewCustomDialog1KmUcretSonuc.setText(String.valueOf((int) ( kayit.getBirKmUcretSonuc() * 100 )) + " Kuruş");
        }
        else{
            textViewCustomDialog1KmUcretSonuc.setText(String.valueOf(numberFormat.format(kayit.getBirKmUcretSonuc())) + " TL");
        }
        textViewCustomDialog1GunUcretSonuc.setText(String.valueOf(numberFormat.format(kayit.getBirGunUcretSonuc())) + " TL");

        buttonCustomDialogKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonCustomDialogKaydet.setText("Sil");
        buttonCustomDialogKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                kayitSil(groupPosition,childPosition);
            }
        });

        dialog.show();
    }


}
