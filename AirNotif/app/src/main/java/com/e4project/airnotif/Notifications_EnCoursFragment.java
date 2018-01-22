package com.e4project.airnotif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Notifications_EnCoursFragment extends Fragment{

    ListView listView;
    private ArrayList<MoyenIndustriel> moyenIndustriels;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.notifications_encours, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView_enCours);
        moyenIndustriels = new ArrayList<MoyenIndustriel>();
        for (int i = 0 ; i < 10 ; i ++) {
            MoyenIndustriel mi = new MoyenIndustriel();

            mi.getRef().DATE = new Date();
            mi.getRef().STATION = "Station " + Integer.toString(new Random().nextInt(100));
            mi.getRef().PROGRAM = "Program " + Integer.toString(new Random().nextInt(50));

            mi.getSystem().STATUS = new Random().nextInt(100);

            mi.getProcess().STATUS = new Random().nextInt(130);
            mi.getProcess().NOGO = new Random().nextInt(1);

            mi.getFunction().SESSION = new Random().nextInt(2);

            moyenIndustriels.add(mi);
        }
        listView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return moyenIndustriels.size();
            }

            @Override
            public Object getItem(int i) {
                return moyenIndustriels.get(i);
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
                    view = inflater.inflate(R.layout.activity_notifications_view, viewGroup, false);
                    holder = new ViewHolder();
                    holder.ref_station =  (TextView) view.findViewById(R.id.notification_ref_station);
                    holder.system_status = (TextView) view.findViewById(R.id.notification_system_status);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }

                final MoyenIndustriel moyenIndustriel = getMoyenIndustriel(i);

                holder.ref_station.setText(moyenIndustriel.getRef().STATION);
                holder.system_status.setText(String.format(Locale.getDefault(),"%d", moyenIndustriel.getSystem().STATUS));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), NotificationsDialogViewActivity.class);
                        intent.putExtra("mi", moyenIndustriel);
                        startActivity(intent);
                    }
                });

                return view;
            }

            private MoyenIndustriel getMoyenIndustriel(int i) {
                return (MoyenIndustriel) getItem(i);
            }

            class ViewHolder{
                TextView ref_station;
                TextView system_status;
            }
        });
    }
}
