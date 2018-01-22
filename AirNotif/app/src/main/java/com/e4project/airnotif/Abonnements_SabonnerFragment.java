package com.e4project.airnotif;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class Abonnements_SabonnerFragment extends Fragment {

    ListView listView;
    private String[] prenoms = new String[]{
            "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
            "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
            "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
            "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
            "Yann", "Zoé"};

    private ArrayList<Abonnement> abonnements;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.abonnements_sabonner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        abonnements = new ArrayList<Abonnement>();
        for (String prenom : prenoms) {
            abonnements.add(new Abonnement(false, prenom));
        }
        listView = (ListView) view.findViewById(R.id.listView_sabonner);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return abonnements.size();
            }

            @Override
            public Object getItem(int i) {
                return abonnements.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View convertView, ViewGroup viewGroup) {
                View view = convertView;
                ViewHolder holder = null;
                if(view == null) {
                    view = inflater.inflate(R.layout.abonnement_view, viewGroup, false);
                    holder = new ViewHolder();
                    holder.textView =  (TextView) view.findViewById(R.id.abonnement_textView);
                    holder.checkBox = (CheckBox) view.findViewById(R.id.abonnement_checkBox);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }

                final Abonnement abonnement = getAbonnement(i);

                holder.textView.setText(abonnement.getText());
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        abonnement.setChecked(b);
                    }
                });
                holder.checkBox.setChecked(abonnement.isChecked());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), AbonnementDialogViewActivity.class);
                        intent.putExtra("abonnement", abonnement);
                        startActivityForResult(intent, 0);
                    }
                });
                return view;
            }

            private Abonnement getAbonnement(int i) {
                return (Abonnement) getItem(i);
            }

            class ViewHolder{
                TextView textView;
                CheckBox checkBox;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
