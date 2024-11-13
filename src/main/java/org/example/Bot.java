package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Slf4j
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
                SendMessage outMes = handlerMessage(getMes.getText());
                outMes.setChatId(chatID);
                execute(outMes);
            } else if (update.hasCallbackQuery()) {
                String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
                String callBack = update.getCallbackQuery().getData();
                SendMessage message = new SendMessage();

                if (callBack.equals("создать заметку")){
                    message.setText("введите текст заметки начиная с /text");
                }
                else if (callBack.equals("по умолчанию")){
                    message.setText("заметка создана");
                    message.setReplyMarkup(KeyBoards.setNote());
                }
                message.setChatId(chatID);
                execute(message);
            }
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage handlerMessage(String text){
        SendMessage message = new SendMessage();
        InlineKeyboardMarkup keyBoard;
        String response = "";
        if(text.equals("/start") || text.equals("/старт")){
            response = "Вас приветствует бот-хранитель ваших заметок";
            keyBoard = KeyBoards.setNote();
            message.setReplyMarkup(keyBoard);
        }

        else if (text.startsWith("/text")){
            response = "введите дату напоминания начиная с /date";
        }

        else if (text.startsWith("/date")){
            response = "Введите время начиная с /time. По умолчанию время 10:00";
            keyBoard = KeyBoards.defaultButton();
            message.setReplyMarkup(keyBoard);
        }
        else {
            response = "сообщение не распознано";
            keyBoard = null;
        }

        message.setText(response);
        return message;
    }
}
