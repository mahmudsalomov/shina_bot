package uz.shina.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.shina.bot.entity.FileStorage;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage,Integer> {

    FileStorage findByHashId(String hashId);
}
