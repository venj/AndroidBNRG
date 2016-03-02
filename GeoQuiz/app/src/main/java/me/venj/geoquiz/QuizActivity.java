package me.venj.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private ImageButton mNextImageButton;
    private ImageButton mPrevImageButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;
    private boolean isQuestionAnswered = false;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ISCHEAT = "is_cheat";
    private static final String KEY_ISANSWERED = "is_answered";
    private static final int REQUEST_CODE_CHEAT = 0;

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
        isQuestionAnswered = false;
        if (mNextImageButton != null) {
            mNextImageButton.setEnabled(false);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueAnswer();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.true_toast;
            } else {
                messageResId = R.string.false_toast;
            }
        }
        isQuestionAnswered = true;
        mNextImageButton.setEnabled(true);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(bundle) called.");
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener((view) -> {
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
            Toast.makeText(QuizActivity.this, R.string.toast_string, Toast.LENGTH_SHORT).show();
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener((view) -> {
            checkAnswer(true);
        });

        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener((view) -> {
            checkAnswer(false);
        });

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener((view) -> {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            mCurrentIndex--;
            updateQuestion();
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEAT, false);
            isQuestionAnswered = savedInstanceState.getBoolean(KEY_ISANSWERED, false);
        }

        //mNextButton = (Button)findViewById(R.id.next_button);
        mNextImageButton = (ImageButton)findViewById(R.id.next_button);
        mNextImageButton.setOnClickListener((view) -> {
            // Disable question rotation;
            if (mCurrentIndex + 1 == mQuestionBank.length) {
                Toast.makeText(QuizActivity.this, R.string.toast_already_last, Toast.LENGTH_SHORT).show();
                return;
            }
            //mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            mCurrentIndex++;
            mIsCheater = false;
            updateQuestion();
        });

        if (!isQuestionAnswered) {
            mNextImageButton.setEnabled(false);
        }

        //mPrevButton = (Button)findViewById(R.id.previous_button);
        mPrevImageButton = (ImageButton)findViewById(R.id.previous_button);
        mPrevImageButton.setOnClickListener((view) -> {
            // Disable question rotation;
            if (mCurrentIndex == 0) {
                Toast.makeText(QuizActivity.this, R.string.toast_already_first, Toast.LENGTH_SHORT).show();
                return;
            }
            mCurrentIndex--;
            //mCurrentIndex = mCurrentIndex - 1 >= 0 ? mCurrentIndex - 1 : mQuestionBank.length - (mCurrentIndex + 1);
            mIsCheater = false;
            updateQuestion();
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener((view) -> {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueAnswer();
            Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
            //startActivity(i);
            startActivityForResult(i, REQUEST_CODE_CHEAT);
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "------- Activity Call back");
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_ISCHEAT, mIsCheater);
        savedInstanceState.putBoolean(KEY_ISANSWERED, isQuestionAnswered);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }
}
