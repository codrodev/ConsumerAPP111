package com.yo4gis.consumersurvey.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.Navigation;

import com.yo4gis.consumersurvey.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yo4gis.consumersurvey.views.listeners.TaskDetailListener;

public class TaskDetailDialogFragment extends BottomSheetDialogFragment {
    private View mRootView;
    static TaskDetailListener listener;

    public static  TaskDetailDialogFragment newInstance(TaskDetailListener list){
        TaskDetailDialogFragment f = new TaskDetailDialogFragment();
        listener = list;
        return f;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_fragment_task_detail, null);
        Button btnStart = (Button) contentView.findViewById(R.id.btnStart);

        contentView.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(contentView);


        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onStartClick();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}