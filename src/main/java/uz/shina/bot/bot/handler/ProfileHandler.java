package uz.shina.bot.bot.handler;

import com.example.mit.bot.State;
import com.example.mit.model.Order;
import com.example.mit.model.OrderState;
import com.example.mit.model.User;
import com.example.mit.model.profile_handler_model.ProfileEnums;
import com.example.mit.model.profile_handler_model.ProfileMenuStrings;
import com.example.mit.repository.CategoryRepository;
import com.example.mit.repository.OrderRepository;
import com.example.mit.repository.ProductRepository;
import com.example.mit.repository.UserRepository;
import com.example.mit.service.UserService;
import com.example.mit.util.ButtonModel.Col;
import com.example.mit.util.KeyboardBuilder;
import com.example.mit.util.MessagesInterface;
import com.example.mit.util.TelegramUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.mit.util.TelegramUtil.createMessageTemplate;


@Component
public class ProfileHandler implements Handler{
    @Autowired
    private KeyboardBuilder keyboardBuilder;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StartHandler startHandler;

    private final UserService userService;

    public ProfileHandler(UserService userService) {
        this.userService = userService;
    }


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message){


        if (user.getPhone()==null||user.getPhone().isEmpty()|| user.getPhone().equals("")){
            System.out.println("PHONE "+message);
            if (!message.equals(State.PROFILE.name())){
                user.setPhone(message);
                user=userService.save(user);
                Col col = new Col();
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_ORDERS.name()),ProfileEnums.MY_ORDERS.name());
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_LANGUAGE.name()),ProfileEnums.MY_LANGUAGE.name());
//                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_REGION.name()),ProfileEnums.MY_REGION.name());
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.EXIT.name()),ProfileEnums.EXIT.name());
//
                return Collections.singletonList(createMessageTemplate(user)
                        .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user)+"\n*"+user.getPhone()+"*")
                        .setReplyMarkup(col.getMarkup()));

            }else {
                KeyboardButton button=new KeyboardButton();
                button.setRequestContact(true);
                button.setText("Telefon raqamni yuborish");
                KeyboardRow row=new KeyboardRow();
                row.add(button);
                return Collections.singletonList(createMessageTemplate(user)
                        .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user))
                        .setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row))));
            }
        }

        else {
            Col col = new Col();
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_ORDERS.name()),ProfileEnums.MY_ORDERS.name());
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_LANGUAGE.name()),ProfileEnums.MY_LANGUAGE.name());
//            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_REGION.name()),ProfileEnums.MY_REGION.name());
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.EXIT.name()),ProfileEnums.EXIT.name());
            return Collections.singletonList(createMessageTemplate(user)
                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user)+"\n*"+user.getPhone()+"*")
                    .setReplyMarkup(col.getMarkup()));

        }




    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, CallbackQuery callback) {
        if (user.getPhone()==null||user.getPhone().isEmpty()|| user.getPhone().equals("")){
            System.out.println("PHONE "+callback.getData());
            if (!callback.getData().equals(State.PROFILE.name())){
                user.setPhone(callback.getData());
                user=userService.save(user);
                Col col = new Col();
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_ORDERS.name()),ProfileEnums.MY_ORDERS.name());
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_LANGUAGE.name()),ProfileEnums.MY_LANGUAGE.name());
//                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_REGION.name()),ProfileEnums.MY_REGION.name());
                col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.EXIT.name()),ProfileEnums.EXIT.name());
//
                return Collections.singletonList(createMessageTemplate(user)
                        .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user)+"\n*"+user.getPhone()+"*")
                        .setReplyMarkup(col.getMarkup()));

            }else {
                KeyboardButton button=new KeyboardButton();
                button.setRequestContact(true);
                button.setText("Telefon raqamni yuborish");
                KeyboardRow row=new KeyboardRow();
                row.add(button);
                return Collections.singletonList(createMessageTemplate(user)
                        .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user))
                        .setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row))));
            }
        }

        if (callback.getData().equals(ProfileEnums.MY_LANGUAGE.name())) {
            user.setLanguage(null);
            user=userService.save(user);
            return startHandler.handle(user,callback.getData());
        }

        if (callback.getData().equals(ProfileEnums.MY_ORDERS.name())){
            List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.ACTIVE);
            String text="";
            orders=findActiveOrders(orders);

//            if (orders.size()>0){
//
//            }
        }

            Col col = new Col();
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_ORDERS.name()),ProfileEnums.MY_ORDERS.name());
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_LANGUAGE.name()),ProfileEnums.MY_LANGUAGE.name());
//            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.MY_REGION.name()),ProfileEnums.MY_REGION.name());
            col.add(ProfileMenuStrings.map_oz.get(ProfileEnums.EXIT.name()),ProfileEnums.EXIT.name());
            return Collections.singletonList(createMessageTemplate(user)
                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+TelegramUtil.parseName(user)+"\n*"+user.getPhone()+"*")
                    .setReplyMarkup(col.getMarkup()));


    }

    @Override
    public State operatedBotState() {
        return State.PROFILE;
    }

    @Override
    public List<String> operatedCallBackQuery(User user) {

        List<String> idList=new ArrayList<>();

//        for (ProfileEnums profile : ProfileEnums.values()) {
//            System.out.println(profile);
//            idList.add(profile.name());
//        }
//
        idList.add(ProfileEnums.MY_ORDERS.name());
        idList.add(ProfileEnums.MY_LANGUAGE.name());
        idList.add(ProfileEnums.MY_REGION.name());

        idList.add(State.PROFILE.name());

        return idList;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public List<Order> findActiveOrders(List<Order> orders){
        if (orders.size()>0){
            return orders
                    .stream()
                    .filter(item->item.getOrderState().equals(OrderState.ACTIVE))
                    .collect(Collectors.toList());
        }
        return orders;
    }
}
