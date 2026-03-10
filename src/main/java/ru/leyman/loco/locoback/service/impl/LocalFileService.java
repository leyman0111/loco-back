package ru.leyman.loco.locoback.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.service.FileService;
import ru.leyman.loco.locoback.utils.FilenameUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileService implements FileService {

    @Value("${filestore.local-store-path}")
    private String storePath;

    @Override
    public String upload(MultipartFile file) {
        var ext = FilenameUtil.getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Path.of(getFilename(ext));
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Can't upload file", e);
            throw new RuntimeException("Error on saving file: " + e);
        }
    }

    @Override
    public Resource download(String filename) throws FileNotFoundException {
        Path filePath = Path.of(filename);
        try {
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            log.error("Can't download file {}", filename, e);
            return null;
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

    private String getFilename(String extension) {
        return String.format(storePath + "%s.%s", UUID.randomUUID(),
                Objects.requireNonNullElse(extension, "txt"));
    }

}
