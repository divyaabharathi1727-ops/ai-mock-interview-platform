package com.mockinterview.backend.dto;

public class InterviewSessionResponse {

    private QuestionResponse currentQuestion;
    private boolean readyToFinish;
    private int answeredQuestions;
    private int totalQuestions;

    public InterviewSessionResponse() { }

    public InterviewSessionResponse(QuestionResponse currentQuestion, boolean readyToFinish,
                                    int answeredQuestions, int totalQuestions) {
        this.currentQuestion = currentQuestion;
        this.readyToFinish = readyToFinish;
        this.answeredQuestions = answeredQuestions;
        this.totalQuestions = totalQuestions;
    }

    public QuestionResponse getCurrentQuestion() { return currentQuestion; }
    public boolean isReadyToFinish() { return readyToFinish; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public int getTotalQuestions() { return totalQuestions; }
}
