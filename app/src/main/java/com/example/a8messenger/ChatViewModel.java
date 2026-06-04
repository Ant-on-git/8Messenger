package com.example.a8messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSend = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private String currentUserId;
    private String otherUserId;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();;
    private DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("Users");
    private DatabaseReference messagesDatabaseReference = firebaseDatabase.getReference("Messages");


    public LiveData<List<Message>> getMessages() { return messages; }
    public LiveData<User> getOtherUser() { return otherUser; }
    public LiveData<Boolean> getMessageSend() { return messageSend; }
    public LiveData<String> getError() { return error; }


    public ChatViewModel(String currentUserId, String otherUserId) {
        // конструктор ChatViewModel - здесь вешаем слушателей на изменение состояния пользователя, с кот. общаемся
        // и на обновление списка сообщений
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;

        usersDatabaseReference.child( otherUserId ).addValueEventListener(new ValueEventListener() {    // Сообщай мне каждый раз, когда данные этого пользователя изменятся.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {  // прилетает при каком то изменении в записи польз: напр, online \ offline
                User user = snapshot.getValue( User.class );
                if (user == null) { return; }
                otherUser.setValue(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        messagesDatabaseReference.child( currentUserId ).child( otherUserId ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageListFromDb = new ArrayList<>();
                for ( DataSnapshot messageDataSnapshot : snapshot.getChildren() ) {
                    Message message = messageDataSnapshot.getValue( Message.class );
                    if (message == null) { return; }
                    messageListFromDb.add( message );
                }
                messages.setValue( messageListFromDb );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }


    public void sendMessage(Message message) {
        // сохраняем сообщение
        messagesDatabaseReference                   // у отправителя
                .child( message.getSenderId() )     // Messages - id отправителя
                .child( message.getRecieverId() )   // Messages - id отправителя - id получателя
                .push().setValue( message )         // Messages - id отправителя - id получателя - id сообщения
                .addOnSuccessListener( void1 -> {         // если сообщение успешно отправлено (сохранилось у отправителя)
                    messagesDatabaseReference                   // вносим запись в бд у получателя
                            .child( message.getRecieverId() )   // Messages - id получателя
                            .child( message.getSenderId() )     // Messages - id получателя - id отправителя
                            .push().setValue( message )         // Messages - id получателя - id отправителя - id сообщения
                            .addOnSuccessListener(void2 -> messageSend.setValue( true ))   // если все у всех сохранилось - меняем значение лайвдаты
                            .addOnFailureListener( exc -> error.setValue( exc.getMessage() ) );
                } )
                .addOnFailureListener( exc -> error.setValue( exc.getMessage() ) );
    }
}
