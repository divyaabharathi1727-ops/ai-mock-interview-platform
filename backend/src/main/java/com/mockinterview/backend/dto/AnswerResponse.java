package com.mockinterview.backend.dto;

public class AnswerResponse {

    private QuestionResponse nextQuestion;
    private boolean readyToFinish;
    private int answeredQuestions;
    private int totalQuestions;

    public AnswerResponse() { }

    public AnswerResponse(QuestionResponse nextQuestion, boolean readyToFinish,
                          int answeredQuestions, int totalQuestions) {
        this.nextQuestion = nextQuestion;
        this.readyToFinish = readyToFinish;
        this.answeredQuestions = answeredQuestions;
        this.totalQuestions = totalQuestions;
    }

    public QuestionResponse getNextQuestion() { return nextQuestion; }
    public boolean isReadyToFinish() { return readyToFinish; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public int getTotalQuestions() { return totalQuestions; }
}
