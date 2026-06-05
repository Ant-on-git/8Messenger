package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class UsersActivity extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private RecyclerView recyclerView_users;
    private UsersAdapter usersAdapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // база данных
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
    private List<User> users = new ArrayList<>();
    private String currentUserId;   // id текущего польз. прилетает при вызове активити через Intent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerView_users), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);


        observeViewModel();
        initViews();

        currentUserId = getIntent().getStringExtra("currentUserId");

        usersAdapter.setOnUserClickListener(user -> {   // user - тот пользователь, по которому кликает польз.
            Intent intent = ChatActivity.newIntent(UsersActivity.this, currentUserId, user.getId());
            startActivity( intent );
        });
    }



    private void initViews() {
        recyclerView_users = findViewById(R.id.recyclerView_users);
        usersAdapter = new UsersAdapter();
        recyclerView_users.setAdapter(usersAdapter);
    }


    private void observeViewModel() {
        usersViewModel.getUser().observe(
                this,
                user -> {
                    if (user == null) { startActivity(LoginActivity.newIntent(this)); }
                }
        );

        usersViewModel.getUsers().observe(
                this,
                users -> usersAdapter.setUsers(users)
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
        usersViewModel.setUserOnlineStatus( true );     // если экран активен = пользователь онлайн
    }

    @Override
    protected void onPause() {
        super.onPause();
        usersViewModel.setUserOnlineStatus( false );    // если не активно = офлайн
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuLogOut) {
            usersViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }


    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra("currentUserId", currentUserId);
        return intent;
    }
}