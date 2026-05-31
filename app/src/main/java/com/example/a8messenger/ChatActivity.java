package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

        currentUserId = getIntent().getStringExtra("currentUserId");
        otherUserId = getIntent().getStringExtra("otherUserId");

        initViews();

        chatAdapter = new ChatAdapter( currentUserId );


        List<Message> messages = new ArrayList<>();
        for (int i=0; i<20; i++) {
            Message message;
            if (i % 2 == 0) {
                message = new Message(
                        "Text " + i,
                        otherUserId,
                        currentUserId
                );
            } else {
                message = new Message(
                        "Text " + i,
                        currentUserId,
                        otherUserId
                );
            }
            messages.add(message);
        }
        chatAdapter.setMessages(messages);

        recyclerView_chat.setAdapter(chatAdapter);
    }


    private void initViews() {
        textView_chatTitle = findViewById(R.id.textView_chatTitle);
        view_chatOnlineStatus = findViewById(R.id.view_chatOnlineStatus);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        editText_message = findViewById(R.id.editText_message);
        imageView_sendMessage = findViewById(R.id.imageView_sendMessage);
    }


    public static Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("currentUserId", currentUserId);
        intent.putExtra("otherUserId", otherUserId);
        return intent;
    }
}