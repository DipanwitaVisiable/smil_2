package com.visiabletech.smilmobileapp.Pogo;


import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

//@Entity(tableName = "questions", primaryKeys = {"question_id", "quiz_id" })
public class QuestionTable implements Serializable {

    @SerializedName("question_id")
    @Expose
//    @ColumnInfo(name = "question_id")
//    @NonNull
    private String questionId;

    //Local usage
//    @ColumnInfo(name = "quiz_id")
//    @NonNull
    @SerializedName("quiz_id")
    @Expose
    private String quizId;
    //Local usage


    @SerializedName("directions")
    @Expose
//    @ColumnInfo(name = "directions")
    private String directions;

    @SerializedName("question")
    @Expose
//    @ColumnInfo(name = "question")
    private String question;

//    @Ignore
    @SerializedName("ans_arr")
    @Expose
    private ArrayList<AnswerTable> answerDetails;

    //For local purpose
    @SerializedName("is_marked")
    @Expose
//    @ColumnInfo(name = "is_marked")
    private boolean isMarked;

    @SerializedName("is_visited")
    @Expose
//    @ColumnInfo(name = "is_visited")
    private boolean isVisited;

    @SerializedName("is_answered")
    @Expose
//    @ColumnInfo(name = "is_answered")
    private boolean isAnswered;

    @SerializedName("exam_taken_id")
    @Expose
    private boolean exam_taken_id;

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    @NonNull
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(@NonNull String quizId) {
        this.quizId = quizId;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<AnswerTable> getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(ArrayList<AnswerTable> answerDetails) {
        this.answerDetails = answerDetails;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public boolean isExam_taken_id() {
        return exam_taken_id;
    }

    public void setExam_taken_id(boolean exam_taken_id) {
        this.exam_taken_id = exam_taken_id;
    }
}
