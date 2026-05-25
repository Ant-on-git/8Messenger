package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UsersActivity extends AppCompatActivity {
    private UsersViewModel usersViewModel;
    private RecyclerView recyclerView_users;
    private UsersAdapter usersAdapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // база данных
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Users");


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

        usersViewModel.getUser().observe(
                this,
                user -> {
                    if (user == null) { startActivity( LoginActivity.newIntent(this) ); }
                }
        );


        recyclerView_users = findViewById(R.id.recyclerView_users);
        usersAdapter = new UsersAdapter();
        recyclerView_users.setAdapter(usersAdapter);


        // запись в бд
//        for (int i=0; i<10; i++) {
//            User user = new User("id " + i , "name " + i, "last name " + i, i, true);
//            databaseReference.push().setValue(user);
//        }
        // чтение из бд
        databaseReference.addValueEventListener(new ValueEventListener() {      // чтоб читать надо повесить слушатель
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {          // слушатель на изменение записи
                // onDataChange вызывается когда происходит изменение в таблицк Messages     (databaseReference = firebaseDatabase.getReference("Messages");)
                // т.е. при изменении таблицы Messages вызывается onDataChange, в который прилетает вся таблица Messages в обновленном виде  - snapshot
                for ( DataSnapshot dataSnapshot : snapshot.getChildren() ) {    // snapshot.getChildren() - список всех записей в таблице Messages
                    // dataSnapshot - одна запись в таблице Messages
                    User user = dataSnapshot.getValue(User.class);   // User.class - тип записи в таблице
                    Log.d("UsersActivity", user.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {         // слушатель на ошибку
            }
        });
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


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UsersActivity.class);
        return intent;
    }
}