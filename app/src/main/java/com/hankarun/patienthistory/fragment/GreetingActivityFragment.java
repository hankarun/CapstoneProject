package com.hankarun.patienthistory.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.CheckUserActivity;
import com.hankarun.patienthistory.activity.GreetingActivity;
import com.hankarun.patienthistory.activity.QuestionsActivity;

public class GreetingActivityFragment extends Fragment implements Button.OnClickListener, Animation.AnimationListener {
    private TextView txtMessage;
    private TextView mainMessage;
    private Button button;
    private Button begin;

    // Animation
    private Animation animFadein;
    private Animation animFadein1;
    private Animation animFadein2;
    private Animation animFadeout;

    public GreetingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_greeting, container, false);
        button = (Button) rootView.findViewById(R.id.startButton);
        button.setOnClickListener(this);

        txtMessage = (TextView) rootView.findViewById(R.id.greetingText);
        mainMessage = (TextView) rootView.findViewById(R.id.mainText);
        begin = (Button) rootView.findViewById(R.id.beginButton);

        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);

        animFadein1 = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);

        animFadein2 = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in);

        animFadeout = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_out);

        animFadein.setAnimationListener(this);
        animFadein1.setAnimationListener(this);
        animFadein2.setAnimationListener(this);
        animFadeout.setAnimationListener(this);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        begin.startAnimation(animation);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin.clearAnimation();
                begin.startAnimation(animFadeout);
            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CheckUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animFadein) {
            mainMessage.startAnimation(animFadein1);
        }
        if (animation == animFadein1) {
            button.startAnimation(animFadein2);
        }
        if (animation == animFadeout) {
            begin.clearAnimation();
            begin.setVisibility(View.GONE);
            txtMessage.startAnimation(animFadein);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
