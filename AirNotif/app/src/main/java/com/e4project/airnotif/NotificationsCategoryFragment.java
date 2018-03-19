package com.e4project.airnotif;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationsCategoryFragment extends Fragment implements CustomNotificationsDialog.NotificationDialogListener {

    static final int IN_PROGRESS = 885;
    static final int IGNORED = 383;
    static final int HISTORY = 660;

    private ArrayList<Notification> notifications;
    private View root;
    private NotificationAdapter adapter;
    private int category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifications = new ArrayList<>();
        category = getArguments().getInt("category", IN_PROGRESS);
        if (category == IGNORED) {
            fillRandomNotifications();
        } else if (category == HISTORY) {
            fillRandomNotifications();
        } else {
            fillRandomNotifications();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications_category, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.notification_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CustomNotificationsSimpleCallback(0, category == IN_PROGRESS ? (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) : 0));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return root;
    }

    void fillRandomNotifications() {
        for (int i = 0; i < 40; i++) {
            notifications.add(new Notification(getRandomDate()));
        }
    }

    // --- pour générer des dates aléatoirement ---
    public Date getRandomDate() {
        int year = 2018;
        int month = 1;
        int day = randBetween(1, 12);
        int hour = randBetween(8, 18);
        int min = randBetween(0, 59);
        int sec = randBetween(0, 59);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day, hour, min, sec);
        return gregorianCalendar.getTime();
    }

    public int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() != null) {
                listener = (NotificationsFragmentListener) getParentFragment();
            } else {
                listener = (NotificationsFragmentListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NotificationsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private NotificationsFragmentListener listener;*/

    @Override
    public void onNotificationDialogValidation(final int position, int category) {
        final Notification notification = notifications.get(position);
        adapter.deleteItem(position);
        Snackbar.make(root, "La notification est " + (category == CustomNotificationsDialog.INGORED ? "ignorée" : "acceptée"), Snackbar.LENGTH_LONG)
                .setAction("Annuler", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.addItem(position, notification);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.blueLight))
                .show();
    }

    @Override
    public void onNotificationDialogCancel(int position) {
        adapter.notifyItemChanged(position);
    }

    public interface NotificationsFragmentListener {
        void onNotificationSwipe(final int category);
    }

    class CustomNotificationsSimpleCallback extends ItemTouchHelper.SimpleCallback {

        CustomNotificationsSimpleCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            DialogFragment dialog = new CustomNotificationsDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("position", viewHolder.getAdapterPosition());
            if (direction == ItemTouchHelper.RIGHT) { // Ignore
                bundle.putInt("category", CustomNotificationsDialog.INGORED);
            } else if (direction == ItemTouchHelper.LEFT) { // Accept (
                bundle.putInt("category", CustomNotificationsDialog.ACCEPTED);
            }
            dialog.setArguments(bundle);
            dialog.show(getChildFragmentManager(), "CustomNotificationsDialog");
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                final View foregroundView = ((NotificationAdapter.ViewHolder) viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((NotificationAdapter.ViewHolder) viewHolder).viewForeground;
            final View backgroundViewIgnored = ((NotificationAdapter.ViewHolder) viewHolder).viewBackgroundIgnored;
            final View backgroundViewHistory = ((NotificationAdapter.ViewHolder) viewHolder).viewBackgroundHistory;
            backgroundViewIgnored.setVisibility(View.VISIBLE);
            backgroundViewHistory.setVisibility(View.VISIBLE);
            if (dX > 0) {
                backgroundViewHistory.setVisibility(View.INVISIBLE);
            } else if (dX < 0) {
                backgroundViewIgnored.setVisibility(View.INVISIBLE);
            }
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final View foregroundView = ((NotificationAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((NotificationAdapter.ViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
