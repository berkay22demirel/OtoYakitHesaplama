package com.berkay22demirel.otoyakthesaplama;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.berkay22demirel.otoyakthesaplama.Adapter.MainPagerAdapter;
import com.berkay22demirel.otoyakthesaplama.Fragment.Hesapla;
import com.berkay22demirel.otoyakthesaplama.Fragment.Kayitlarim;
import com.berkay22demirel.otoyakthesaplama.Interface.OnButtonPressListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnButtonPressListener {
    private MainPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] tabBasliklari = getResources().getStringArray(R.array.tab_titles);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                        actionBar.setTitle(tabBasliklari[position]);
                    }
                });
        actionBar.addTab(actionBar.newTab().setTabListener(this).setText(tabBasliklari[0]));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setText(tabBasliklari[1]).setTag("kayitlarim"));
        actionBar.addTab(actionBar.newTab().setTabListener(this).setText(tabBasliklari[2]));

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onButtonPressed(Kayit kayit) {
        Kayitlarim kayitlarim = (Kayitlarim) getSupportFragmentManager().getFragments().get(1);
        kayitlarim.setKayit(kayit);
    }
}



