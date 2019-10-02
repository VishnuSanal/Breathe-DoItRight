package phone.vishnu.breathe_doitright;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.MessageFormat;

import util.Prefs;

public class MainActivity extends AppCompatActivity {

    static int DURATION = 5000;
    private ImageView imageView;
    private TextView guideTv;
    private TextView minUpdateTv;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        TextView breathsTv = findViewById(R.id.breathsTodayTv);
        TextView timeTv = findViewById(R.id.lastSessionTv);
        TextView sessionTv = findViewById(R.id.todayMinutesTv);

        guideTv = findViewById(R.id.guideTv);
        minUpdateTv = findViewById(R.id.oneMinuteTv);
        startIntroAnimation();
        prefs = new Prefs(this);
        TextView startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        breathsTv.setText(MessageFormat.format("{0} Breaths", prefs.getBreaths()));
        timeTv.setText(prefs.getDate());
        sessionTv.setText(MessageFormat.format("{0} min today", prefs.getSessions()));
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

        ViewAnimator
                .animate(imageView)
                .alpha(0, 1)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTv.setText(getResources().getString(R.string.inhale_exhale));
                        new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                minUpdateTv.setText(("00 : " + millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {

                            }
                        }.start();

                    }
                })
                .decelerate()
                .duration(1000)
                .thenAnimate(imageView)
                .scale(0.01f, 1.5f, 0.01f)
                .rotation(360)
                .repeatCount(10)
                .duration(DURATION)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        guideTv.setText(getResources().getString(R.string.good_job));
                        imageView.setScaleX(1.0f);
                        imageView.setScaleY(1.0f);

                        prefs.setBreaths(prefs.getBreaths() + 1);
                        prefs.setDate(System.currentTimeMillis());

                        prefs.setSessions(prefs.getSessions() + 1);

                        new CountDownTimer(1000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }.start();
                    }
                })
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

