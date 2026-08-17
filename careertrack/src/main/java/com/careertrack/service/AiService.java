package com.careertrack.service;

import java.util.List;

public interface AiService {

    List<String> getInterviewTips(
            String jobRole, String companyName);

    List<String> generateMockQuestions(
            String jobRole, String companyName);
}