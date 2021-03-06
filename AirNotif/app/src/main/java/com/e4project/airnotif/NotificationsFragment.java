package com.e4project.airnotif;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationsFragment extends Fragment implements NotificationsCategoryFragment.NotificationsFragmentListener {

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = new NotificationsCategoryFragment();
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_in_progress:
                    bundle.putInt("category", NotificationsCategoryFragment.IN_PROGRESS);
                    fragment.setArguments(bundle);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_ignored:
                    bundle.putInt("category", NotificationsCategoryFragment.IGNORED);
                    fragment.setArguments(bundle);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    bundle.putInt("category", NotificationsCategoryFragment.HISTORY);
                    fragment.setArguments(bundle);
                    changeFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    void changeFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_notifications_content_menu, fragment);
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        this.navigation = root.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_in_progress);
        return root;
    }

    @Override
    public void onNotificationSwipe(int category) {
        if(category == NotificationsCategoryFragment.IGNORED){
            this.navigation.getMenu().getItem(R.id.navigation_ignored).getIcon();
        } else if(category == NotificationsCategoryFragment.HISTORY){

        }
    }
}
