package com.e4project.airnotif;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

/**
 * L'écran d'accueil de l'application
 * Il passe à l'autre activité après quelques secondes où lorsque l'utilisateur touche l'écran
 * @author Nicolas RACIC
 * @version 07/12/2017
 */
public class MainActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Les préférences nous servent ici à garder en mémoire certaines valeurs
        SharedPreferences prefs = getSharedPreferences("airnotif", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("demarrageApp", true);
        editor.apply();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeActivity();
            }
        }, TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        changeActivity();
        return super.onTouchEvent(event);
    }

    private void changeActivity() {
        handler.removeCallbacksAndMessages(null);
        SharedPreferences prefs = getSharedPreferences("airnotif", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Class activityClass = null;
        if(!prefs.contains("connecte")){
            editor.putBoolean("connecte", false);
            editor.apply();
        }
        // Si l'utilisateur est déjà connecté...
        if(prefs.getBoolean("connecte", false)){
            activityClass = MenuActivity.class;
        }
        else{ // Sinon...
            activityClass = ConnectionActivity.class;
        }
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }
}
