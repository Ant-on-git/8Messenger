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

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private EditText email;
    private Button resetButton;


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

        auth        = FirebaseAuth.getInstance();
        email       = findViewById(R.id.editText_ResetPassword_Email);
        resetButton = findViewById(R.id.button_ResetPassword_send);

        resetButton.setOnClickListener(v -> {
            auth.sendPasswordResetEmail( email.getText().toString() )
                    .addOnSuccessListener(Void -> Toast.makeText(this, "ОТПРАВЛЕНО", Toast.LENGTH_SHORT).show() )
                    .addOnFailureListener(exc -> Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show() );
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        return intent;
    }
}