package com.example.a8messenger;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<String> loginError = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();   // здесь храним пользователя (и подписываемся на его в активити)
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();;
    private DatabaseReference usersDatabaseReference = firebaseDatabase.getReference("Users");

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth -> {     // слушатель состояния авторизации. меняется при входе-выходе
            if (firebaseAuth.getCurrentUser() != null) { user.setValue( firebaseAuth.getCurrentUser() ); }  // если польз авторизован, засовываем его в лайвдату
        });     // далее в активити, если user есть - переход на юзерс активити
    }


    public void signUp(String email, String password, String name, String lastName, String age) {
        if (email.trim().isEmpty()  || password.trim().isEmpty()) {
            loginError.setValue("Email или пароль не могут быть пустыми");
            return;
        }
        int intAge;
        try {intAge = Integer.parseInt(age);} catch (NumberFormatException e) {
            loginError.setValue("Возраст должен быть числом");
            return;
        }
        // createUserWithEmailAndPassword() в Firebase не только создаёт аккаунт, но и автоматически логинит пользователя после успешной регистрации.
        auth.createUserWithEmailAndPassword( email, password )  // если польз зарегался -> срабатывает addAuthStateListener (см. выше). и затем вход в систему -> переход на юзерс активити
                .addOnSuccessListener( authResult -> {
                    // создаем пользователя
                    FirebaseUser currentUser = authResult.getUser();
                    if (currentUser == null) {
                        return;
                    }
                    User user = new User(
                            currentUser.getUid(),   // получаем id нового польз., присвоенный фаербейсом
                            name,
                            lastName,
                            intAge,
                            true
                    );
                    usersDatabaseReference.child( currentUser.getUid() ).setValue( user ); // добавляем пользователя в бд по ключу = id польз.
                })
                .addOnFailureListener( exc -> loginError.setValue( exc.getMessage() ) );
    }


    public LiveData<String> getLoginError() {
        return loginError;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

}
