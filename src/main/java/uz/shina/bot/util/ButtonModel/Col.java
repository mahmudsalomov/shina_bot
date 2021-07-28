package uz.shina.bot.util.ButtonModel;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class Col {

    private List<List<InlineKeyboardButton>> col=new ArrayList<>();
    private final InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

    public InlineKeyboardMarkup getMarkup() {
        this.markup.setKeyboard(col);
        return markup;
    }

    public List<List<InlineKeyboardButton>> getCol() {
        return col;
    }

    public void setCol(List<List<InlineKeyboardButton>> col) {
        this.col = col;
    }

    public Col(String text) {
        add(new Row(text));
    }

    public Col(Row row) {
        add(row);
    }


    public Col(List<InlineKeyboardButton> row) {
        add(row);
    }

    public Col(String text, String callback) {
        add(new Row(text, callback));
    }

    public Col(String text, String callback, String url) {
        add(new Row(text, callback, url));
    }

    public void add(Row row) {
        this.col.add(row.getButtons());
    }

    public void add(InlineKeyboardButton button) {
        add(new Row(button));
    }

    public void add(List<InlineKeyboardButton> row) {
        add(new Row(row));
    }

    public void add(String text, String callback) {
        add(new Row(text, callback));
    }

    public void add(String text, String callback, String url) {
        add(new Row(text, callback, url));
    }


}
