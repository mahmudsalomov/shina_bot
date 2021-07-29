package uz.shina.bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.*;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/category")
    public String category(){
        return "category";
    }

}
