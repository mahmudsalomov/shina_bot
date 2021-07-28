package uz.shina.bot.bot;


import com.example.mit.bot.handler.Handler;
import com.example.mit.bot.handler.StartHandler;
import com.example.mit.model.User;
import com.example.mit.repository.ProductRepository;
import com.example.mit.repository.UserRepository;
import com.example.mit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@Service
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;
    public UpdateReceiver(List<Handler> handlers, UserRepository userRepository) {
        this.handlers = handlers;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {


        try {
            if (isMessageWithText(update)) {
                final Message message = update.getMessage();
                System.out.println(message);
//                update.getMessage().getFrom();


                final int chatId = message.getFrom().getId();
                final User user = userRepository.getByChatId(chatId)
                        .orElseGet(() -> userRepository.save(new User(update.getMessage().getFrom())));
                user.setAction(" ");
                userRepository.save(user);
                return getHandlerByState(user.getBotState()).handle(user, message.getText());
            }
            else if (update.hasCallbackQuery()) {

                System.out.println(update.getCallbackQuery().getData());
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final int chatId = callbackQuery.getFrom().getId();
                String message=callbackQuery.getData();
                System.out.println(update.getCallbackQuery().getData());
                if (callbackQuery.getData().equals("EXIT")){
                    final User user = userRepository.getByChatId(chatId)
                            .orElseGet(() -> userRepository.save(new User(chatId)));
                    user.setBotState(State.START);
                    user.setAction(" ");
                    userRepository.save(user);
                    return getHandlerByState(user.getBotState()).handle(user, callbackQuery.getData());
                }


                    final User user = userRepository.getByChatId(chatId)
                            .orElseGet(() -> userRepository.save(new User(chatId)));
//                user.setAction(addAction(user, message).trim());
//                System.out.println(user.getAction());
//                User save = userRepository.save(user);
                return getHandlerByCallBackQuery(callbackQuery.getData(),user).handle(user, callbackQuery);
//
//                }




            } else if (update.getMessage().hasContact()){
                StartHandler startHandler=new StartHandler(userService);
                final int chatId = Math.toIntExact(update.getMessage().getChatId());
                final User user = userRepository.getByChatId(chatId)
                        .orElseGet(() -> userRepository.save(new User(chatId)));

                return startHandler.addContact(user,update);
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException | IOException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
//        System.out.println("state "+state);
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query,User user) {
//        System.out.println("state Callback "+query);
//        System.out.println(handlers);
//        handlers.forEach(item-> System.out.println(item.operatedCallBackQuery()));
        String[] parts=query.split("-");
        String finalQuery = parts[0];
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery(user).stream()
                        .anyMatch(finalQuery::equals))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText()&& !update.getMessage().hasContact();
    }

    private String addAction(User user,String data){
        String[] split = data.split("-");
        if (user.getAction().equals("")){
           switch (split[0]){
               case "PRODUCT": return "-c-1";
               case "catId": return "-c-"+split[1];
               case "brandId": return "-b-"+split[1];
               case "prodId": return "-p-"+split[1];
               default:return " ";
           }
       }else  {
            switch (split[0]){
                case "PRODUCT": return user.getAction()+"-c-1";
                case "catId": return user.getAction()+"-c-"+split[1];
                case "brandId": return user.getAction()+"-b-"+split[1];
                case "prodId": return user.getAction()+"-p-"+split[1];
                default:return user.getAction()+" ";
            }
       }
    }


}
