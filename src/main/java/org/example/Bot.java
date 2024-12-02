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

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Slf4j
public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "8029386665:AAFwHizKwSdMz-Q1vcdcmlkEDqnJHd9ZQmo";
    final private String BOT_NAME = "@FunnyHumoristBot";
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    Bot(){
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
                SendMessage outMes = MessagesHandler.create().handlerMessage(getMes.getText());
                outMes.setChatId(chatID);
                execute(outMes);
            } else if (update.hasCallbackQuery()) {
                String chatID = update.getCallbackQuery().getMessage().getChatId().toString();
                String callBack = update.getCallbackQuery().getData();
                SendMessage message = new SendMessage();

                if (callBack.equals("создать заметку")){
                    message.setText("введите текст заметки начиная с /text");
                }
                message.setChatId(chatID);
                execute(message);
            }
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void scheduleMessage(long chatId, String message, LocalDateTime dateTime) {
        long delay = calculateDelay(dateTime);
        if (delay >= 0) {
            executorService.schedule(() -> sendResponse(chatId, message), delay, TimeUnit.MILLISECONDS);
        } else {
            sendResponse(chatId, "Вы уже пропустили это время.");
        }
    }

    private long calculateDelay(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(now, dateTime).toMillis();
    }

}
