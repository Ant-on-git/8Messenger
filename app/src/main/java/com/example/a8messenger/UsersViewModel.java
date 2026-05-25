package com.example.a8messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();


    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> user.setValue( firebaseAuth.getCurrentUser() ) );
        // если не авторизован user = null, нсли авторизован user = авторизованный польз
    }

    public void logout() { auth.signOut(); }


    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}
