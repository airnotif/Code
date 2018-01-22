package com.e4project.airnotif;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import static android.content.Context.MODE_PRIVATE;

public class ReglagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reglages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final SharedPreferences prefs = getActivity().getSharedPreferences("airnotif", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        /*-- Langue --*/
        final String[] code_langues = getResources().getStringArray(R.array.code_langues);
        Spinner spinnerLangue = (Spinner) view.findViewById(R.id.spinner_langue);
        ArrayAdapter<CharSequence> adapterLangue = ArrayAdapter.createFromResource(view.getContext(), R.array.liste_langues, android.R.layout.simple_spinner_item);
        adapterLangue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLangue.setAdapter(adapterLangue);
        if(!prefs.contains("langSelection")) {
            editor.putInt("langSelection", 0);
            editor.apply();
        }
        spinnerLangue.setSelection(prefs.getInt("langSelection", 0));
        spinnerLangue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (prefs.getInt("langSelection", 0) != i) {
                    editor.putInt("langSelection", i);
                    editor.putBoolean("reglageChange", true);
                    editor.apply();
                    ((MenuActivity)getActivity()).changerLangue(code_langues[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*-- Deconnection --*/
        ImageButton boutonDeconnection = (ImageButton) view.findViewById(R.id.deconnection);
        boutonDeconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("connecte", false);
                editor.apply();
                Intent intent = new Intent(ReglagesFragment.this.getContext(), ConnectionActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
