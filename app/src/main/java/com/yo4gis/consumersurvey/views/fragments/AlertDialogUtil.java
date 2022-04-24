package com.yo4gis.consumersurvey.views.fragments;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yo4gis.consumersurvey.R;


public class AlertDialogUtil {

    private static ViewGroup viewGroup;
    public static TextView btnOk;
    public static TextView btnCancel;
    public static void alertDialog(String heading, String message, Activity context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog,
                (ViewGroup)context.findViewById(android.R.id.content), false);


        final TextView txtHeader = (TextView) viewInflated.findViewById(R.id.header);
        final TextView txtContent = (TextView) viewInflated.findViewById(R.id.content);
        btnOk = (TextView) viewInflated.findViewById(R.id.btnOk);

        txtHeader.setText(heading);
        txtContent.setText(message);

        builder.setView(viewInflated);
        final android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
    
    
    public static void alertDialogConfirmation(String heading, String message, final Activity context, OnAlertSelectedListener listener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog_confirmation,
                (ViewGroup)context.findViewById(android.R.id.content), false);


        final TextView txtHeader = (TextView) viewInflated.findViewById(R.id.header);
        final TextView txtContent = (TextView) viewInflated.findViewById(R.id.content);
        final TextView btnOk = (TextView) viewInflated.findViewById(R.id.btnOk);
        final TextView btnCancel = (TextView) viewInflated.findViewById(R.id.btnCancel);

        txtHeader.setText(heading);
        txtContent.setText(message);

        builder.setView(viewInflated);
        final android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAlertSelected(false);
                dialog.cancel();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAlertSelected(true);
                dialog.cancel();
            }
        });
    }

    public interface OnAlertSelectedListener {
        void onAlertSelected(boolean isVisible);
    }
}
