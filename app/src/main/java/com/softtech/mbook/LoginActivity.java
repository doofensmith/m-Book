package com.softtech.mbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //view
    TextView text_register;
    TextInputEditText ed_email;
    TextInputEditText ed_password;
    Button bt_login;

    //firebase
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //AUTH
        auth = FirebaseAuth.getInstance();

        //init view
        ed_email = findViewById(R.id.al_ed_email);
        ed_password = findViewById(R.id.al_ed_password);

        //LOGIN FUNC
        //init button
        bt_login = findViewById(R.id.al_bt_login);
        //onclick
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edit text
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                //edit text condition
                if (email.isEmpty()) {
                    ed_email.setError("Wajib diisi!");
                }else {
                    //show dialog logging in... (loading)
                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    //PROSES LOGIN
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //dismiss progressdialog
                                        progressDialog.dismiss();

                                        //pindah activity
                                        Intent intent = new Intent(LoginActivity.this,BookshelfActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        //dismiss progressdialog
                                        progressDialog.dismiss();

                                        //show error message
                                        AlertDialog.Builder errordialog = new AlertDialog.Builder(LoginActivity.this);
                                        errordialog.setTitle("Login Failed");
                                        errordialog.setMessage(task.getException().getMessage());
                                        errordialog.show();
                                    }
                                }
                            });
                }
            }
        });

        //TEXT REGISTER FUNC
        //init view
        text_register = findViewById(R.id.al_tv_register);
        //on click
        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}