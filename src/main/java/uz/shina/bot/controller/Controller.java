package uz.shina.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.PathVariable;
import uz.shina.bot.entity.FileStorage;
import uz.shina.bot.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URLEncoder;

@CrossOrigin
@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/")
    public String main(){
        return "category";
    }

    @GetMapping("/category")
    public String category(){
        return "category";
    }

    @GetMapping("/product")
    public String product(){
        return "product";
    }

    /** File **/
    @GetMapping("/photo/{hashId}")
    public HttpEntity<?> images(@PathVariable String hashId,
                                HttpServletRequest request) throws MalformedURLException {
        FileStorage fileStorage=fileStorageService.findByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline: filename\""+ URLEncoder.encode(fileStorage.getName()))
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getFileSize())
                .body(new FileUrlResource(fileStorage.getUploadPath()));
    }
}
