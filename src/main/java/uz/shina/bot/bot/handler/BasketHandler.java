package uz.shina.bot.bot.handler;

import com.example.mit.bot.State;
import com.example.mit.model.*;
import com.example.mit.repository.OrderRepository;
import com.example.mit.repository.ProductRepository;
import com.example.mit.repository.ProductWithAmountRepository;
import com.example.mit.repository.UserRepository;
import com.example.mit.util.ButtonModel.Col;
import com.example.mit.util.ButtonModel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.mit.util.TelegramUtil.createMessageTemplate;

@Service
@Component
public class BasketHandler implements Handler{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductWithAmountRepository amountRepository;

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);

        if (orders.size()==0){

            Col col=new Col();
            col.add("Bosh menyu",State.START.name());

            return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                    .setText("*❗️Savatcha bo'sh!*")
                    .setReplyMarkup(col.getMarkup()));
        }
        List<ProductWithAmount> amounts = amountRepository.findAllByOrder(orders.get(0));


        String text="";
        Col col=new Col();
        Row row=new Row();
        col.add("✅Buyurtma berish!","order-"+orders.get(0).getId());
        for (ProductWithAmount amount:amounts){
            if (amount.getProduct().getActualPrice()!=null){
                row.clear();
                text+="Mahsulot:\n*"+amount.getProduct().getNameOz()+"\n"
                        +amount.getAmount()+"x"+amount.getProduct().getActualPrice()+"="
                        +(amount.getAmount()*Float.parseFloat(amount.getProduct().getActualPrice()))+"*\n\n";
                row.add("❌ O'chirish!","amount_"+amount.getId());
                row.add(amount.getProduct().getNameOz());
                col.add(row);
            }

        }





        col.add("Bosh menyu",State.START.name());

        return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                .setText(text)
                .setReplyMarkup(col.getMarkup()));

    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, CallbackQuery callback) {

        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);

        if (parseString(callback.getData()).equals("addBasket")){

            Integer id=parseInt(callback.getData());
            int count = Integer.parseInt(parseString3th(callback.getData()));
            System.out.println("ID = "+id);
            System.out.println("callback = "+callback.getData());
            System.out.println("count = "+count);
            Optional<Product> byId = productRepository.findById(Long.valueOf(id));

            if (byId.isPresent()){
                if (orders.size()==0){
                    Order order= Order
                            .builder()
                            .orderState(OrderState.DRAFT)
                            .user(user)
                            .build();
                    order=orderRepository.save(order);
                    ProductWithAmount productWithAmount= ProductWithAmount
                            .builder()
                            .amount(count)
                            .order(order)
                            .product(byId.get())
                            .build();
                    amountRepository.save(productWithAmount);
                } else {
                    if (amountRepository.existsByOrderAndProduct(orders.get(0),byId.get())){

                        ProductWithAmount withAmount = amountRepository.findByOrderAndProduct(orders.get(0), byId.get());
                        withAmount.setAmount(withAmount.getAmount()+count);
                        amountRepository.save(withAmount);

                    }
                    else {
                        ProductWithAmount productWithAmount= ProductWithAmount
                                .builder()
                                .amount(count)
                                .order(orders.get(0))
                                .product(byId.get())
                                .build();
                        amountRepository.save(productWithAmount);
                    }

                }

            }

            orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);

        }



        if (orders.size()==0){

            Col col=new Col();
            col.add("Bosh menyu",State.START.name());
            return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                    .setText("*❗️Savatcha bo'sh!*")
                    .setReplyMarkup(col.getMarkup()));


        }

        if (parseString(callback.getData()).equals("amount")){
            amountRepository.deleteById(Long.valueOf(parseInt(callback.getData())));
        }
        List<ProductWithAmount> amounts = amountRepository.findAllByOrder(orders.get(0));
        if (amounts.size()==0){
            orderRepository.deleteById(orders.get(0).getId());
        }




        String text="";
        Col col=new Col();
        Row row=new Row();
        col.add("✅Buyurtma berish!","order-"+orders.get(0).getId());
        double fullAmount=0;
        for (ProductWithAmount amount:amounts){
            if (amount.getProduct().getActualPrice()!=null){
                row.clear();
                text+="Mahsulot:\n*"+amount.getProduct().getNameOz()+"\n"
                        +amount.getAmount()+"x"+amount.getProduct().getActualPrice()+"="
                        +(amount.getAmount()*Float.parseFloat(amount.getProduct().getActualPrice()))+"*\n\n";
                row.add("❌ O'chirish!","amount-"+amount.getId());
                row.add(amount.getProduct().getNameOz());
                col.add(row);
                fullAmount+=amount.getAmount()*Float.parseFloat(amount.getProduct().getActualPrice());
            }
        }
        text+="Umumiy narx : *"+fullAmount+"*";





        col.add("Bosh menyu",State.START.name());

        return Collections.singletonList(createMessageTemplate(user)
//                    .setText(MessagesInterface.BTN_PROFILE_LATIN+"\n"+ TelegramUtil.parseName(user))
                .setText(text)
                .setReplyMarkup(col.getMarkup()));
    }

    @Override
    public State operatedBotState() {
        return State.BASKET;
    }

    @Override
    public List<String> operatedCallBackQuery(User user) {

        List<String> list=new ArrayList<>();
        list.add(State.BASKET.name());


//        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);
//
//        if (orders.size()>0){
//            List<ProductWithAmount> amounts = amountRepository.findAllByOrder(orders.get(0));
//
//            for (ProductWithAmount amount:amounts){
//                list.add("amount-"+amount.getId());
//            }
//            list.add("order"+orders.get(0).getId());
//        }


        list.add("amount");
        list.add("addBasket");
        list.add("order");
        return list;
    }

    public List<Order> allDrafts(List<Order> orders){
        if (orders.size()>0){
            return orders
                    .stream()
                    .filter(item->item.getOrderState().equals(OrderState.DRAFT))
                    .collect(Collectors.toList());
        }
        return orders;

    }

    public Integer parseInt(String str){
        try {
            String[] parts = str.split("-");
            return Integer.parseInt(parts[1]);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public String parseString(String str){
        try {
            String[] parts = str.split("-");
            return parts[0];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String parseString3th(String str){
        try {
            String[] parts = str.split("-");
            return parts[2];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
