package uz.shina.bot.service;

import org.springframework.stereotype.Service;
import uz.shina.bot.dto.ProductDto;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.Product;
import uz.shina.bot.payload.ApiResponse;
import uz.shina.bot.repository.CategoryRepository;
import uz.shina.bot.repository.ProductRepository;
import uz.shina.bot.util.MyConverter;

import java.util.Optional;

@Service
public class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MyConverter converter;

    public CategoryService(ProductRepository productRepository, CategoryRepository categoryRepository, MyConverter converter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    public ApiResponse save(Category category){
        try {
            Category save = categoryRepository.save(category);
            return converter.apiSuccess(save);
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }

    public ApiResponse edit(Category category){
        try {
            return converter.apiSuccess(categoryRepository.save(category));
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }


    public ApiResponse findById(Integer id){
        try {
            return converter.apiSuccess(categoryRepository.findById(id));
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }


    public ApiResponse findAll(){
        try {
            return converter.apiSuccess(categoryRepository.findAllByOrderByIdAsc());
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }


    public ApiResponse delete(Integer id) {
        try {
            categoryRepository.deleteById(id);
            return converter.apiSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }
}
