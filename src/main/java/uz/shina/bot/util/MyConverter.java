package uz.shina.bot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.shina.bot.payload.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class MyConverter {



    @Autowired
    private uz.shina.bot.repository.FileStorageRepository fileStorageRepository;



    @Autowired
    private MyConverter converter;



    public ApiResponse api(String message, boolean success){
       return new ApiResponse(message,success);
    }

    public ApiResponse api(String message, boolean success,Object object){
        return new ApiResponseObject(message,success,object);
    }

    public ApiResponse api(String message, boolean success,Object object,long totalElements, Integer totalPages){
        return new ApiResponseObject(message,success,object,totalElements,totalPages);
    }

    public ApiResponse apiError(){
        return api("Error",false);
    }

    public ApiResponse apiError(String message){
        return api(message,false);
    }

    public ApiResponse apiError(Object object){
        return api("Error",false,object);
    }

    public ApiResponse apiSuccess(){
        return api("Success",true);
    }

    public ApiResponse apiSuccess(String message){
        return api(message,true);
    }

    public ApiResponse apiSuccess(Object object){
        return api("Success",true,object);
    }

    public ApiResponse apiSuccess(String message, Object object){
        return api(message,true,object);
    }

    public ApiResponse apiSuccess(Object object,long totalElements, Integer totalPages){
        return api("Success",true, object, totalElements, totalPages);
    }

    public ApiResponse apiSuccess(String message, Object object,long totalElements, Integer totalPages){
        return api(message,true, object, totalElements, totalPages);
    }



    public uz.shina.bot.payload.ApiResponse apiSuccessObject(Object object){
        return api("Success",true,object);
    }

    public ApiResponse apiSuccessObject(Object object,long totalElements, Integer totalPages){
        return api("Success",true, object, totalElements, totalPages);
    }


}
