package com.hankarun.patienthistory.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.DetailActivity;
import com.hankarun.patienthistory.fragment.DetailActivityFragment;
import com.hankarun.patienthistory.fragment.PatientListFragment;
import com.hankarun.patienthistory.model.Patient;

import java.util.List;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    Activity mActivity;

    public PatientAdapter(Activity activity){
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View test = mActivity.findViewById(R.id.container_body);
                if(test == null) {
                    Intent intent = new Intent(mActivity, DetailActivity.class);
                    //intent.putExtra(DetailActivity.EXTRA_CONTACT, contact);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, holder.mLinearLayout, "profile");
                    mActivity.startActivity(intent, options.toBundle());
                }else{
                    Fragment fragment = null;
                    Class fragmentClass = DetailActivityFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        Log.d("eror", e.toString());
                    }

                    FragmentManager fragmentManager = ((AppCompatActivity)mActivity).getSupportFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.patientItem);
        }
    }
}
