package com.yo4gis.consumersurvey.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.listeners.TaskSelectedListener;
import com.yo4gis.consumersurvey.model.AssignedTask;

import java.util.List;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.GenericViewHolder> {
    private List<AssignedTask> lstAssignedTask;
    TaskSelectedListener listener;

    public SurveyListAdapter(TaskSelectedListener listener, List<AssignedTask> lstResponseAssignedList) {
        this.lstAssignedTask = lstResponseAssignedList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_survey_list, parent, false);
        return new GenericViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder,int position) {
        holder.txtTown.setText("Town:" +lstAssignedTask.get(position).getTown());
        holder.txtFeederCode.setText("Feeder Code:" + lstAssignedTask.get(position).getFeederCode());
        holder.txtDtNin.setText("Dt Nin:" + lstAssignedTask.get(position).getDtr_nin());
        holder.txtDtName.setText("Dt Name:" + lstAssignedTask.get(position).getDt_name());
        holder.txtSurveyType.setText(lstAssignedTask.get(position).getSurveyType());
        if(lstAssignedTask.get(position).getSurveyType().equals("33 KV")){
            holder.txtFeederName.setText("Feeder Name:" + lstAssignedTask.get(position).getFeeder33());
            holder.txtSubstation.setText("Substation:" + lstAssignedTask.get(position).getSubstation33());
        } else {
            holder.txtFeederName.setText("Feeder Name:" +lstAssignedTask.get(position).getFeeder11());
            holder.txtSubstation.setText("Substation:" +lstAssignedTask.get(position).getSubstation11());
        }
        holder.layoutContainer.setOnClickListener(v->listener.onTaskSelected(position));
        /*if(AppConstants.CURRENT_LOCALE.equals(AppConstants.AR)){
            holder.txtName.setText(lstAssignedTask.get(position).getNameAr());
        } else {
            holder.txtName.setText(lstAssignedTask.get(position).getNameEn());
        }
*/
        //holder.txtName.setOnClickListener(v->listener.onCategorySelected(lstAssignedTask.get(position).getCategoryId(),position));

    }

    @Override
    public int getItemCount() {
        return  lstAssignedTask.size();
    }


    public static  class GenericViewHolder extends RecyclerView.ViewHolder {
        View container;
        LinearLayout layoutContainer;
        TextView txtTown, txtSubstation, txtFeederName, txtFeederCode, txtSurveyType,txtDtNin,txtDtName;
        GenericViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            layoutContainer = (LinearLayout) itemView.findViewById(R.id.layoutContainer);
            txtTown = (TextView)itemView.findViewById(R.id.txtTown);
            txtSubstation = (TextView)itemView.findViewById(R.id.txtSubstation);
            txtFeederName = (TextView)itemView.findViewById(R.id.txtFeederName);
            txtFeederCode = (TextView)itemView.findViewById(R.id.txtFeederCode);
            txtSurveyType = (TextView)itemView.findViewById(R.id.txtSurveyType);
            txtDtNin= (TextView)itemView.findViewById(R.id.txtdtnin);
            txtDtName= (TextView)itemView.findViewById(R.id.txtdtname);
        }
    }

}