package com.e4project.airnotif;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NotificationsFragment extends Fragment {

    private static final int ENCOURS = 0;
    private static final int IGNOREES = 1;
    private static final int HISTORIQUE = 2;
    private static final int DROITE_A_GAUCHE = 10;
    private static final int GAUCHE_A_DROITE = 11;
    private static int NAVIGATION;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.notifications_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation(new Notifications_EnCoursFragment(), 0);
        super.onViewCreated(view, savedInstanceState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            int orientation = 0;
            switch (item.getItemId()) {
                case R.id.navigation_encours:
                    if(NAVIGATION != ENCOURS) {
                        NAVIGATION = ENCOURS;
                        orientation = GAUCHE_A_DROITE;
                        fragment = new Notifications_EnCoursFragment();
                    }
                    break;
                case R.id.navigation_ignorees:
                    if(NAVIGATION != IGNOREES) {
                        if(NAVIGATION == ENCOURS){
                            orientation = DROITE_A_GAUCHE;
                        }
                        if(NAVIGATION == HISTORIQUE){
                            orientation = GAUCHE_A_DROITE;
                        }
                        NAVIGATION = IGNOREES;
                        fragment = new Notifications_IgnoreesFragment();
                    }
                    break;
                case R.id.navigation_historique:
                    if(NAVIGATION != HISTORIQUE) {
                        NAVIGATION = HISTORIQUE;
                        orientation = DROITE_A_GAUCHE;
                        fragment = new Notifications_HistoriqueFragment();
                    }
                    break;
            }
            if(fragment != null){
                navigation(fragment, orientation);
                return true;
            }
            return false;
        }
    };

    public void navigation(Fragment fragment, int orientation){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        switch (orientation) {
            case DROITE_A_GAUCHE:
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case GAUCHE_A_DROITE:
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
        ft.replace(R.id.notifications_content_menu, fragment);
        ft.commit();
    }

}
