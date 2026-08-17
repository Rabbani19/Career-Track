package com.careertrack.service;

import com.careertrack.dto.DocumentDTO;
import com.careertrack.enums.DocumentType;
import com.careertrack.model.Document;
import com.careertrack.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DocumentService {

    Document uploadDocument(
            DocumentDTO dto, User user) throws IOException;

    List<Document> getAllDocuments(User user);

    List<Document> getDocumentsByType(
            User user, DocumentType type);

    Document getDocumentById(Long id, User user);

    void deleteDocument(
            Long id, User user) throws IOException;

    Map<String, Long> getDocumentStats(User user);
}