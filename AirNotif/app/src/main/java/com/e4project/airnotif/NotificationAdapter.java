package com.e4project.airnotif;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<Notification> notifications;
    private SimpleDateFormat dateFormat;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout viewBackgroundIgnored;
        RelativeLayout viewBackgroundHistory;
        RelativeLayout viewForeground;
        TextView date;
        TextView info1;
        TextView info2;
        TextView info3;
        TextView info4;
        TextView info5;
        int visible;

        ViewHolder(View view) {
            super(view);
            this.viewBackgroundIgnored = view.findViewById(R.id.notification_layout_ignored);
            this.viewBackgroundHistory = view.findViewById(R.id.notification_layout_history);
            this.viewForeground = view.findViewById(R.id.notificaton_layout_current);
            this.date = view.findViewById(R.id.notification_date);
            this.info1 = view.findViewById(R.id.info1);
            this.info2 = view.findViewById(R.id.info2);
            this.info3 = view.findViewById(R.id.info3);
            this.info4 = view.findViewById(R.id.info4);
            this.info5 = view.findViewById(R.id.info5);
            this.visible = View.GONE;
        }
    }

    NotificationAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Notification notification = notifications.get(position);
        Date date = notification.getDate();
        String str = dateFormat.format(date);
        holder.date.setText(str);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisible(holder);
                // Elargir la vue
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    void deleteItem(int positon) {
        notifyItemRemoved(positon);
        notifications.remove(positon);
    }

    void addItem(int positon, Notification notification) {
        notifications.add(positon, notification);
        notifyItemInserted(positon);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.visible == View.VISIBLE) {
            toggleVisible(holder);
        }
    }

    private void toggleVisible(ViewHolder holder) {
        if (holder.visible == View.GONE) {
            holder.visible = View.VISIBLE;
        } else if (holder.visible == View.VISIBLE) {
            holder.visible = View.GONE;
        }
        holder.info1.setVisibility(holder.visible);
        holder.info2.setVisibility(holder.visible);
        holder.info3.setVisibility(holder.visible);
        holder.info4.setVisibility(holder.visible);
        holder.info5.setVisibility(holder.visible);
    }
}
