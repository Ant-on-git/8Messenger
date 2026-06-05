package com.example.a8messenger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();    // список польз из дб
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // база данных
    private DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("Users");  // таблица Users из дб


    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> user.setValue( firebaseAuth.getCurrentUser() ) );
        // если не авторизован user = null, нсли авторизован user = авторизованный польз


        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();   // получаем текущего пользователя для проверки, чтоб не внести в список
                if (currentUser == null) { return; }                // на всякий

                List<User> usersFromDb = new ArrayList<>();
                for ( DataSnapshot usersDataSnapshot : snapshot.getChildren() ) {
                    User user = usersDataSnapshot.getValue(User.class);
                    if (user == null) { return; }
                    if (user.getId().equals( currentUser.getUid() )) { continue; }  // если польз из бд = текущий польз, пропускаем его
                    usersFromDb.add(user);
                }
                users.setValue( usersFromDb );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UsersActivity", error.getMessage());
            }
        });
    }

    public void logout() { auth.signOut(); }

    public LiveData<FirebaseUser> getUser() { return user; }
    public LiveData<List<User>> getUsers() { return users; }


    public void setUserOnlineStatus(Boolean isOnline) {
        // получаем польз из бд (по id), получаем заначение его поля online и устанавливаем что нужно
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) { return; }
        usersDatabaseReference.child( currentUser.getUid() ).child("online").setValue( isOnline );
    }
}
