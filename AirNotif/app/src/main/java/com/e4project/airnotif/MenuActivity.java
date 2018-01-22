package com.e4project.airnotif;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

/**
 * Classe principale de l'application où les principales interfaces utilisateurs sont gérés via des fragments
 * @author Nicolas RACIC
 * @version 07/12/2017
 */
public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences("airnotif", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Si l'application à été rafraichie via le menu des réglages...
        if(prefs.getBoolean("reglageChange", false)){
            editor.putBoolean("reglageChange", false);
            editor.apply();
            // ... on retourne sur le fragment des réglages
            reglages();
        }
        else{ // Sinon on retourne sur le fragment des notifications (par défaut)
            navigationView.setCheckedItem(R.id.nav_norifications);
            menuSelection(R.id.nav_norifications);
        }
        // Si l'application vient d'être démarrée...
        if(prefs.getBoolean("demarrageApp", false)) {
            editor.putBoolean("demarrageApp", false);
            editor.apply();
            String[] code_langues = getResources().getStringArray(R.array.code_langues);
            if(!prefs.contains("langSelection")) {
                String langue = Locale.getDefault().getLanguage();
                for (int i = 0; i < code_langues.length; i++) {
                    // ... et si la langue de l'appareil figure dans les réglages...
                    if (code_langues[i].equals(langue)) {
                        editor.putInt("langSelection", i);
                        editor.apply();
                        break;
                    }
                }
            }
            // On change la langue de l'application
            changerLangue(code_langues[prefs.getInt("langSelection", 0)]);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reglages) {
            reglages();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Changement du fragment en fonction du menu sélectionné
     * @param id l'id du menu sélectionné
     */
    private void menuSelection(final int id) {
        Fragment fragment = null;
        if (id == R.id.nav_norifications) {
            setTitle(R.string.title_notifications);
            fragment = new NotificationsFragment();
        } else if (id == R.id.nav_abonnements) {
            setTitle(R.string.title_abonnements);
            fragment = new AbonnementsFragment();
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_menu, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        menuSelection(id);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Permet d'accéder au menu des réglages
     */
    public void reglages(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_none);
        setTitle(R.string.title_reglages);
        Fragment fragment = new ReglagesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_menu, fragment);
        ft.commit();
    }

    /**
     * Permet de changer la langue de l'application
     * @param lang la langue choisie sous forme ISO 639-1 (2 lettres : fr, en, etc)
     */
    public void changerLangue(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale locale = new Locale(lang.toLowerCase());
        conf.setLocale(locale);
        res.updateConfiguration(conf, dm);
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
