package com.example.a8messenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private  static final int VIEW_TYPE_MY_MESSAGE = 1;
    private  static final int VIEW_TYPE_OTHER_MESSAGE = 2;
    private List<Message> messages;
    private String currentUserId;


    public ChatAdapter(String currentUserId) {  // конструктор для присвоения id текущего польозователя
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {  // в зависимости от позиции может возвращать разный тип int
        // используем для определения чье сообщение - мое \ чужое
        // то значение, кот вернет этот метод, прилетит в onCreaateViewHolder   ( int viewType )
        if( messages.get( position ).getSenderId().equals( currentUserId ) ) {
            return VIEW_TYPE_MY_MESSAGE;
        } else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // определяем какой макет использовать в зависимости от типа сообщения
        int message_item_layout;
        if ( viewType == VIEW_TYPE_MY_MESSAGE )  { message_item_layout = R.layout.my_message_item; }
        else                                     { message_item_layout = R.layout.other_message_item; }

        View messageView = LayoutInflater.from( parent.getContext() ).inflate( message_item_layout, parent, false );
        return new ChatViewHolder( messageView );
    }



    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get( position );
        holder.textViewMessage.setText( message.getText() );
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }



    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textView_message);
        }
    }
}
