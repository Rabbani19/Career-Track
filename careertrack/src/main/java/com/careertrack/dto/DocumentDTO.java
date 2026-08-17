package com.careertrack.dto;

import com.careertrack.enums.DocumentType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentDTO {

    private DocumentType documentType;
    private String description;
    private MultipartFile file;
}