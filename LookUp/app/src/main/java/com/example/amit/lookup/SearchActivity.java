package com.example.amit.lookup;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
public class SearchActivity extends TabActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Resources ressources = getResources();
        TabHost tabHost = getTabHost();

        Intent searchByCategory = new Intent().setClass(this, MainActivity.class);
        TabSpec tabSpecCategory= tabHost
                .newTabSpec("By Category")
                .setIndicator("By Category")
                .setContent(searchByCategory);

        Intent searchByName = new Intent().setClass(this, SearchByName.class);
        TabSpec tabSpecName = tabHost
                .newTabSpec("By Name")
                .setIndicator("By Name")
                .setContent(searchByName);

        tabHost.addTab(tabSpecCategory);
        tabHost.addTab(tabSpecName);

        tabHost.setCurrentTab(0);
    }


}