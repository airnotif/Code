package com.e4project.airnotif;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class SubscriptionsCategoryFragment extends Fragment implements CustomSubscriptionsDialog.SubscriptionDialogListener {

    static final int SUBSCRIBE = 639;
    static final int MY_SUBSCRIBES = 928;

    private ArrayList<Subscription> subscriptions;
    private View root;
    private SubscriptionAdapter adapter;
    private int category;

    private SharedPreferences prefs;
    private String ipAddress;
    private int port;

    private String[] example = {"Alexandre", "Nicolas", "Yoann", "Camille", "Océane", "Roxanne", "Julien", "Colombe", "Mathilde", "Floriane", "Cédric", "Astrid", "Guillaume", "Antoine", "Aurélie", "Amandine", "Sébastien", "Wilfried", "William", "Siam", "Amélina"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscriptions = new ArrayList<>();
        category = getArguments().getInt("category", SUBSCRIBE);

        prefs = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);

        ipAddress = prefs.getString("ipAddress", "NULL");
        port = prefs.getInt("port", 0);

        if (category == MY_SUBSCRIBES) {
            new Thread(new MySubscriptionsRunnable()).start();
        } else {
            new Thread(new SubcribeRunnable()).start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_subscriptions_category, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.subscription_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new SubscriptionAdapter(subscriptions);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CustomSubscriptionsSimpleCallback(0, (category == MY_SUBSCRIBES ? ItemTouchHelper.LEFT : 0) | (category == SUBSCRIBE ? ItemTouchHelper.RIGHT : 0)));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return root;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() != null) {
                listener = (SubscriptionsFragmentListener) getParentFragment();
            } else {
                listener = (SubscriptionsFragmentListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SubscriptionsFragmentListerer");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private SubscriptionsFragmentListener listener;*/

    @Override
    public void onSubscriptionDialogValidation(final int position, int category) {
        final Subscription subscription = subscriptions.get(position);
        adapter.deleteItem(position);
        if(category == CustomSubscriptionsDialog.MY_SUBSCRIPTIONS){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean addSubscribeSuccess = Connection.addSubscribe(prefs.getString("userName","NULL"), subscription.getName(), ipAddress, port);
                    if(addSubscribeSuccess) {
                        Snackbar.make(root, "Vous vous êtes abonné", Snackbar.LENGTH_LONG)
                                .setAction("Annuler", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean removeSubscribeSuccess = Connection.removeSubscribe(prefs.getString("userName","NULL"), subscription.getName(), ipAddress, port);
                                                if(removeSubscribeSuccess) {
                                                    adapter.addItem(position, subscription);
                                                }
                                                else {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast toast = Toast.makeText(getActivity(), "Erreur lors de l'annulation", Toast.LENGTH_LONG);
                                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                                            toast.show();
                                                        }
                                                    });
                                                }
                                            }
                                        }).start();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.blueLight))
                                .show();
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getActivity(), "Erreur lors de l'abonnement", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });
                    }
                }
            }).start();
        }
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean removeSubscribeSuccess = Connection.removeSubscribe(prefs.getString("userName","NULL"), subscription.getName(), ipAddress, port);
                    if(removeSubscribeSuccess) {
                        Snackbar.make(root, "Vous vous êtes desabonné", Snackbar.LENGTH_LONG)
                                .setAction("Annuler", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                boolean addSubscribeSuccess = Connection.addSubscribe(prefs.getString("userName","NULL"), subscription.getName(), ipAddress, port);
                                                if(addSubscribeSuccess) {
                                                    adapter.addItem(position, subscription);
                                                }
                                                else{
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast toast = Toast.makeText(getActivity(), "Erreur lors de l'annulation", Toast.LENGTH_LONG);
                                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                                            toast.show();
                                                        }
                                                    });
                                                }
                                            }
                                        }).start();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.blueLight))
                                .show();
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getActivity(), "Erreur lors du desabonnement", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    @Override
    public void onSubscriptionDialogCancel(int position) {
        adapter.notifyItemChanged(position);
    }

    public interface SubscriptionsFragmentListener {
        void onSubscriptionSwipe(final int category);
    }

    class CustomSubscriptionsSimpleCallback extends ItemTouchHelper.SimpleCallback {

        CustomSubscriptionsSimpleCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            DialogFragment dialog = new CustomSubscriptionsDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("position", viewHolder.getAdapterPosition());
            if (direction == ItemTouchHelper.RIGHT) { // My subscriptions
                bundle.putInt("category", CustomSubscriptionsDialog.MY_SUBSCRIPTIONS);
            } else if (direction == ItemTouchHelper.LEFT) { // Subscribe
                bundle.putInt("category", CustomSubscriptionsDialog.SUBSCRIBE);
            }
            dialog.setArguments(bundle);
            dialog.show(getChildFragmentManager(), "CustomSubscriptionsDialog");
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                final View foregroundView = ((SubscriptionAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((SubscriptionAdapter.ViewHolder) viewHolder).viewForeground;
            final View backgroundViewMySubscribe = ((SubscriptionAdapter.ViewHolder) viewHolder).viewBackgroundMySubscribe;
            final View backgroundViewSubscribe = ((SubscriptionAdapter.ViewHolder) viewHolder).viewBackgroundSubscribe;
            backgroundViewMySubscribe.setVisibility(View.VISIBLE);
            backgroundViewSubscribe.setVisibility(View.VISIBLE);
            if (dX > 0) {
                backgroundViewSubscribe.setVisibility(View.INVISIBLE);
            } else if (dX < 0) {
                backgroundViewMySubscribe.setVisibility(View.INVISIBLE);
            }
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final View foregroundView = ((SubscriptionAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((SubscriptionAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }

    void fillExampleSubscribe() {
        for (String str : example) {
            subscriptions.add(new Subscription(str));
        }
    }

    class SubcribeRunnable implements Runnable {
        @Override
        public void run() {
            ArrayList<ArrayList<String>> subs = Connection.getAllSubscriptions(prefs.getString("userName", "NULL"), ipAddress, port);
            if(subs != null && subs.get(0).get(0).equals("true")) {
                int i;
                for (i = 1 ; i < subs.size() ; i++) {
                    subscriptions.add(new Subscription(subs.get(i).get(0)));
                }
            }
            else{
                Toast toast = Toast.makeText(getActivity(), "Erreur lors de la récupération des abonnements", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    class MySubscriptionsRunnable implements Runnable {
        @Override
        public void run() {
            ArrayList<ArrayList<String>> subs = Connection.getMySubscriptions(prefs.getString("userName", "NULL"), ipAddress, port);
            if(subs != null && subs.get(0).get(0).equals("true")) {
                int i;
                for (i = 1 ; i < subs.size() ; i++) {
                    subscriptions.add(new Subscription(subs.get(i).get(0)));
                }
            }
            else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getActivity(), "Erreur lors de la récupération des abonnements", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
