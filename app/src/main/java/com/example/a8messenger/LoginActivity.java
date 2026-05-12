package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
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
            v.setPadding(
                    systemBars.left + v.getPaddingLeft(),
                    systemBars.top + v.getPaddingTop(),
                    systemBars.right + v.getPaddingRight(),
                    systemBars.bottom + v.getPaddingBottom());
            return insets;
        });

        initParams();

        // вход в систему
        buttonLogin.setOnClickListener(v -> {
            String email    = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            auth.signInWithEmailAndPassword( email, password )
                    .addOnSuccessListener( authResult ->  startActivity(UsersActivity.newIntent(this)) )
                    .addOnFailureListener( exc -> Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show() );
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

    private void initParams() {
        editTextEmail           = findViewById(R.id.editTextEmail);
        editTextPassword        = findViewById(R.id.editTextPassword);
        buttonLogin             = findViewById(R.id.buttonLogin);
        textViewResetPassword   = findViewById(R.id.textViewResetPassword);
        textViewRegister        = findViewById(R.id.textViewRegister);
        auth = FirebaseAuth.getInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}