package com.example.a8messenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private onUserClickListener onUserClickListener;


    public void setOnUserClickListener(onUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }


    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View user_view = LayoutInflater.from(parent.getContext()).inflate( R.layout.user_item, parent, false );
        return new ViewHolder( user_view );
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s %d", user.getName(), user.getLastName(), user.getAge());
        holder.textView_userInfo.setText( userInfo );

        int backgroundResId;
        if (user.getOnline()) {
            backgroundResId = R.drawable.circle_online;
        } else {
            backgroundResId = R.drawable.circle_offline;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundResId);
        holder.view_isOnline.setBackground( background );

        holder.itemView.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick( user );
            }
        });
    }


    @Override
    public int getItemCount() { return users.size(); }



    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_userInfo;
        private View view_isOnline;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_userInfo = itemView.findViewById(R.id.textView_UserInfo);
            view_isOnline = itemView.findViewById(R.id.view_isOnline);
        }
    }


    interface onUserClickListener {
        void onUserClick(User user);
    }
}
