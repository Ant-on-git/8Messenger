package com.example.a8messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText lastName;
    private EditText howOld;
    private Button signUpButton;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initParams();




        signUpButton.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String nameText = name.getText().toString().trim();
            String lastNameText = lastName.getText().toString().trim();
            int age = Integer.parseInt(howOld.getText().toString().trim());

            auth.createUserWithEmailAndPassword( emailText, passwordText )
                    .addOnSuccessListener( authResult -> startActivity(UsersActivity.newIntent(this)) )
                    .addOnFailureListener( exc -> {
                        Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.d("**************", email.getText().toString() );
                        Log.d("**************", password.getText().toString() );
                    });
        });
    }

    private void initParams() {
        email       = findViewById(R.id.editText_Registration_Email);
        password    = findViewById(R.id.editText_Registration_Password);
        name        = findViewById(R.id.editText_Registration_Name);
        lastName    = findViewById(R.id.editText_Registration_LastName);
        howOld      = findViewById(R.id.editText_Registration_HowOld);
        signUpButton= findViewById(R.id.button_Registration_SignUp);
        auth        = FirebaseAuth.getInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        return intent;
    }
}