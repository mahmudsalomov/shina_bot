package uz.shina.bot.bot;

import com.example.mit.model.User;
import com.example.mit.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
    private UserRepository userRepository;
    private final UpdateReceiver updateReceiver;

    @Override
    public void onUpdateReceived(Update update) {
        List<PartialBotApiMethod<? extends Serializable>> messagesToSend = updateReceiver.handle(update);
        if (messagesToSend != null && !messagesToSend.isEmpty()) {

            messagesToSend.forEach(response -> {
                if (response instanceof SendMessage) {



                    if (update.hasCallbackQuery()) {
                        Optional<User> user = userRepository.getByChatId(update.getCallbackQuery().getFrom().getId());

                        if (user.isPresent()&&user.get().getPhone()!=null&&user.get().getLanguage()!=null){
                            long message_id = update.getCallbackQuery().getMessage().getMessageId();
                            long chat_id = update.getCallbackQuery().getMessage().getChatId();

                            try {
                                InlineKeyboardMarkup replyMarkup=null;
                                try {
                                    replyMarkup = (InlineKeyboardMarkup) ((SendMessage) response).getReplyMarkup();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                EditMessageText new_message = new EditMessageText()
                                        .setChatId(chat_id)
                                        .setMessageId(toIntExact(message_id))
                                        .enableMarkdown(true)
                                        .setReplyMarkup(replyMarkup)
                                        .setText(((SendMessage) response).getText());
                                execute(new_message);
                            } catch (TelegramApiException e) {
                                executeWithExceptionCheck((SendMessage) response);
//                            e.printStackTrace();
                            }
                        }
                        else
                        executeWithExceptionCheck((SendMessage) response);



                    } else
                    {
                        if (update.getMessage().hasContact()){
                            deleteMessage(update);
                            executeWithExceptionCheck((SendMessage) response);
                        }
                        else
                            executeWithExceptionCheck((SendMessage) response);
                    }
                }
                if (response instanceof SendPhoto) {
                    executeWithExceptionCheck((SendPhoto) response);
                }else if (response instanceof EditMessageReplyMarkup){
                    executeWithExceptionCheck((EditMessageReplyMarkup) response);
                }else if (response instanceof AnswerCallbackQuery){
                    executeWithExceptionCheck((AnswerCallbackQuery) response);
                }

            });
        }
//       deleteMessage(update);
    }

    public void executeWithExceptionCheck(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("oops");
        }
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


    public void editMessage(Update update){
        EditMessageText editMessageText=new EditMessageText();
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();

        if (update.hasMessage()) {
            editMessageText.setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
            editMessageReplyMarkup.setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId());
        } else {
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageReplyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        try {
            execute(editMessageText);
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Update update){
        DeleteMessage deleteMessage=new DeleteMessage();
        if (update.hasMessage()) {
            deleteMessage.setChatId(update.getMessage().getChatId()).setMessageId(update.getMessage().getMessageId()-1);
        } else {
            deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId()).setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
