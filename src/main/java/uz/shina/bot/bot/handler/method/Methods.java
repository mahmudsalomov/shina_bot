package uz.shina.bot.bot.handler.method;

import com.example.mit.model.User;
import com.example.mit.model.dto.CategoryDto;
import com.example.mit.model.dto.ProductDto;
import com.example.mit.model.projection.ru.NameRuCategory;
import com.example.mit.model.projection.ru.NameRuProduct;
import com.example.mit.model.projection.uz_kril.NameOzCategory;
import com.example.mit.model.projection.uz_kril.NameOzProduct;
import com.example.mit.model.projection.uz_lat.NameUzCategory;
import com.example.mit.model.projection.uz_lat.NameUzProduct;
import com.example.mit.repository.BrandRepository;
import com.example.mit.repository.CategoryRepository;
import com.example.mit.repository.ProductRepository;
import com.example.mit.util.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Methods {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<CategoryDto> categoryForLang(Integer id, User user){
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        if (user.getLanguage().equals(Language.LANGUAGE_OZ)){
            List<NameUzCategory> childCategories = categoryRepository.findChildUzCategories(id);
            childCategories.forEach(category->{
                CategoryDto categoryDto=new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName_uz());
                categoryDto.setParent_id(category.getParent_id());
                categoryDto.setTemp_id(category.getTemp_id());
                categoryDtoList.add(categoryDto);
            });
            return categoryDtoList;
        }
        else if (user.getLanguage().equals(Language.LANGUAGE_RU)){
            List<NameRuCategory> childCategories = categoryRepository.findChildRuCategories(id);
            childCategories.forEach(category->{
                CategoryDto categoryDto=new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName_ru());
                categoryDto.setParent_id(category.getParent_id());
                categoryDto.setTemp_id(category.getTemp_id());
                categoryDtoList.add(categoryDto);
            });
            return categoryDtoList;
        }
        else {
            List<NameOzCategory> childCategories = categoryRepository.findChildOzCategories(id);
            childCategories.forEach(category->{
                CategoryDto categoryDto=new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName_oz());
                categoryDto.setParent_id(category.getParent_id());
                categoryDto.setTemp_id(category.getTemp_id());
                categoryDtoList.add(categoryDto);
            });
            return categoryDtoList;
        }
    }

    public List<ProductDto> productForLang(Long id, User user){
        List<ProductDto> productDtoList=new ArrayList<>();
        if (user.getLanguage().equals(Language.LANGUAGE_OZ)){
            String action = user.getAction();
            Integer cat_id=Integer.valueOf(action.substring(action.lastIndexOf("c[")+1,action.indexOf("-b[")));
            List<NameUzProduct> nameUzProducts = productRepository.allUzByBrandId(id,cat_id);
            nameUzProducts.forEach(nameUzProduct -> {
                ProductDto productDto=new ProductDto();
                productDto.setName(nameUzProduct.getName_uz());
                productDto.setCategoryId(nameUzProduct.getCategory_id());
                productDto.setBrandId(nameUzProduct.getBrand_id());
                productDto.setCost(nameUzProduct.getCost());
                productDto.setDescription(nameUzProduct.getDescription_uz());
                productDto.setPrice(nameUzProduct.getPrice());
                productDto.setQuantity(nameUzProduct.getQuantity());
                productDto.setId(nameUzProduct.getId());
                productDto.setMainImage(nameUzProduct.getMain_image());
                productDto.setSlug(nameUzProduct.getSlug());
                productDto.setStatus(nameUzProduct.getStatus());
                productDtoList.add(productDto);
            });
            return productDtoList;
        }
        else if (user.getLanguage().equals(Language.LANGUAGE_RU)){
            String action = user.getAction();
            Integer cat_id=Integer.valueOf(action.substring(action.lastIndexOf("c[")+1,action.indexOf("-b[")));
            List<NameRuProduct> nameUzProducts = productRepository.allRuByBrandId(id,cat_id);
            nameUzProducts.forEach(nameUzProduct -> {
                ProductDto productDto=new ProductDto();
                productDto.setName(nameUzProduct.getNameRu());
                productDto.setCategoryId(nameUzProduct.getCategoryId());
                productDto.setBrandId(nameUzProduct.getBrandId());
                productDto.setCost(nameUzProduct.getCost());
                productDto.setDescription(nameUzProduct.getDescriptionRu());
                productDto.setPrice(nameUzProduct.getPrice());
                productDto.setQuantity(nameUzProduct.getQuantity());
                productDto.setId(nameUzProduct.getId());
                productDto.setMainImage(nameUzProduct.getMainImage());
                productDto.setSlug(nameUzProduct.getSlug());
                productDto.setStatus(nameUzProduct.getStatus());
                productDtoList.add(productDto);
            });
            return productDtoList;
        }
        else {
            String action = user.getAction();
            Integer cat_id=Integer.valueOf(action.substring(action.lastIndexOf("c[")+1,action.indexOf("-b[")));
            List<NameOzProduct> nameUzProducts = productRepository.allOzByBrandId(id,cat_id);
            nameUzProducts.forEach(nameUzProduct -> {
                ProductDto productDto=new ProductDto();
                productDto.setName(nameUzProduct.getName_oz());
                productDto.setCategoryId(nameUzProduct.getCategory_id());
                productDto.setBrandId(nameUzProduct.getBrand_id());
                productDto.setCost(nameUzProduct.getCost());
                productDto.setDescription(nameUzProduct.getDescription_oz());
                productDto.setPrice(nameUzProduct.getPrice());
                productDto.setQuantity(nameUzProduct.getQuantity());
                productDto.setId(nameUzProduct.getId());
                productDto.setMainImage(nameUzProduct.getMain_image());
                productDto.setSlug(nameUzProduct.getSlug());
                productDto.setStatus(nameUzProduct.getStatus());
                productDtoList.add(productDto);
            });
            return productDtoList;
        }
    }

    public ProductDto productForLang(Long id,String lang){
        return null;
    }
}
