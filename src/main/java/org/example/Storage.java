package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    HashMap<Integer, String> base = new HashMap<>();
    Storage(){
        base.put(0, "— Послала своего за картошкой, а его сбила машина. — Ужас! И что ты теперь будешь делать? — Не знаю. Рис, наверное");
        base.put(1, "психолог: здравствуйте, по какому поводу вы обратились ко мне\n" +
                "клиент: у меня раздвоение личности\n" +
                "психолог: тогда платите за двоих");
        base.put(2, "Студент технического ВУЗа решил поступать в семинарию. На вступительном экзамене батюшка у него спрашивает:\n" +
                "— Сын мой, что такое Божья сила?\n" +
                "— Это Божья масса на Божье ускорение...");
    }

    String getAnecdote(int ind){
        return base.get(ind);
    }
}
