package com.enrique7mc.braintrainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import codetail.graphics.drawables.DrawableHotspotTouch;
import codetail.graphics.drawables.LollipopDrawable;
import codetail.graphics.drawables.LollipopDrawablesCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Game game = new Game();
    private boolean blocked = true;

    RelativeLayout layout;
    Typeface typeface;
    android.support.v7.widget.GridLayout grid;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView timeTextView;
    TextView operationTextView;
    TextView resultTextView;
    TextView scoreTextView;
    Button newButton;

    long startTime = 0;
    private static final int INIT_SECONDS = 20;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            seconds = INIT_SECONDS - seconds;

            setTimerText(seconds);
            if(seconds == 0) {
                restart();
                return;
            }

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.container);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");
        overrideFonts(layout);
        grid = (android.support.v7.widget.GridLayout) findViewById(R.id.grid);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView1.setOnClickListener(clickListener);
        textView2.setOnClickListener(clickListener);
        textView3.setOnClickListener(clickListener);
        textView4.setOnClickListener(clickListener);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        operationTextView = (TextView) findViewById(R.id.operationTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(gameListener);
        setTimerText(INIT_SECONDS);
        resultTextView.setVisibility(View.INVISIBLE);

        setAnswers(game.generateNextOperation());


        textView1.setBackgroundDrawable(getDrawable2(R.drawable.ripple));
        textView1.setClickable(true);// if we don't set it true, ripple will not be played
        textView1.setOnTouchListener(
                new DrawableHotspotTouch((LollipopDrawable) textView1.getBackground()));
    }

    public Drawable getDrawable2(int id){
        return LollipopDrawablesCompat.getDrawable(getResources(), id, getTheme());
    }

    private void restart() {
        timerHandler.removeCallbacks(timerRunnable);
        newButton.setVisibility(View.VISIBLE);
        newButton.setText("New Game");
        resultTextView.setText("Finished!");
        blocked = true;
        game = new Game();
        setAnswers(game.generateNextOperation());
    }

    private void setTimerText(int seconds) {
        timeTextView.setText(String.format("%02ds", seconds));
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (blocked) {
                return;
            }

            int answer = Integer.parseInt(((TextView)v).getText().toString());
            if (game.isCorrectAnswer(answer)) {
                resultTextView.setText("Correct!");
            } else {
                resultTextView.setText("Wrong!");
            }

            new LongOperation().execute();
        }
    };

    View.OnClickListener gameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (newButton.getText().equals("New Game")) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                blocked = false;
                scoreTextView.setText("0/0");
                resultTextView.setText("");
                resultTextView.setVisibility(View.VISIBLE);
                hideView(newButton);
                newButton.setText("");
            } else {
                timerHandler.removeCallbacks(timerRunnable);
                blocked = true;
                resultTextView.setVisibility(View.INVISIBLE);
                showView(newButton);
                newButton.setText("New Game");
            }
        }
    };

    private void setAnswers(Operation op) {
        operationTextView.setText(op.toString());
        Integer[] answers = op.generateAnswers();

        textView1.setText(String.valueOf(answers[0]));
        textView2.setText(String.valueOf(answers[1]));
        textView3.setText(String.valueOf(answers[2]));
        textView4.setText(String.valueOf(answers[3]));
    }

    private void overrideFonts(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(typeface);
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    private class LongOperation extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            game.generateNextOperation();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            setAnswers(game.getCurrentOperation());
            blocked = false;
            scoreTextView.setText(String.format("%d/%d",
                    game.getCorrectAnwers(), game.getTotalQuestions() - 1));
        }

        @Override
        protected void onPreExecute() {
            blocked = true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void showView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            view.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideView(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
