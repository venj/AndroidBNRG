package me.venj.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "me.venj.GeoQuiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "me.venj.GeoQuiz.answer_shown";
    private static final String KEY_INDEX = "CHEAT_KEY_INDEX";
    private static final String TAG = "CHEAT_ACTIVITY";

    private TextView answerTextView;
    private TextView apiLevelTextView;
    private Button showAnswerButton;
    private boolean answerIsTrue;
    private boolean isAnswerShown;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent intent) {
        return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(KEY_INDEX, false);
            Log.d(TAG, isAnswerShown ? "shown" : "not shown");
        }
        else {
            isAnswerShown = false;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        answerTextView = (TextView)findViewById(R.id.answer_text_view);

        if (isAnswerShown) {
            setAnswerShownResult();
            answerTextView.setText(answerIsTrue ? R.string.true_button : R.string.false_button);
        }

        showAnswerButton = (Button)findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener((view) -> {
            answerTextView.setText(answerIsTrue ? R.string.true_button : R.string.false_button);
            isAnswerShown = true;
            setAnswerShownResult();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int cx = showAnswerButton.getWidth() / 2;
                int cy = showAnswerButton.getHeight() / 2;
                float radius = showAnswerButton.getWidth() / 2;
                Animator anim = ViewAnimationUtils.createCircularReveal(showAnswerButton, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        answerTextView.setVisibility(View.VISIBLE);
                        showAnswerButton.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            }
            else {
                answerTextView.setVisibility(View.VISIBLE);
                showAnswerButton.setVisibility(View.INVISIBLE);
            }
        });

        apiLevelTextView = (TextView)findViewById(R.id.api_level_text_view);
        apiLevelTextView.setText(String.format(getResources().getString(R.string.api_level_text), Build.VERSION.SDK_INT));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_INDEX, isAnswerShown);
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
