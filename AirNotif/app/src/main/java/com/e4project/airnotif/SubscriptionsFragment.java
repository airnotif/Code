package com.e4project.airnotif;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SubscriptionsFragment extends Fragment {

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = new SubscriptionsCategoryFragment();
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_subscribe:
                    bundle.putInt("category", SubscriptionsCategoryFragment.SUBSCRIBE);
                    fragment.setArguments(bundle);
                    changeFragment(fragment);
                    return true;
                case R.id.navigation_my_subscribes:
                    bundle.putInt("category", SubscriptionsCategoryFragment.MY_SUBSCRIBES);
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
            ft.replace(R.id.fragment_subscriptions_content_menu, fragment);
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        BottomNavigationView navigation = root.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_subscribe);
        return root;
    }
}
