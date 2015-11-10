package com.hankarun.patienthistory.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.PatientDetailActivity;
import com.hankarun.patienthistory.fragment.PatientDetailFragment;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    private final Activity mActivity;
    private ArrayList<Patient> mPatientList;


    public PatientAdapter(Activity activity, ArrayList<Patient> patients){
        mActivity = activity;
        mPatientList = patients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Patient patient = mPatientList.get(position);

        holder.nameTextView.setText(patient.getmName());
        holder.surnameTextView.setText(patient.getmSurname());
        holder.birthTextView.setText(patient.getmBirthDate());
        holder.emailTextView.setText(patient.getmEmail());
        holder.phoneNumberTextView.setText(patient.getmTelephone1());
        holder.phoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        holder.phoneNumber2TextView.setText(patient.getmTelephone2());
        holder.phoneNumber2TextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PatientDetailActivity.class);
                intent.putExtra("patient",patient);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, holder.mLinearLayout, "profile");
                mActivity.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPatientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView nameTextView;
        public TextView surnameTextView;
        public TextView birthTextView;
        public TextView emailTextView;
        public TextView phoneNumberTextView;
        public TextView phoneNumber2TextView;


        public ViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.patientItem);
            nameTextView = (TextView) v.findViewById(R.id.patientNameText);
            surnameTextView = (TextView) v.findViewById(R.id.patientSurnameText);
            birthTextView = (TextView) v.findViewById(R.id.patientBirthDateText);
            emailTextView = (TextView) v.findViewById(R.id.patientEmailText);
            phoneNumberTextView = (TextView) v.findViewById(R.id.patientNumberText);
            phoneNumber2TextView = (TextView) v.findViewById(R.id.patientNumber2);
        }
    }
}
