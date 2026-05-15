package com.example.a8messenger;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<String> loginError = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {     // слушатель состояния авторизации. меняется при входе-выходе
            if (firebaseAuth.getCurrentUser() != null) { user.setValue( firebaseAuth.getCurrentUser() ); }  // если польз авторизован, засовываем его в лайвдату
        });     // видимо шоб зареганный польз не мог создавать кучу акков
    }


    public void signUp(String email, String password, String name, String lastName, int age) {
        auth.createUserWithEmailAndPassword( email, password )  // если польз зарегался -> срабатывает addAuthStateListener (см. выше). и затем вход в систему -> переход на юзерс активити
                .addOnFailureListener( exc -> loginError.setValue( exc.getMessage() ) );
    }


    public LiveData<String> getLoginError() {
        return loginError;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

}
