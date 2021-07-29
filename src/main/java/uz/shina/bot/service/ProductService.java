package uz.shina.bot.service;

import org.springframework.stereotype.Service;
import uz.shina.bot.dto.ProductDto;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.Product;
import uz.shina.bot.payload.ApiResponse;
import uz.shina.bot.repository.CategoryRepository;
import uz.shina.bot.repository.ProductRepository;
import uz.shina.bot.util.MyConverter;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MyConverter converter;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, MyConverter converter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    public ApiResponse save(ProductDto dto){
        try {
            Optional<Category> category = categoryRepository.findById(dto.getCategory_id());
            if (!category.isPresent()) return converter.apiError();
            Product product=Product
                    .builder()
                    .active(dto.isActive())
                    .amount(dto.getAmount())
                    .name(dto.getName())
                    .number(dto.getNumber())
                    .category(category.get())
                    .build();

            Product save = productRepository.save(product);
            return converter.apiSuccess(save);
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }

    public ApiResponse edit(ProductDto dto){
        try {
            Optional<Product> product = productRepository.findById(dto.getId());
            if (!product.isPresent()) return converter.apiError();
            product.get().setAmount(dto.getAmount());
            product.get().setName(dto.getName());
            product.get().setNumber(dto.getNumber());
            product.get().setActive(dto.isActive());
            return converter.apiSuccess(productRepository.save(product.get()));
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }


    public ApiResponse delete(Integer id){
        try {
            return converter.apiSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }



    public ApiResponse findById(Integer id){
        try {
            return converter.apiSuccess(productRepository.findById(id));
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }



    public ApiResponse findAll(){
        try {
            List<Product> all = productRepository.findAll();
            return converter.apiSuccess(all);
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }

    public ApiResponse findAllByCategory(Integer category_id){
        try {
            Optional<Category> category = categoryRepository.findById(category_id);
            if (!category.isPresent()) return converter.apiError();
            List<Product> allByCategory = productRepository.findAllByCategory(category.get());
            return converter.apiSuccess(allByCategory);
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }
    }


}
