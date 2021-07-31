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
import java.util.Arrays;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("api")
@CrossOrigin
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

    @GetMapping("category/delete/{id}")
    public HttpEntity<?> deleteCategory(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.delete(id));
    }







    @GetMapping("product/all")
    public HttpEntity<?> allProduct(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("product/all/{id}")
    public HttpEntity<?> allProductByCategory(@PathVariable Integer id){
        return ResponseEntity.ok(productService.allProductByCategory(id));
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


    @GetMapping("product/delete/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable Integer id){
        return ResponseEntity.ok(productService.delete(id));
    }






    @GetMapping("images/all/{product_id}")
    public HttpEntity<?> findAllByProduct(@PathVariable("product_id") Integer product_id){
        return ResponseEntity.ok(fileStorageService.findAllByProduct(product_id));
    }


    @GetMapping("images/delete/{id}")
    public HttpEntity<?> deleteImage(@PathVariable("id") Integer id){
        return ResponseEntity.ok(fileStorageService.delete(id));
    }







    /** Fayllar uchun **/

    @PostMapping("upload/{id}")
    public HttpEntity<?> upload(@RequestBody MultipartFile[] files,@PathVariable Integer id){
//        String image=fileStorageService.save(multipartFile);
//        System.out.println(files.length);
//        System.out.println(files.length);
        System.out.println(Arrays.toString(files));
        return ResponseEntity.ok(fileStorageService.save(files,id));
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
