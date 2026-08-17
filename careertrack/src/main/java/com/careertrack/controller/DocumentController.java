package com.careertrack.controller;

import com.careertrack.dto.DocumentDTO;
import com.careertrack.enums.DocumentType;
import com.careertrack.model.Document;
import com.careertrack.model.User;
import com.careertrack.service.DocumentService;
import com.careertrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping
    public String listDocuments(
            Authentication authentication, Model model) {

        User user = userService.getCurrentUser(
                authentication.getName());

        model.addAttribute("documents",
                documentService.getAllDocuments(user));
        model.addAttribute("stats",
                documentService.getDocumentStats(user));

        return "documents/documents";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("documentDTO", new DocumentDTO());
        model.addAttribute("documentTypes",
                DocumentType.values());
        return "documents/upload-document";
    }

    @PostMapping("/upload")
    public String uploadDocument(
            @ModelAttribute DocumentDTO documentDTO,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        try {
            documentService.uploadDocument(documentDTO, user);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Document uploaded successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Upload failed: " + e.getMessage());
        }

        return "redirect:/documents";
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            Authentication authentication) throws IOException {

        User user = userService.getCurrentUser(
                authentication.getName());

        Document document =
                documentService.getDocumentById(id, user);

        Path filePath = Paths.get(uploadDir)
                .resolve(document.getFilePath())
                .normalize();

        Resource resource =
                new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException(
                    "File not found on server!");
        }

        String contentType = document.getFileType() != null
                ? document.getFileType()
                : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + document.getDocumentName() + "\"")
                .body(resource);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long id,
            Authentication authentication) throws IOException {

        User user = userService.getCurrentUser(
                authentication.getName());

        Document document =
                documentService.getDocumentById(id, user);

        Path filePath = Paths.get(uploadDir)
                .resolve(document.getFilePath())
                .normalize();

        Resource resource =
                new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException(
                    "File not found on server!");
        }

        String contentType = document.getFileType() != null
                ? document.getFileType()
                : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + document.getDocumentName() + "\"")
                .body(resource);
    }


    @PostMapping("/{id}/delete")
    public String deleteDocument(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser(
                authentication.getName());

        try {
            documentService.deleteDocument(id, user);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Document deleted!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete failed: " + e.getMessage());
        }

        return "redirect:/documents";
    }
}