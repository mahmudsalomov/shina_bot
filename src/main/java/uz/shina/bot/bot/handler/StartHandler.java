package uz.shina.bot.bot.handler;


import com.example.mit.bot.State;
import com.example.mit.model.User;
import com.example.mit.model.profile_handler_model.ProfileEnums;
import com.example.mit.repository.CategoryRepository;
import com.example.mit.repository.UserRepository;
import com.example.mit.service.UserService;
import com.example.mit.util.ButtonModel.Col;
import com.example.mit.util.ButtonModel.Row;
import com.example.mit.util.KeyboardBuilder;
import com.example.mit.util.Language;
import com.example.mit.util.MessagesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.mit.util.TelegramUtil.createMessageTemplate;


@Component
public class StartHandler implements Handler{
    @Autowired
    private KeyboardBuilder keyboardBuilder;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    private final UserService userService;

    public StartHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        System.out.println(message);

        if (user.getLanguage()==null){
            Row row=new Row();
            row.add("\uD83C\uDDFA\uD83C\uDDFF Uz", Language.LANGUAGE_OZ);
            row.add("\uD83C\uDDFA\uD83C\uDDFF Ўз", Language.LANGUAGE_UZ);
            row.add("\uD83C\uDDF7\uD83C\uDDFA Ru", Language.LANGUAGE_RU);

            user.setBotState(State.START);
            userRepository.save(user);
            return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                    "Tilni tanlang!!!\nТилни танланг!!!\nВыберите язык", user.getName()))
                    .setReplyMarkup(row.getMarkup()));
        }

        if (user.getPhone()==null){

            KeyboardButton button=new KeyboardButton();
            button.setRequestContact(true);
            button.setText("Telefon raqamni yuborish");
            KeyboardRow row=new KeyboardRow();
            row.add(button);
            return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                    .setText("*❗️Botdan foydalanish uchun telefon raqamingizni yuboring!*")
                    .setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row)).setResizeKeyboard(true)));

        }

        String msg=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.MENU_MSG_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.MENU_MSG_UZ_LATIN
                        :MessagesInterface.MENU_MSG_UZ_KRILL;
        String BTN_PROMOTIONS=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROMOTIONS_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROMOTIONS_UZ_LATIN
                        :MessagesInterface.BTN_PROMOTIONS_UZ_KRILL;
        String BTN_BASKET=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_BASKET_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_BASKET_UZ_LATIN
                        :MessagesInterface.BTN_BASKET_UZ_KRILL;
        String BTN_PRODUCT=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PRODUCT_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PRODUCT_UZ_LATIN
                        :MessagesInterface.BTN_PRODUCT_UZ_KRILL;
        String BTN_PROFILE=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROFILE_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROFILE_LATIN
                        :MessagesInterface.BTN_PROFILE_KRILL;

        Col col =new Col();
        col.add(BTN_PRODUCT,State.PRODUCT.name());
        col.add(BTN_PROMOTIONS,State.PROMOTIONS.name());
        col.add(BTN_BASKET,State.BASKET.name());
        col.add(BTN_PROFILE,State.PROFILE.name());


        return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                msg, user.getName())).setReplyMarkup(col.getMarkup())
        );

    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, CallbackQuery callback) {

        System.out.println("START "+callback.getData());





        if (callback.getData().equals(Language.LANGUAGE_RU)||
                callback.getData().equals(Language.LANGUAGE_OZ)||
                callback.getData().equals(Language.LANGUAGE_UZ)){

            user.setLanguage(callback.getData());
            userRepository.save(user);

            if (user.getLanguage()==null){
                Row row=new Row();
                row.add("\uD83C\uDDFA\uD83C\uDDFF Uz", Language.LANGUAGE_OZ);
                row.add("\uD83C\uDDFA\uD83C\uDDFF Ўз", Language.LANGUAGE_UZ);
                row.add("\uD83C\uDDF7\uD83C\uDDFA Ru", Language.LANGUAGE_RU);

                user.setBotState(State.START);
                userRepository.save(user);
                return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                        "Tilni tanlang!!!\nТилни танланг!!!\nВыберите язык", user.getName()))
                        .setReplyMarkup(row.getMarkup()));
            }
            if (user.getPhone()==null){
                KeyboardButton button=new KeyboardButton();
                button.setRequestContact(true);
                button.setText("Telefon raqamni yuborish");
                KeyboardRow row=new KeyboardRow();
                row.add(button);
                return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                        .setText("*❗️Botdan foydalanish uchun telefon raqamingizni yuboring!*")
                        .setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row)).setResizeKeyboard(true)));
            }


            String msg=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.MENU_MSG_RU :
                    user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.MENU_MSG_UZ_LATIN
                            :MessagesInterface.MENU_MSG_UZ_KRILL;
            String BTN_PROMOTIONS=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROMOTIONS_RU :
                    user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROMOTIONS_UZ_LATIN
                            :MessagesInterface.BTN_PROMOTIONS_UZ_KRILL;
            String BTN_BASKET=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_BASKET_RU :
                    user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_BASKET_UZ_LATIN
                            :MessagesInterface.BTN_BASKET_UZ_KRILL;
            String BTN_PRODUCT=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PRODUCT_RU :
                    user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PRODUCT_UZ_LATIN
                            :MessagesInterface.BTN_PRODUCT_UZ_KRILL;
            String BTN_PROFILE=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROFILE_RU :
                    user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROFILE_LATIN
                            :MessagesInterface.BTN_PROFILE_KRILL;
            Col col =new Col();
            col.add(BTN_PRODUCT,State.PRODUCT.name());
            col.add(BTN_PROMOTIONS,State.PROMOTIONS.name());
            col.add(BTN_BASKET,State.BASKET.name());
            col.add(BTN_PROFILE,State.PROFILE.name());


            return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                    msg, user.getName())).setReplyMarkup(col.getMarkup())
            );
        }


        String msg=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.MENU_MSG_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.MENU_MSG_UZ_LATIN
                        :MessagesInterface.MENU_MSG_UZ_KRILL;
        String BTN_PROMOTIONS=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROMOTIONS_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROMOTIONS_UZ_LATIN
                        :MessagesInterface.BTN_PROMOTIONS_UZ_KRILL;
        String BTN_BASKET=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_BASKET_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_BASKET_UZ_LATIN
                        :MessagesInterface.BTN_BASKET_UZ_KRILL;
        String BTN_PRODUCT=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PRODUCT_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PRODUCT_UZ_LATIN
                        :MessagesInterface.BTN_PRODUCT_UZ_KRILL;
        String BTN_PROFILE=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROFILE_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROFILE_LATIN
                        :MessagesInterface.BTN_PROFILE_KRILL;

        Col col =new Col();
        col.add(BTN_PRODUCT,State.PRODUCT.name());
        col.add(BTN_PROMOTIONS,State.PROMOTIONS.name());
        col.add(BTN_BASKET,State.BASKET.name());
        col.add(BTN_PROFILE,State.PROFILE.name());


        return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                msg, user.getName())).setReplyMarkup(col.getMarkup())
        );
    }

    public List<PartialBotApiMethod<? extends Serializable>> addContact(User user, Update update){
        user.setPhone(update.getMessage().getContact().getPhoneNumber());
        user=userService.save(user);
        String msg=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.MENU_MSG_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.MENU_MSG_UZ_LATIN
                        :MessagesInterface.MENU_MSG_UZ_KRILL;
        String BTN_PROMOTIONS=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROMOTIONS_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROMOTIONS_UZ_LATIN
                        :MessagesInterface.BTN_PROMOTIONS_UZ_KRILL;
        String BTN_BASKET=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_BASKET_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_BASKET_UZ_LATIN
                        :MessagesInterface.BTN_BASKET_UZ_KRILL;
        String BTN_PRODUCT=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PRODUCT_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PRODUCT_UZ_LATIN
                        :MessagesInterface.BTN_PRODUCT_UZ_KRILL;
        String BTN_PROFILE=user.getLanguage().equals(Language.LANGUAGE_RU)? MessagesInterface.BTN_PROFILE_RU :
                user.getLanguage().equals(Language.LANGUAGE_OZ)?MessagesInterface.BTN_PROFILE_LATIN
                        :MessagesInterface.BTN_PROFILE_KRILL;

        Col col =new Col();
        col.add(BTN_PRODUCT,State.PRODUCT.name());
        col.add(BTN_PROMOTIONS,State.PROMOTIONS.name());
        col.add(BTN_BASKET,State.BASKET.name());
        col.add(BTN_PROFILE,State.PROFILE.name());
//        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//        editMessageReplyMarkup.setMessageId(update.getMessage().getMessageId());
//        editMessageReplyMarkup.setChatId(update.getMessage().getChatId());
//        editMessageReplyMarkup.setReplyMarkup(col.getMarkup());
//        System.out.println(update);
//        System.out.println(update.getMessage());
//        System.out.println(update);
        return Collections.singletonList(createMessageTemplate(user).setText(String.format("" +
                msg, user.getName())).setReplyMarkup(col.getMarkup())
        );

//        return Collections.singletonList(createEditMessageTemplate(
//                String.valueOf(update.getMessage().getChatId()),update.getChosenInlineQuery().getInlineMessageId())
//                .setReplyMarkup(col.getMarkup())
//        );
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery(User user) {
        List<String> callBackList=new ArrayList<>();
        callBackList.add(Language.LANGUAGE_RU);
        callBackList.add(Language.LANGUAGE_UZ);
        callBackList.add(Language.LANGUAGE_OZ);
        callBackList.add(ProfileEnums.MY_LANGUAGE.name());
        callBackList.add(State.START.name());
        return callBackList;
    }
}
