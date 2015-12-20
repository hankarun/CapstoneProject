package com.hankarun.patienthistory.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.QuestionsActivity;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.model.Patient;


public class CheckUserActivityFragment extends Fragment implements Animation.AnimationListener, LoaderManager.LoaderCallbacks<Cursor> {
    private Button continueButton;
    private Button checkUserButton;
    private Button searchUser;
    private View divider;
    private LinearLayout phoneInput;
    private TextView tel1Input;

    private Animation animFadein;
    private Animation animFadeout;

    public CheckUserActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_user, container, false);

        continueButton = (Button) rootView.findViewById(R.id.button2);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        final CheckUserActivityFragment temp = this;
        searchUser = (Button) rootView.findViewById(R.id.button3);
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("tel", tel1Input.getText().toString());
                getLoaderManager().initLoader(0, b, temp);
            }
        });

        divider = rootView.findViewById(R.id.divider);

        phoneInput = (LinearLayout) rootView.findViewById(R.id.phoneLayout);

        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);

        animFadeout = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_out);

        animFadein.setAnimationListener(this);
        animFadeout.setAnimationListener(this);

        tel1Input = (TextView) rootView.findViewById(R.id.input_tel);
        tel1Input.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        checkUserButton = (Button) rootView.findViewById(R.id.button);
        checkUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divider.startAnimation(animFadeout);
                continueButton.startAnimation(animFadeout);
                checkUserButton.startAnimation(animFadeout);
            }
        });

        return rootView;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(animation!=animFadein) {
            divider.clearAnimation();
            continueButton.clearAnimation();
            checkUserButton.clearAnimation();
            divider.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            checkUserButton.setVisibility(View.GONE);
            phoneInput.startAnimation(animFadein);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.PATIENT_NAME,
                PatientSQLiteHelper.PATIENT_SURNAME,
                PatientSQLiteHelper.PATIENT_BDATE,
                PatientSQLiteHelper.PATIENT_TEL1,
                PatientSQLiteHelper.PATIENT_TEL2,
                PatientSQLiteHelper.PATIENT_ADDRESS,
                PatientSQLiteHelper.PATIENT_CITY,
                PatientSQLiteHelper.PATIENT_TOWN,
                PatientSQLiteHelper.PATIENT_EMAIL,
                PatientSQLiteHelper.PATIENT_DOCTOR_NAME,
                PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER,
                PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS};
        return new CursorLoader(getContext(),
                DataContentProvider.CONTENT_URI_PATIENT, projection,
                PatientSQLiteHelper.PATIENT_TEL1 + " = '" + args.getString("tel") + "'", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        Patient p = new Patient(data);
        Intent intent = new Intent(getActivity(), QuestionsActivity.class);
        intent.putExtra("patient",p);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
