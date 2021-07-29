package uz.shina.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.shina.bot.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    boolean existsByName(String name);
}
