package ru.leyman.loco.locoback.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.domain.errors.ContentException;
import ru.leyman.loco.locoback.service.FileService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class LocalFileService implements FileService {

    private final Path fileStorage;

    public LocalFileService(@Value("${filestore.local-store-path}") String storePath) {
        this.fileStorage = Paths.get(storePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorage);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored", e);
        }
    }

    @Override
    public Path create(String filename) {
        return this.fileStorage.resolve(filename).normalize();
    }

    @Override
    public Path upload(MultipartFile file, String filename) {
        Path filePath = this.fileStorage.resolve(filename).normalize();
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            log.error("Can't upload file", e);
            throw new ContentException("Не удалось сохранить изображение. Попробуйте позднее");
        }
    }

    @Override
    public Resource download(String filename) {
        Path filePath = Path.of(filename);
        try {
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (IOException e) {
            log.error("Can't download file {}", filename, e);
            throw new ContentException("Не удалось загрузить изображение. Попробуйте позднее");
        }
    }

    @Override
    public void delete(String filename) {
        Path filePath = Path.of(filename);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error on deleting file={}", filename, e);
        }
    }

}
