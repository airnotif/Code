package com.e4project.airnotif;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Classe de connexion
 * @author Nicolas RACIC
 * @version 07/12/2017
 */
public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setTitle(R.string.title_connexion);

        ImageButton boutonConnection = (ImageButton) findViewById(R.id.connection);
        boutonConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("airnotif", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("connecte", true);
                editor.apply();
                Intent intent = new Intent(ConnectionActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
