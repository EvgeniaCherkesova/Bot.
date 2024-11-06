package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "8029386665:AAFwHizKwSdMz-Q1vcdcmlkEDqnJHd9ZQmo";
    final private String BOT_NAME = "@FunnyHumoristBot";
    Storage storage;

    Bot(){
        storage = new Storage();
    }
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if(update.hasMessage() && update.getMessage().hasText()){
                Message getMes = update.getMessage();
                String chatID = getMes.getChatId().toString();
                String response = handlerMessage(getMes.getText());
                SendMessage outMes = new SendMessage();
                outMes.setChatId(chatID);
                outMes.setText(response);
                execute(outMes);
            }
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String handlerMessage(String text){
        String response;
        if(text.equals("/start") || text.equals("/старт")){
            response = "Вас приветствует бот-юморист. Введите цифру от 0 до 9 и получите анекдот от нашего бота";
        }
        else if(text.length() == 1 && (text.charAt(0)) >= '0' && text.charAt(0)<='9'){
            response = storage.getAnecdote(Integer.parseInt(text));
        }
        else response = "сообщение не распознано";
        return response;
    }
}
