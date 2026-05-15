package com.example.a8messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordViewModel extends ViewModel {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> success = new MutableLiveData<>();

    public void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(Void -> success.setValue(true) )
                .addOnFailureListener(exc -> error.setValue(exc.getMessage()) );
    }

    public LiveData<String> getError() { return error;
    }

    public LiveData<Boolean> getSuccess() { return success;
    }
}
