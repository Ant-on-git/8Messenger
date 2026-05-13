package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewResetPassword;
    private TextView textViewRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(                                                   // шоб работал падинг на корневом элементе у ConstraintLayout
                    systemBars.left + v.getPaddingLeft(),                   // шоб работал падинг на корневом элементе у ConstraintLayout
                    systemBars.top + v.getPaddingTop(),                     // шоб работал падинг на корневом элементе у ConstraintLayout
                    systemBars.right + v.getPaddingRight(),                 // шоб работал падинг на корневом элементе у ConstraintLayout
                    systemBars.bottom + v.getPaddingBottom());              // шоб работал падинг на корневом элементе у ConstraintLayout
            return insets;
        });

        initParams();
        ovserveViewModel();
        setOnClickListeners();
    }

    private void initParams() {
        editTextEmail           = findViewById(R.id.editTextEmail);
        editTextPassword        = findViewById(R.id.editTextPassword);
        buttonLogin             = findViewById(R.id.buttonLogin);
        textViewResetPassword   = findViewById(R.id.textViewResetPassword);
        textViewRegister        = findViewById(R.id.textViewRegister);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void ovserveViewModel() {
        // подписываемся на объекты LiveData из LoginViewModel
        loginViewModel.getLoginError().observe(
                this,
                errorMessage -> {
                    if (errorMessage != null) { Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show(); }
                });
        loginViewModel.getUser().observe(
                this,
                user -> {
                    if (user != null) {
                        startActivity( UsersActivity.newIntent(this) );     // если пользователь авторизован переходим в активити Users
                        finish();  // закрываем логин активити чтоб нельзя было вернуться при нажатии на кнопку назад
                    }
                }
        );
    }

    private void setOnClickListeners() {
        // вынесли навешивание слушателей кликов в отдельный метод
        // вход в систему
        buttonLogin.setOnClickListener(v -> {
            String email    = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            loginViewModel.login(email, password);
        });

        // сброс пароля
        textViewResetPassword.setOnClickListener(v -> {
            startActivity( ResetPasswordActivity.newIntent(this) );
        });

        // регистрация
        textViewRegister.setOnClickListener(v -> {
            startActivity( RegistrationActivity.newIntent(this) );
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   // очищаем стек чтоб нельзя было вернуться на страницу, с которой попал на стр логина
        // CLEAR_TASK Полностью уничтожает весь старый стек.
        // NEW_TASK Создаёт новый чистый task.
        return intent;
    }
}