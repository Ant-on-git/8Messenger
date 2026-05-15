package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private EditText email;
    private Button resetButton;
    private ResetPasswordViewModel resetPasswordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left + v.getPaddingLeft(),
                    systemBars.top + v.getPaddingTop(),
                    systemBars.right + v.getPaddingRight(),
                    systemBars.bottom + v.getPaddingBottom());
            return insets;
        });

        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        email       = findViewById(R.id.editText_ResetPassword_Email);
        resetButton = findViewById(R.id.button_ResetPassword_send);

        observeViewModel();

        resetButton.setOnClickListener(v -> {
            resetPasswordViewModel.resetPassword(email.getText().toString());
        });
    }


    private void observeViewModel() {
        resetPasswordViewModel.getError().observe(
                this,
                errorMessage -> {
                    if (errorMessage != null) {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        resetPasswordViewModel.getSuccess().observe(
                this,
                success -> {
                    if (success) {
                        Toast.makeText(this, "Ссылка для сброса отправлена", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        return intent;
    }
}