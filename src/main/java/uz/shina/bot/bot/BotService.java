package uz.shina.bot.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.shina.bot.entity.Category;
import uz.shina.bot.entity.FileStorage;
import uz.shina.bot.entity.Product;
import uz.shina.bot.repository.CategoryRepository;
import uz.shina.bot.repository.FileStorageRepository;
import uz.shina.bot.repository.ProductRepository;
import uz.shina.bot.util.ButtonModel.Col;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
        String callback=callbackQuery.getData();
        if (callback.startsWith("b")){
            return category(callbackQuery.getFrom().getId());
        }
        if (callback.startsWith("c")){
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


    public SendPhoto productPhoto(Long chatId,Integer productId, Integer imageNumber) throws IOException {

        Col col=new Col();
        Optional<Product> product = productRepository.findById(productId);
        List<FileStorage> fileStorages = fileStorageRepository.findAllByProduct(product.get());

        if (fileStorages.size()<=imageNumber){
            imageNumber=0;
            col.add("Keyingi rasm","i-"+productId+"-"+0);
        } else{
            col.add("Keyingi rasm","i-"+productId+"-"+(imageNumber+1));
        }
        System.out.println(col.getCol());
//        URL url=new URL("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg");
//        URL url=new URL("http://localhost:8080/photo/"+fileStorages.get(imageNumber).getHashId());
//        URL url=new URL("https://shinalar.herokuapp.com/photo/"+fileStorages.get(imageNumber).getHashId());
        URL url=new URL("http://89.43.33.54/api/photo/"+fileStorages.get(imageNumber).getHashId());
//        URL url=new URL("http://89.43.33.54/photo/d6Xv4G");
        URLConnection connection=url.openConnection();

        SendPhoto msg = new SendPhoto();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(connection.getInputStream(),"a");
        msg.setChatId(String.valueOf(chatId));
        msg.setPhoto(inputFile);
        msg.setCaption(product.get().getName());
        col.add("◀️Back","back");
        msg.setReplyMarkup(col.getMarkup());


        return msg;

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


    public Integer parseInt(String str){
        try {
            String[] parts = str.split("-");
            return Integer.parseInt(parts[1]);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public Integer parseInt3th(String str){
        try {
            String[] parts = str.split("-");
            return Integer.parseInt(parts[2]);
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
