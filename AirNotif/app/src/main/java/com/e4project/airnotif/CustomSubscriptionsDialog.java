package com.e4project.airnotif;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class CustomSubscriptionsDialog extends DialogFragment {

    static final int MY_SUBSCRIPTIONS = 89;
    static final int SUBSCRIBE = 721;
    static final int UNKNOWN = -1;

    private int category;
    private int position;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        category = getArguments().getInt("category", UNKNOWN);
        position = getArguments().getInt("position", UNKNOWN);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message;
        if (category == MY_SUBSCRIPTIONS) {
            message = "S'abonner ?";
        } else if (category == SUBSCRIBE) {
            message = "Se désabonner ?";
        } else {
            message = "UNKNOWN";
        }
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onSubscriptionDialogValidation(position, category);
            }
        });
        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onSubscriptionDialogCancel(position);
            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onSubscriptionDialogCancel(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() != null) {
                listener = (SubscriptionDialogListener) getParentFragment();
            } else {
                listener = (SubscriptionDialogListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SubscriptionDialogListener");
        }
    }

    public interface SubscriptionDialogListener {
        void onSubscriptionDialogValidation(int position, int category);

        void onSubscriptionDialogCancel(int position);
    }

    private SubscriptionDialogListener listener;
}
