package com.example.a8messenger;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();

        auth.addAuthStateListener(firebaseAuth -> {
            if(auth.getCurrentUser() != null){
                Log.d("MainActivity", "польз авторизован");
            } else {
                Log.d("MainActivity", "польз не авторизован");
            }
        });


        FirebaseUser currentUser = auth.getCurrentUser();

//         auth.signInWithEmailAndPassword("denisenkoaa88@gmail.com", "123456");
//        auth.sendPasswordResetEmail( currentUser.getEmail() )
//                .addOnSuccessListener(Void -> Log.d("MainActivity", "ссылка для сброса отправлена"))
//                .addOnFailureListener(exc -> Log.d("MainActivity", exc.getMessage()));

    }
}