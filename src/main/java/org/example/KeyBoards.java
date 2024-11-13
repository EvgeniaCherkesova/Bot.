package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyBoards {

    public static InlineKeyboardMarkup setNote(){
         InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
         List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
         List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
         InlineKeyboardButton button1 = new InlineKeyboardButton();

         button1.setText("создать заметку");
         button1.setCallbackData("создать заметку");
         rowInline1.add(button1);
         rowsInline.add(rowInline1);
         markupInline.setKeyboard(rowsInline);
         return markupInline;
    }
    public static InlineKeyboardMarkup defaultButton() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();

        button1.setText("по умолчанию");
        button1.setCallbackData("по умолчанию");
        rowInline1.add(button1);
        rowsInline.add(rowInline1);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
