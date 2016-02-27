package me.venj.geoquiz;

/**
 * Created by venj on 2016/1/26.
 */
public class Question {
    private int question;
    private boolean trueAnswer;

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public boolean isTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(boolean trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public Question(int question, boolean trueQuestion) {
        this.question = question;
        this.trueAnswer = trueQuestion;
    }
}
