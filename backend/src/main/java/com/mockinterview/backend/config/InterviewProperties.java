package com.mockinterview.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "interview")
public class InterviewProperties {

    private int defaultQuestionCount = 20;

    public int getDefaultQuestionCount() { return defaultQuestionCount; }
    public void setDefaultQuestionCount(int defaultQuestionCount) { this.defaultQuestionCount = defaultQuestionCount; }
}
