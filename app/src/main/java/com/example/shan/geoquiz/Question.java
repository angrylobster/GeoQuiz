package com.example.shan.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{
    private int mTextResID;
    private boolean mAnswerTrue;
    private boolean mQuestionAnswered;

    public Question(int textResID, boolean answerTrue) {
        mTextResID = textResID;
        mAnswerTrue = answerTrue;
        mQuestionAnswered = false;
    }

    protected Question(Parcel in) {
        mTextResID = in.readInt();
        mAnswerTrue = in.readByte() != 0;
        mQuestionAnswered = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mTextResID);
        parcel.writeByte((byte) (mAnswerTrue ? 1 : 0));
        parcel.writeByte((byte) (mQuestionAnswered ? 1 : 0));
    }
}
