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

public class AbonnementsFragment extends Fragment {

    private static final int SABONNER = 0;
    private static final int MESABONNEMENTS = 1;
    private static final int DROITE_A_GAUCHE = 10;
    private static final int GAUCHE_A_DROITE = 11;
    private static int NAVIGATION;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.abonnements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.abonnements_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation(new Abonnements_SabonnerFragment(), 0);
        NAVIGATION = SABONNER;
        super.onViewCreated(view, savedInstanceState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            int orientation = 0;
            switch (item.getItemId()) {
                case R.id.navigation_sabonner:
                    if (NAVIGATION != SABONNER) {
                        NAVIGATION = SABONNER;
                        fragment = new Abonnements_SabonnerFragment();
                        orientation = GAUCHE_A_DROITE;
                    }
                    break;
                case R.id.navigation_mesabonnements:
                    if (NAVIGATION != MESABONNEMENTS) {
                        NAVIGATION = MESABONNEMENTS;
                        fragment = new Abonnements_SabonnerFragment();
                        orientation = DROITE_A_GAUCHE;
                    }
                    break;
            }
            if (fragment != null) {
                navigation(fragment, orientation);
                return true;
            }
            return false;
        }
    };

    public void navigation(Fragment fragment, int orientation) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        switch (orientation) {
            case DROITE_A_GAUCHE:
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case GAUCHE_A_DROITE:
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
        ft.replace(R.id.abonnements_content_menu, fragment);
        ft.commit();
    }

}
