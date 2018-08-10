package com.example.shan.geoquiz;

public class Question {
    private int mTextResID;
    private boolean mAnswerTrue;
    private boolean mQuestionAnswered;

    public Question(int textResID, boolean answerTrue) {
        mTextResID = textResID;
        mAnswerTrue = answerTrue;
        mQuestionAnswered = false;
    }

    public int getTextResID() {
        return mTextResID;
    }

    public void setTextResID(int textResID) {
        mTextResID = textResID;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isQuestionAnswered() {
        return mQuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        mQuestionAnswered = questionAnswered;
    }
}
