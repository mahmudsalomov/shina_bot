package uz.shina.bot.util;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.shina.bot.util.ButtonModel.InlineKeyboardModel;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardBuilder {


    public static InlineKeyboardButton keyboard(String text){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData("none")
                .build();
    }

    public static InlineKeyboardButton keyboard(String text, String callback){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData(callback)
                .build();
    }

    public static InlineKeyboardButton keyboard(String text, String callback, String url){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData(callback)
                .url(url)
                .build();
    }


    public InlineKeyboardMarkup keyboardMarkup(List<InlineKeyboardButton> buttonList){
        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();

        for (InlineKeyboardButton inlineKeyboardButton : buttonList) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        }
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        return new InlineKeyboardMarkup(rowList);
    }



    public InlineKeyboardMarkup keyboardMarkupWithoutExit(List<InlineKeyboardButton> buttonList){
        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        for (InlineKeyboardButton inlineKeyboardButton : buttonList) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();

            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        }
        return new InlineKeyboardMarkup(rowList);
    }
}
