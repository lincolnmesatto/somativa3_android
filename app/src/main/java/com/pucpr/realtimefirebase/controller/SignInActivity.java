package com.pucpr.realtimefirebase.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pucpr.realtimefirebase.R;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    EditText editTextLogin;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextLogin = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnCadastrarClicked(View view){
        String email = editTextLogin.getText().toString();
        String senha = editTextPassword.getText().toString();

        createUser(email, senha);
    }

    public void createUser(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this,"User created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignInActivity.this,"User couldnt be created", Toast.LENGTH_LONG).show();
                            Log.e("FIREBASEAUTH", "Create Error"+task.getException().toString());
                        }
                    }
                });
    }
}