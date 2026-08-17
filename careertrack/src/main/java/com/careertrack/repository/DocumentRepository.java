package com.careertrack.repository;

import com.careertrack.enums.DocumentType;
import com.careertrack.model.Document;
import com.careertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    List<Document> findByUser(User user);

    List<Document> findByUserAndDocumentType(
            User user, DocumentType documentType);

    Optional<Document> findByIdAndUser(Long id, User user);
}