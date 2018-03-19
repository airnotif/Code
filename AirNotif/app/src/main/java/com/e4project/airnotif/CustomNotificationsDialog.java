package com.e4project.airnotif;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class CustomNotificationsDialog extends DialogFragment {

    static final int INGORED = 133;
    static final int ACCEPTED = 321;
    static final int UNKNOWN = -1;

    private int category;
    private int position;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        category = getArguments().getInt("category", UNKNOWN);
        position = getArguments().getInt("position", UNKNOWN);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message;
        if (category == ACCEPTED) {
            message = "Accepter ?";
        } else if (category == INGORED) {
            message = "Ignorer ?";
        } else {
            message = "UNKNOWN";
        }
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onNotificationDialogValidation(position, category);
            }
        });
        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onNotificationDialogCancel(position);
            }
        });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onNotificationDialogCancel(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getParentFragment() != null) {
                listener = (NotificationDialogListener) getParentFragment();
            } else {
                listener = (NotificationDialogListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NotificationDialogListener");
        }
    }

    public interface NotificationDialogListener {
        void onNotificationDialogValidation(int position, int category);

        void onNotificationDialogCancel(int position);
    }

    private NotificationDialogListener listener;
}
