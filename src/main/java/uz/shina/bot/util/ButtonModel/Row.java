package uz.shina.bot.util.ButtonModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.shina.bot.util.TelegramUtil;

import java.util.ArrayList;
import java.util.List;

@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Row {


    private List<InlineKeyboardButton> row=new ArrayList<>();
    private final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


    public InlineKeyboardMarkup getMarkup() {
        this.markup.setKeyboard(new Col(row).getCol());
        return markup;
    }

    public Row(InlineKeyboardButton button) {
        this.row.add(button);
    }

    public Row(String text) {
        add(text);
    }
    public Row(String text, String callback) {
        add(text,callback);
    }

    public Row(String text, String callback, String url) {
        add(text,callback,url);
    }

    public List<InlineKeyboardButton> getButtons() {
        return row;
    }

    public void setButtons(List<InlineKeyboardButton> buttons) {
        this.row = buttons;
    }

    public void add(String text) {
        this.row.add(TelegramUtil.keyboard(text));
    }

    public void add(InlineKeyboardButton button) {
        this.row.add(button);
    }

    public void add(String text, String callback) {
        this.row.add(TelegramUtil.keyboard(text,callback));
    }

    public void add(String text, String callback, String url) {
        this.row.add(TelegramUtil.keyboard(text,callback,url));
    }



    public void clear(){
        this.row=new ArrayList<>();
    }

}
