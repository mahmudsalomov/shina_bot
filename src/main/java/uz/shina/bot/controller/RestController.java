package uz.shina.bot.controller;

import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.shina.bot.dto.ProductDto;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.FileStorage;
import uz.shina.bot.entity.Product;
import uz.shina.bot.service.CategoryService;
import uz.shina.bot.service.FileStorageService;
import uz.shina.bot.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URLEncoder;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("api")
public class RestController {
    private final FileStorageService fileStorageService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public RestController(FileStorageService fileStorageService, ProductService productService, CategoryService categoryService) {
        this.fileStorageService = fileStorageService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("category/all")
    public HttpEntity<?> allCategory(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("category/{id}")
    public HttpEntity<?> oneCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping("category/add")
    public HttpEntity<?> addCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PutMapping("category/edit")
    public HttpEntity<?> editCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.edit(category));
    }







    @GetMapping("product/all")
    public HttpEntity<?> allProduct(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("product/{id}")
    public HttpEntity<?> oneProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("product/add")
    public HttpEntity<?> addProduct(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.save(dto));
    }

    @PutMapping("product/edit")
    public HttpEntity<?> editProduct(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.edit(dto));
    }










    /** Fayllar uchun **/

    @PostMapping("upload")
    public HttpEntity<?> upload(@RequestBody MultipartFile[] files){
//        String image=fileStorageService.save(multipartFile);
//        System.out.println(files.length);
        return ResponseEntity.ok(fileStorageService.save(files));
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
