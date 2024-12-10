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
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Slf4j
public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "8090487613:AAF-PaoAj1VaazRVLEHgB8cg30_Pj0E0HIs";
    final private String BOT_NAME = "@notesStorageBot";
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private String textNote = null;
    private LocalDateTime date = null;

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
                SendMessage message = new SendMessage();
                String text = getMes.getText();
                InlineKeyboardMarkup keyBoard;
                if (text.equals("/start") || text.equals("/старт")) {
                    message.setText("Вас приветствует бот-хранитель ваших заметок");
                    keyBoard = KeyBoards.setNote();
                    message.setReplyMarkup(keyBoard);
                } else if (text.startsWith("/text")) {
                    this.textNote = text.replace ("/text", "");
                    message.setText("введите дату и время напоминания в формате dd.mm.yyyy hh:mm начиная с /date");
                } else if (text.startsWith("/date")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    this.date = LocalDateTime.parse(text.replace ("/date ", ""), formatter);
                    message.setText("заметка создана");
                } else {
                    message.setText("сообщение не распознано");
                }
                message.setChatId(chatID);
                execute(message);
                if(textNote != null && date != null){
                    scheduleMessage(chatID,textNote, date);
                    textNote = null;
                    date = null;    
                }
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

    public void scheduleMessage(String chatId, String message, LocalDateTime dateTime) {
        long delay = calculateDelay(dateTime);
        if (delay >= 0) {
            executorService.schedule(() -> sendResponse(chatId, message), delay, TimeUnit.MILLISECONDS);
        } else {
            sendResponse(chatId, "Вы уже пропустили это время. Начните с начала");
        }
    }

    private long calculateDelay(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(now, dateTime).toMillis();
    }

    private void sendResponse(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
