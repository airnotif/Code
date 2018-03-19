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

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private ArrayList<Subscription> subscriptions;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout viewBackgroundMySubscribe;
        RelativeLayout viewBackgroundSubscribe;
        RelativeLayout viewForeground;
        TextView name;
        int visible;

        ViewHolder(View view) {
            super(view);
            this.viewBackgroundMySubscribe = view.findViewById(R.id.subscription_layout_my_subscribes);
            this.viewBackgroundSubscribe = view.findViewById(R.id.subscription_layout_subscribe);
            this.viewForeground = view.findViewById(R.id.subscription_layout_current);
            this.name = view.findViewById(R.id.subscription_name);
            this.visible = View.GONE;
        }
    }

    SubscriptionAdapter(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Subscription subscription = subscriptions.get(position);
        String name = subscription.getName();
        holder.name.setText(name);
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
        return subscriptions.size();
    }

    void deleteItem(int positon) {
        notifyItemRemoved(positon);
        subscriptions.remove(positon);
    }

    void addItem(int positon, Subscription subscription) {
        subscriptions.add(positon, subscription);
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
        // setVisibility
        // ex : holder.info.setVisibility(holder.visible);
    }
}
