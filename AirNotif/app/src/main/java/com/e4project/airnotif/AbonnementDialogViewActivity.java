package com.e4project.airnotif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class AbonnementDialogViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_dialog);

        final Abonnement abonnement = (Abonnement) getIntent().getSerializableExtra("abonnement");

        setTitle(abonnement.getText());

        Button fermer = (Button) findViewById(R.id.abonnement_dialog_fermer);
        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final boolean isChecked = abonnement.isChecked();
        final CheckBox checkBox = (CheckBox) findViewById(R.id.abonnement_checkBox);
        Button toggle = (Button) findViewById(R.id.abonnement_dialog_toggle);
        if(isChecked){
            toggle.setText("Se désabonner");
        }
        else{
            toggle.setText("S'abonner");
        }
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abonnement.setChecked(!isChecked);
                finish();
            }
        });
    }
}
