package com.p3springboot.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
Permet la gestion des fichiers des images uploadées pour les locations. 
Gère la création du dossier d'upload et le stockage des fichiers.
*/
@Service
public class FileStorageService {

    private final Path uploadPath = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String saveFile(MultipartFile file ) {
        if(file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        try {
            Path destination = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:3001/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error during file storage.", e);
        }
    }
}
