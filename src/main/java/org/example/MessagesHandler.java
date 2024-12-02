package org.example;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessagesHandler {
    private static final MessagesHandler handler = new MessagesHandler();
    private String textNote;
    private LocalDateTime date;

    private MessagesHandler() {
    }

    public static MessagesHandler create(){
        return handler;
    }
    @SneakyThrows
    public SendMessage handlerMessage(String text) {
        SendMessage message = new SendMessage();
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
            message.setText("");
        } else {
            message.setText("сообщение не распознано");
        }

        if(textNote != null && date != null){
            Note note = new Note();
            note.note = textNote;
            note.dateTime = date;
            if (DataBaseHandler.addDataBase(note)){
                note.note = null;
                note.dateTime = null;
            }
        }

        return message;
    }
}
