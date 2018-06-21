package com.berkay22demirel.otoyakthesaplama.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.berkay22demirel.otoyakthesaplama.Fragment.Hesapla;
import com.berkay22demirel.otoyakthesaplama.Fragment.Kayitlarim;
import com.berkay22demirel.otoyakthesaplama.Fragment.Rapor;
import com.berkay22demirel.otoyakthesaplama.Interface.OnButtonPressListener;
import com.berkay22demirel.otoyakthesaplama.Kayit;


/**
 * Created by BerkayDemirel on 08.02.2018.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Hesapla();
            case 1:
                return new Kayitlarim();

            case 2:
                return new Rapor();
        }
        throw new IllegalArgumentException("bilinmeyen tab fragment");
    }

    @Override
    public int getCount() {
        return 3;
    }




}
