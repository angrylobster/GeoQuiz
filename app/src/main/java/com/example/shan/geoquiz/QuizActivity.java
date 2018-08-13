package com.example.shan.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String QUESTION_ANSWERED_INDEX = "question_answered_index";
    private static final String SCORE_INDEX = "score_index";
    private static final String CHEAT_INDEX = "cheat_index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private int mScore = 0;
    private int mCheatCounter = 3;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_afghanistan, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionBank = (Question[]) savedInstanceState.getParcelableArray(QUESTION_ANSWERED_INDEX);
            mScore = savedInstanceState.getInt(SCORE_INDEX, 0);
            mCheatCounter = savedInstanceState.getInt(CHEAT_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                previousQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                nextQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        if (mCheatCounter <= 0){
            mCheatButton.setEnabled(false);
        }
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mQuestionBank[mCurrentIndex].setUserCheated(true);
            mCheatCounter--;
        }
        if (mCheatCounter <= 0){
            mCheatButton.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putParcelableArray(QUESTION_ANSWERED_INDEX, mQuestionBank);
        savedInstanceState.putInt(SCORE_INDEX, mScore);
        savedInstanceState.putInt(CHEAT_INDEX, mCheatCounter);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void previousQuestion(){
        if (mCurrentIndex == 0){
            mCurrentIndex = mQuestionBank.length - 1;
        } else {
            mCurrentIndex--;
        }
        updateQuestion();
    }

    private void nextQuestion(){
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResID();
        mQuestionTextView.setText(question);
        if (mQuestionBank[mCurrentIndex].isQuestionAnswered()){
            disableAnswerButtons();
        } else {
            enableAnswerButtons();
        }
    }

    private void disableAnswerButtons(){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void enableAnswerButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userAnswer){
        boolean answerToQuestion = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResID = 0;

        if (mQuestionBank[mCurrentIndex].userCheated()){
            messageResID = R.string.judgement_toast;
        } else {
            if (userAnswer == answerToQuestion) {
                mScore++;
                messageResID = R.string.correct_toast;
            } else {
                messageResID = R.string.incorrect_toast;
            }
        }

        Toast.makeText(QuizActivity.this, messageResID, Toast.LENGTH_SHORT).show();

        mQuestionBank[mCurrentIndex].setQuestionAnswered(true);
        disableAnswerButtons();

        if (allQuestionsAnswered()){
            int percentageCorrect = (int) (((double)mScore / (double) mQuestionBank.length) * 100d);
            Toast.makeText(QuizActivity.this,
                    "Congratulations! You scored " + percentageCorrect + "% on the quiz!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean allQuestionsAnswered(){
        for (Question question : mQuestionBank){
            if (!question.isQuestionAnswered()) {
                return false;
            }
        }
        return true;
    }
}
