package uz.shina.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uz.shina.bot.util.MyConverter;

@Service
public class FileStorageService {

    @Autowired
    private MyConverter converter;

    private final org.hashids.Hashids hashids;
    public FileStorageService() {
        this.hashids = new org.hashids.Hashids(getClass().getName(),6);
    }
    @Autowired
    private uz.shina.bot.repository.FileStorageRepository fileStorageRepository;

    public uz.shina.bot.payload.ApiResponse save(MultipartFile[] files){
        List<uz.shina.bot.entity.FileStorage> fileStorages=new ArrayList<>();
        try {
            if (files!=null&&files.length>0){
                for (MultipartFile multipartFile : files) {
                    uz.shina.bot.entity.FileStorage fileStorage = uz.shina.bot.entity.FileStorage
                            .builder()
                            .name(multipartFile.getOriginalFilename())
                            .fileSize(multipartFile.getSize())
                            .contentType(multipartFile.getContentType())
                            .fileStorageStatus(uz.shina.bot.entity.FileStorageStatus.ACTIVE)
                            .extension(getEx(multipartFile.getOriginalFilename()))
                            .build();
            fileStorageRepository.save(fileStorage);


                    Date now = new Date();
                    //                this.uploadFolder+
                    File uploadFolder = new File(
                            (1900 + now.getYear()) +
                                    "/"
                                    + (1 + now.getMonth()) +
                                    "/"
                                    + now.getDate()
                    );

                    if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
                        System.out.println("Papkalar yaratildi!");
                    }
                    fileStorage.setHashId(hashids.encode(fileStorage.getId()));
                    fileStorage.setUploadPath(
//                        this.uploadFolder+
                            (1900 + now.getYear()) +
                                    "/"
                                    + (1 + now.getMonth()) +
                                    "/"
                                    + now.getDate() +
                                    "/"
                                    + fileStorage.getHashId() +
                                    "."
                                    + fileStorage.getExtension()
                    );

                    uploadFolder = uploadFolder.getAbsoluteFile();
                    File file = new File(uploadFolder, fileStorage.getHashId() + "." + fileStorage.getExtension());
                    try {
                        multipartFile.transferTo(file);
                        fileStorages.add(fileStorageRepository.save(fileStorage));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return converter.apiError();
                    }
                }
                return converter.apiSuccess(fileStorages);
            } return converter.apiError("Rasmlar bo'sh");
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }


    }



    public String getEx(String fileName){
        String ext=null;
        if (fileName!=null&&!fileName.isEmpty()){
            int dot=fileName.lastIndexOf(".");
            if (dot>0&&dot<=fileName.length()-2){
                ext=fileName.substring(dot+1);
            }
        }
        return ext;
    }


    public uz.shina.bot.entity.FileStorage findByHashId(String hashId) {
        return fileStorageRepository.findByHashId(hashId);
    }
}
