package uz.shina.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.shina.bot.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    boolean existsByName(String name);
    List<Category> findAllByOrderByIdAsc();
}
