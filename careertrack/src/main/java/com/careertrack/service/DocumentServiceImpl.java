package com.careertrack.service;

import com.careertrack.dto.DocumentDTO;
import com.careertrack.enums.DocumentType;
import com.careertrack.model.Document;
import com.careertrack.model.User;
import com.careertrack.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DocumentServiceImpl
        implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Override
    public Document uploadDocument(
            DocumentDTO dto, User user) throws IOException {

        MultipartFile file = dto.getFile();

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Please select a file to upload!");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null
                && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf("."));
        }

        // Unique name on disk so uploads never overwrite each other
        String uniqueFilename = UUID.randomUUID() + extension;

        Path targetPath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath,
                StandardCopyOption.REPLACE_EXISTING);

        Document document = Document.builder()
                .documentName(originalFilename)
                .documentType(dto.getDocumentType())
                .filePath(uniqueFilename)
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .description(dto.getDescription())
                .user(user)
                .build();

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocuments(User user) {
        return documentRepository.findByUser(user);
    }

    @Override
    public List<Document> getDocumentsByType(
            User user, DocumentType type) {
        return documentRepository
                .findByUserAndDocumentType(user, type);
    }

    @Override
    public Document getDocumentById(Long id, User user) {
        return documentRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException(
                        "Document not found or access denied!"));
    }

    @Override
    public void deleteDocument(
            Long id, User user) throws IOException {

        Document document = getDocumentById(id, user);

        Path filePath = Paths.get(uploadDir)
                .resolve(document.getFilePath());
        Files.deleteIfExists(filePath);

        documentRepository.delete(document);
    }

    @Override
    public Map<String, Long> getDocumentStats(User user) {

        List<Document> documents = getAllDocuments(user);

        Map<String, Long> stats = new HashMap<>();

        stats.put("total", (long) documents.size());

        stats.put("resumes", documents.stream()
                .filter(d -> d.getDocumentType()
                        == DocumentType.RESUME)
                .count());

        stats.put("coverLetters", documents.stream()
                .filter(d -> d.getDocumentType()
                        == DocumentType.COVER_LETTER)
                .count());

        stats.put("others", documents.stream()
                .filter(d -> d.getDocumentType()
                        != DocumentType.RESUME
                        && d.getDocumentType()
                        != DocumentType.COVER_LETTER)
                .count());

        return stats;
    }
}