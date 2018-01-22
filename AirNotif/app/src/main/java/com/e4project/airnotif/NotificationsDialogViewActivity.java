package com.e4project.airnotif;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Locale;

public class NotificationsDialogViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_notifications_dialog_view);

        final MoyenIndustriel mi = (MoyenIndustriel) getIntent().getSerializableExtra("mi");

        setTitle("Informations");

        MoyenIndustriel.Ref ref = mi.getRef();
        MoyenIndustriel.System system = mi.getSystem();
        MoyenIndustriel.Process process = mi.getProcess();
        MoyenIndustriel.Function function = mi.getFunction();

        /*-- REF --*/
        ((TextView) findViewById(R.id.mi_ref_date))
                .setText(DateFormat.getDateInstance().format(ref.DATE));
        ((TextView) findViewById(R.id.mi_ref_station))
                .setText(ref.STATION);
        ((TextView) findViewById(R.id.mi_ref_program))
                .setText(ref.PROGRAM);

        /*-- SYSTEM --*/
        ((TextView) findViewById(R.id.mi_system_status))
                .setText(Integer.toString(system.STATUS));

        /*-- PROCESS --*/
        ((TextView) findViewById(R.id.mi_process_status))
                .setText(Integer.toString(process.STATUS));
        ((TextView) findViewById(R.id.mi_process_nogo))
                .setText(Integer.toString(process.NOGO));

        /*-- FUNCTION --*/
        ((TextView) findViewById(R.id.mi_function_session))
                .setText(Integer.toString(function.SESSION));

        /*-- IGNORER --*/
        ((Button) findViewById(R.id.mi_ignorer))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        /*-- ACCEPTER --*/
        ((Button) findViewById(R.id.mi_accepter))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

        /*-- FERMER --*/
        ((Button) findViewById(R.id.mi_fermer))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }
}
