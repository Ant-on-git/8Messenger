package com.example.a8messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<String> loginError = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {     // слушатель состояния авторизации. меняется при входе-выходе
            if (firebaseAuth.getCurrentUser() != null) { user.setValue( firebaseAuth.getCurrentUser() ); }  // если польз авторизован, засовываем его в лайвдату
        });     // чем полезно - без этого чтоб польз вошел в систему, он должен нажать на логин. с этим, если он уже в системе был - он будет автоматом переброшен со страницы логина
    }

    public void login(String email, String password) {
        // проверка что поля не пустые
        if (email.trim().isEmpty()  || password.trim().isEmpty()) {
            loginError.setValue("Email или пароль не могут быть пустыми");
            return; // Выходим из метода, не вызывая Firebase
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {} )    // т.к. сделали слушатель состояния авторизации, здесь можно оставить пустым (перенесли этот код в слушатель состояния авторизации)
                .addOnFailureListener(esc -> loginError.setValue( esc.getMessage() ) );
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}
