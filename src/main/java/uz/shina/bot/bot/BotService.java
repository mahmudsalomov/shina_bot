package uz.shina.bot.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.Product;
import uz.shina.bot.repository.CategoryRepository;
import uz.shina.bot.repository.FileStorageRepository;
import uz.shina.bot.repository.ProductRepository;
import uz.shina.bot.util.ButtonModel.Col;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BotService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileStorageRepository fileStorageRepository;

    public BotService(CategoryRepository categoryRepository, ProductRepository productRepository, FileStorageRepository fileStorageRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.fileStorageRepository = fileStorageRepository;
    }


    public SendMessage finder(CallbackQuery callbackQuery) throws IOException {
//        if (callbackQuery.getData().startsWith("p")){
//            return productWithImage(callbackQuery.getFrom().getId(), Integer.valueOf(removeFirstChar(callbackQuery.getData())));
//        }
        if (callbackQuery.getData().startsWith("b")){
            return category(callbackQuery.getFrom().getId());
        }
        if (callbackQuery.getData().startsWith("c")){
            product(callbackQuery.getFrom().getId(), Integer.valueOf(removeFirstChar(callbackQuery.getData())));
        }


        return product(callbackQuery.getFrom().getId(), Integer.valueOf(callbackQuery.getData()));


    }



    public SendMessage category(Long chatId){

        Col col=new Col();
        List<Category> all = categoryRepository.findAll();

        for (Category category : all) {
            col.add(category.getName(), String.valueOf(category.getId()));
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Kategoriyalar");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(col.getMarkup());
        return sendMessage;

    }


    public SendMessage product(Long chatId,Integer categoryId){

        Col col=new Col();
        Optional<Category> category = categoryRepository.findById(categoryId);
        List<Product> products = productRepository.findAllByCategory(category.get());
        for (Product product : products) {
            col.add(product.getName(), "p"+String.valueOf(product.getId()));
        }
        col.add("◀️Back","back");
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Productlar");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(col.getMarkup());
        return sendMessage;

    }


//    public SendMediaGroup productWithImage(Long chatId, Integer productId) throws IOException {
//
//
//        Col col=new Col();
//        Optional<Product> product = productRepository.findById(productId);
//        List<FileStorage> fileStorages = fileStorageRepository.findAllByProduct(product.get());
//
//        List<SendPhoto> sendPhotoList=new ArrayList<>();
//        List<InputMedia> inputMedia=new ArrayList<>();
//
//        for (FileStorage fileStorage : fileStorages) {
//
//
//            URL url = new URL("http://localhost:8080/photo/" + fileStorage.getHashId());
//            URLConnection connection = url.openConnection();
//
//            InputMediaPhoto photo=new InputMediaPhoto();
//            photo.setCaption(product.get().getName() + "\n ");
//
//            InputFile inputFile=new InputFile();
//            inputFile.setMedia(new InputFile(connection.getInputStream(),fileStorage.getName());
//            photo.setMedia(inputFile);
//            inputMedia.add(photo);
//
////            SendPhoto sendPhoto = createPhotoTemplate(Math.toIntExact(chatId));
////            sendPhoto.setPhoto(new InputFile(connection.getInputStream(), fileStorage.getName()));
////            sendPhoto.setCaption(product.get().getName() + "\n ");
////            sendPhoto.setReplyMarkup(col.getMarkup());
////            sendPhotoList.add(
////                    sendPhoto
////            );
//        }
//
//
//
//        SendMediaGroup sendMediaGroup=new SendMediaGroup();
//        SendMessage sendMessage=new SendMessage();
//
//        sendMediaGroup.setMedias(inputMedia);
//        sendMediaGroup.setChatId(String.valueOf(chatId));
//        return sendMediaGroup;
////        try {
////            return sen;
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////        SendMessage sendMessage=new SendMessage();
////        return sendMessage;
//
//    }



    public SendMessage Back(Long chatId,Integer categoryId){

        Col col=new Col();
        Optional<Category> category = categoryRepository.findById(categoryId);
        List<Product> products = productRepository.findAllByCategory(category.get());
        for (Product product : products) {
            col.add(product.getName(), "p"+String.valueOf(product.getId()));
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Productlar");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(col.getMarkup());
        return sendMessage;

    }



    public static SendPhoto createPhotoTemplate(Integer chatId){
        SendPhoto sendPhoto=new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        return sendPhoto;
    }
    public String removeFirstChar(String s){
        return s.substring(1);
    }

}
