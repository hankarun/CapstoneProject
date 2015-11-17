package com.hankarun.patienthistory.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.PatientDetailActivity;
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Patient patient = mPatientList.get(position);

        String twmp = patient.getmName()+" "+patient.getmSurname();
        holder.nameTextView.setText(twmp);
        holder.birthTextView.setText(patient.getmBirthDate());
        holder.emailTextView.setText(patient.getmEmail());
        holder.phoneNumberTextView.setText(patient.getmTelephone1());
        holder.phoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        holder.phoneNumber2TextView.setText(patient.getmTelephone2());
        holder.phoneNumber2TextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        String temp = patient.getmAddress()+" " + patient.getmTown() + " - " + patient.getmCity();
        holder.addressTextView.setText(temp);
        holder.doctorNameTextView.setText(patient.getmDoctorName());
        holder.doctorTelTextView.setText(patient.getmDoctorNumber());
        holder.doctorDateTextView.setText(patient.getmDoctorDate());
        holder.problemTextView.setText(patient.getmProblems());


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
        public TextView birthTextView;
        public TextView emailTextView;
        public TextView addressTextView;
        public TextView phoneNumberTextView;
        public TextView phoneNumber2TextView;
        public TextView doctorNameTextView;
        public TextView doctorTelTextView;
        public TextView doctorDateTextView;
        public TextView problemTextView;


        public ViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.patientItem);
            nameTextView = (TextView) v.findViewById(R.id.patientNameSurnameText);
            birthTextView = (TextView) v.findViewById(R.id.patientBirthDateText);
            emailTextView = (TextView) v.findViewById(R.id.patientEmailText);
            addressTextView = (TextView) v.findViewById(R.id.patientAddress);
            phoneNumberTextView = (TextView) v.findViewById(R.id.patientTel1Text);
            phoneNumber2TextView = (TextView) v.findViewById(R.id.patientTel2Text);
            doctorNameTextView = (TextView) v.findViewById(R.id.patientDoctorName);
            doctorTelTextView = (TextView) v.findViewById(R.id.patientDoctorTel);
            doctorDateTextView = (TextView) v.findViewById(R.id.patientDoctorDate);
            problemTextView = (TextView) v.findViewById(R.id.patientProblem);
        }
    }
}
