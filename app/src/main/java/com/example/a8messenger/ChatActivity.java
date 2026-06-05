package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private TextView textView_chatTitle;
    private View view_chatOnlineStatus;
    private RecyclerView recyclerView_chat;
    private EditText editText_message;
    private ImageView imageView_sendMessage;
    private ChatAdapter chatAdapter;
    private String currentUserId;
    private String otherUserId;
    private ChatViewModel chatViewModel;
    private ChatViewModelFactory chatViewModelFactory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        currentUserId = getIntent().getStringExtra("currentUserId");
        otherUserId = getIntent().getStringExtra("otherUserId");


        chatViewModelFactory = new ChatViewModelFactory(currentUserId, otherUserId);
        chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);

        observeViewModel();

        chatAdapter = new ChatAdapter( currentUserId );
        recyclerView_chat.setAdapter( chatAdapter );

        imageView_sendMessage.setOnClickListener(v -> {
            Message message = new Message(
                    editText_message.getText().toString().trim(),
                    currentUserId,
                    otherUserId
            );
            chatViewModel.sendMessage( message );
        });
    }


    private void initViews() {
        textView_chatTitle = findViewById(R.id.textView_chatTitle);
        view_chatOnlineStatus = findViewById(R.id.view_chatOnlineStatus);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        editText_message = findViewById(R.id.editText_message);
        imageView_sendMessage = findViewById(R.id.imageView_sendMessage);
    }


    private void observeViewModel() {
        chatViewModel.getMessages().observe(this, messages -> {
            chatAdapter.setMessages( messages );
        });

        chatViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        chatViewModel.getMessageSend().observe(this, messageSand -> {
            if (messageSand) {  // если сообщение отправлено - очистить поле  ввода
                editText_message.setText("");
            }
        });

        chatViewModel.getOtherUser().observe(this, user -> {
            textView_chatTitle.setText( user.getName() + " " + user.getLastName() );

            int backgroundResId;
            if (user.getOnline()) {
                backgroundResId = R.drawable.circle_online;
            } else {
                backgroundResId = R.drawable.circle_offline;
            }
            Drawable background = ContextCompat.getDrawable(ChatActivity.this, backgroundResId);
            view_chatOnlineStatus.setBackground( background );
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.setUserOnlineStatus( true );     // если экран активен = пользователь онлайн
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatViewModel.setUserOnlineStatus( false );    // если не активно = офлайн
    }


    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("currentUserId", currentUserId);
        intent.putExtra("otherUserId", otherUserId);
        return intent;
    }
}