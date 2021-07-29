package uz.shina.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findAllByCategory(Category category);
}
