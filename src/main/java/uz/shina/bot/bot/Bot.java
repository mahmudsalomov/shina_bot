package uz.shina.bot.bot;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static java.lang.StrictMath.toIntExact;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    @Autowired
    private BotService botService;


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()){
            SendMessage sendMessage = botService.category(update.getMessage().getChatId());
            executeWithExceptionCheck(sendMessage);
        }
        else {
            if (update.hasCallbackQuery()){
//                if (update.getCallbackQuery().getData().startsWith("p")){
//                    SendMediaGroup sendMediaGroup = botService.productWithImage(update.getCallbackQuery().getFrom().getId(), Integer.valueOf(removeFirstChar(update.getCallbackQuery().getData())));
//                    execute(sendMediaGroup);
//                }



                SendMessage sendMessage = botService.finder(update.getCallbackQuery());
                executeWithExceptionCheck(sendMessage);
            }
        }


    }

    // Геттеры, которые необходимы для наследования от TelegramLongPollingBot
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("oops");
        }
    }

    public void executeWithExceptionCheck(SendMessage sendMessage,Update update) {

        EditMessageText editMessageText=new EditMessageText();
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
            if (update.hasMessage()) {
                editMessageText.setChatId(String.valueOf(update.getMessage().getChatId()));
                editMessageText.setMessageId(update.getMessage().getMessageId());
                editMessageText.setText(sendMessage.getText());


                editMessageReplyMarkup.setChatId(String.valueOf(update.getMessage().getChatId()));
                editMessageReplyMarkup.setMessageId(update.getMessage().getMessageId());
                editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
            } else {
                editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                editMessageText.setText(sendMessage.getText());


                editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageReplyMarkup.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());

            }
        try {
            execute(editMessageText);
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public String removeFirstChar(String s){
        return s.substring(1);
    }

    public void executeWithExceptionCheck(EditMessageReplyMarkup sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("oops");
        }
    }

    public void executeWithExceptionCheck(AnswerCallbackQuery sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("oops");
        }
    }

    public void executeWithExceptionCheck(SendPhoto sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("oops");
        }
    }
//    public void executeWithExceptionCheck( sendMessage) {
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            log.error("oops");
//        }
//    }


//    public void editMessage(Update update){
//        EditMessageText editMessageText=new EditMessageText();
//        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//
//        if (update.hasMessage()) {
//            editMessageText
//                    .setChatId(String.valueOf(update.getMessage().getChatId()))
//                    .setMessageId(update.getMessage().getMessageId());
//            editMessageReplyMarkup.setChatId(String.valueOf(update.getMessage().getChatId())).setMessageId(update.getMessage().getMessageId());
//        } else {
//            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
//            editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
//        }
//        try {
//            execute(editMessageText);
//            execute(editMessageReplyMarkup);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteMessage(Update update){
//        DeleteMessage deleteMessage=new DeleteMessage();
//        if (update.hasMessage()) {
//            deleteMessage.setChatId(String.valueOf(update.getMessage().getChatId())).setMessageId(update.getMessage().getMessageId()-1);
//        } else {
//            deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
//        }
//        try {
//            execute(deleteMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
}
