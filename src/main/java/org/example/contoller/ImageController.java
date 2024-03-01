package org.example.contoller;

import org.example.dao.FileStorageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final FileStorageDao fileStorageDao;
    private final Path rootPath = Path.of(System.getProperty("user.home"), "/download");

    @Autowired
    public ImageController(FileStorageDao fileStorageDao) {
        this.fileStorageDao = fileStorageDao;
    }

    @GetMapping("/download/{filename:.+}")
    public HttpEntity<Resource> download(@PathVariable("filename") String filename) {
        var fileStorage = fileStorageDao.findByGeneratedName(filename);
        var resource = new FileSystemResource(rootPath.resolve(filename));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getSize())
                .body(resource);
    }
}
