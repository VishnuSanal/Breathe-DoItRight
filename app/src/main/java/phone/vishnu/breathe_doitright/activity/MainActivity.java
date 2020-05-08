package phone.vishnu.breathe_doitright.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.MessageFormat;

import phone.vishnu.breathe_doitright.R;
import phone.vishnu.breathe_doitright.fragment.EditFragment;
import phone.vishnu.breathe_doitright.util.Prefs;

public class MainActivity extends AppCompatActivity {

    public static int DURATION = 5000;
    private ImageView imageView;
    private TextView guideTv;
    private TextView minUpdateTv;
    private TextView breathsTv;
    private TextView timeTv;
    private TextView sessionTv;
    private TextView startButton;
    private TextView stopButton;
    private ViewAnimator animator;
    private Prefs prefs;
    private volatile boolean isRunning = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        breathsTv = findViewById(R.id.breathsTodayTv);
        timeTv = findViewById(R.id.lastSessionTv);
        sessionTv = findViewById(R.id.todayMinutesTv);
        guideTv = findViewById(R.id.guideTv);
        minUpdateTv = findViewById(R.id.oneMinuteTv);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        prefs = new Prefs(this);
        startIntroAnimation();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning)
                    startAnimation();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
            }
        });

        breathsTv.setText(MessageFormat.format("{0} Breaths", prefs.getBreaths()));
        timeTv.setText(prefs.getDate());
        sessionTv.setText(MessageFormat.format("{0} min today", prefs.getSessions()));
    }

    private void stopAnimation() {
        isRunning = false;
        stopButton.setVisibility(View.GONE);
        imageView.setScaleX(1.0f);
        imageView.setScaleY(1.0f);
        minUpdateTv.setText(("00 : 00"));
        countDownTimer.cancel();
        animator.cancel();
    }

    private void startIntroAnimation() {
        ViewAnimator
                .animate(guideTv)
                .scale(0, 1)
                .duration(2000)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTv.setText(getResources().getString(R.string.breathe));
                    }
                })
                .start()
                .thenAnimate(guideTv)
                .scale(1.0f, 1.4f, 1.0f);
    }

    private void startAnimation() {

        animator = ViewAnimator
                .animate(imageView)
                .alpha(0, 1)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        isRunning = true;
                        guideTv.setText(getResources().getString(R.string.inhale_exhale));
                        stopButton.setVisibility(View.VISIBLE);

                        countDownTimer = new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                minUpdateTv.setText(("00 : " + millisUntilFinished / 1000));
//                                Log.e("vishnu", "onTick: " + millisUntilFinished);
                                if (millisUntilFinished < 1000) {
                                    prefs.setBreaths(prefs.getBreaths() + 1);
                                    prefs.setDate(System.currentTimeMillis());
                                    prefs.setSessions(prefs.getSessions() + 1);

                                    isRunning = false;
                                    stopButton.setVisibility(View.GONE);

                                    recreate();
                                }
                            }

                            @Override
                            public void onFinish() {
                                isRunning = false;
                                stopButton.setVisibility(View.GONE);

                                minUpdateTv.setText(("00 : 00"));
                                guideTv.setText(getResources().getString(R.string.good_job));

                                imageView.setScaleX(1.0f);
                                imageView.setScaleY(1.0f);

                        recreate();
                            }
                        }.start();

                    }
                })
                .decelerate()
                .duration(1000)
                .thenAnimate(imageView)
                .scale(0.01f, 1.5f, 0.01f)
                .rotation(360)
                .repeatCount(11)
                .duration(DURATION)
                .start();
    }

    public void editButtonClicked(View view) {
        EditFragment fragment = EditFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.constraintLayout, fragment)
                .addToBackStack(null)
                .commit();
    }
}

