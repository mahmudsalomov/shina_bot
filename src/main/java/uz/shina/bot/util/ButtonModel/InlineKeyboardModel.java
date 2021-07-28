package uz.shina.bot.util.ButtonModel;

import lombok.Builder;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;


public class InlineKeyboardModel extends InlineKeyboardButton {


    public InlineKeyboardModel() {
        super();
    }

    @Builder
    public InlineKeyboardModel(String text,
                               String callbackData,
                               String url,
                               Boolean pay,
                               LoginUrl loginUrl,
                               CallbackGame callbackGame,
                               String switchInlineQuery,
                               String switchInlineQueryCurrentChat) {
        setText(text);
        setCallbackData(callbackData);
        setUrl(url);
        setPay(pay);
        setLoginUrl(loginUrl);
        setCallbackGame(callbackGame);
        setSwitchInlineQuery(switchInlineQuery);
        setSwitchInlineQueryCurrentChat(switchInlineQueryCurrentChat);
    }

    public InlineKeyboardModel(String text) {
        super(text);
    }

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    public InlineKeyboardButton setText(String text) {
        return super.setText(text);
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public InlineKeyboardButton setUrl(String url) {
        return super.setUrl(url);
    }

    @Override
    public String getCallbackData() {
        return super.getCallbackData();
    }

    @Override
    public InlineKeyboardButton setCallbackData(String callbackData) {
        return super.setCallbackData(callbackData);
    }

    @Override
    public String getSwitchInlineQuery() {
        return super.getSwitchInlineQuery();
    }

    @Override
    public InlineKeyboardButton setSwitchInlineQuery(String switchInlineQuery) {
        return super.setSwitchInlineQuery(switchInlineQuery);
    }

    @Override
    public CallbackGame getCallbackGame() {
        return super.getCallbackGame();
    }

    @Override
    public InlineKeyboardButton setCallbackGame(CallbackGame callbackGame) {
        return super.setCallbackGame(callbackGame);
    }

    @Override
    public String getSwitchInlineQueryCurrentChat() {
        return super.getSwitchInlineQueryCurrentChat();
    }

    @Override
    public InlineKeyboardButton setSwitchInlineQueryCurrentChat(String switchInlineQueryCurrentChat) {
        return super.setSwitchInlineQueryCurrentChat(switchInlineQueryCurrentChat);
    }

    @Override
    public Boolean getPay() {
        return super.getPay();
    }

    @Override
    public InlineKeyboardButton setPay(Boolean pay) {
        return super.setPay(pay);
    }

    @Override
    public LoginUrl getLoginUrl() {
        return super.getLoginUrl();
    }

    @Override
    public InlineKeyboardButton setLoginUrl(LoginUrl loginUrl) {
        return super.setLoginUrl(loginUrl);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        super.validate();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
