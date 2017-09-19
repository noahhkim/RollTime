package com.noahkim.rolltime.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.noahkim.rolltime.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noahkim on 9/13/17.
 */

public class TimerFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.progressBarCircle)
    ProgressBar mProgressBarCircle;
    @BindView(R.id.editTextMinute)
    EditText mEditTextMinute;
    @BindView(R.id.textViewTime)
    TextView mTextViewTime;
    @BindView(R.id.imageViewReset)
    ImageView mImageViewReset;
    @BindView(R.id.imageViewStartStop)
    ImageView mImageViewSartStop;

    private long timeCountInMilliSeconds = 60000;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private CountDownTimer mCountDownTimer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this, rootView);

        initListeners();

        return rootView;
    }

    // Method to initialize the click listeners
    private void initListeners() {
        mImageViewReset.setOnClickListener(this);
        mImageViewSartStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
        }
    }

    // Method to reset countdown timer
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }

    // Method to start and stop countdown timer
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues();
            setProgressBarValues();
            mImageViewReset.setVisibility(View.VISIBLE);
            mImageViewSartStop.setImageResource(R.drawable.icon_stop);
            mEditTextMinute.setEnabled(false);
            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();
        } else {
            mImageViewReset.setVisibility(View.GONE);
            mImageViewSartStop.setImageResource(R.drawable.icon_start);
            mEditTextMinute.setEnabled(true);
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }

    // Method to initialize values for countdown timer
    private void setTimerValues() {
        int time = 0;
        if (!mEditTextMinute.getText().toString().isEmpty()) {
            // Fetching value from edit text and type cast to integer
            time = Integer.parseInt(mEditTextMinute.getText().toString().trim());
        } else {
            Toast.makeText(getContext(), getString(R.string.message_minutes),
                    Toast.LENGTH_SHORT).show();
        }
        timeCountInMilliSeconds = time * 60000;
    }

    // Method to start countdown timer
    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTextViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                mProgressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mTextViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                mImageViewReset.setVisibility(View.GONE);
                mImageViewSartStop.setImageResource(R.drawable.icon_start);
                mEditTextMinute.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
            }
        }.start();
        mCountDownTimer.start();
    }

    // Method to stop countdown timer
    private void stopCountDownTimer() {
            mCountDownTimer.cancel();
    }

    // Method to set circular progress bar values
    private void setProgressBarValues() {
        mProgressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        mProgressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    /**
     * Method to convert millisecond to time format
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }
}
