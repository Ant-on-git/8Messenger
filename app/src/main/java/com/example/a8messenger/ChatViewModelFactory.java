// "сборщик" для ChatViewModel, чтоб можно было передать параметры в конструктор



package com.example.a8messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {
    private String currentUserId;
    private String otherUserId;


    public ChatViewModelFactory(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // <T extends ViewModel>  -  просто временное имя (шаблон) для типа данных, который станет известен позже.
        //          говорит компилятору: "Вместо T сюда можно подставить только ViewModel или любого её наследника (например, ваш ChatViewModel)
        // @NonNull Class<T> - конкретный «чертеж» (класс) нужной ViewModel, по которому фабрика поймет, какой именно объект создать. т.е. ChatViewModel
        return (T) new ChatViewModel(currentUserId, otherUserId);
        // (T) - приводим возвращаемый объект (new ChatViewModel) к нужному типу ChatViewModel. Нужно для компилятора
        //       типа какой класс на входе прилетел в T, к тому классу и нжно привести то, что возвращаем
    }
}
