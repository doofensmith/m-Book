package com.softtech.mbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtech.mbook.Model.MUser;

public class RegisterActivity extends AppCompatActivity {

    //deklarasi view
    TextView text_login;
    TextInputEditText ed_email, ed_pw, ed_conpw;
    Button bt_register;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //AUTH
        auth = FirebaseAuth.getInstance();

        //init view
        ed_email = findViewById(R.id.ar_ed_email);
        ed_pw = findViewById(R.id.ar_ed_pw);
        ed_conpw = findViewById(R.id.ar_ed_conpw);

        //REGISTER FUNC
        //button register
        bt_register = findViewById(R.id.ar_bt_register);
        //onclick
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edittext
                String email = ed_email.getText().toString();
                String pw = ed_pw.getText().toString();
                String conpw = ed_conpw.getText().toString();

                //kondisi edtext
                if (email.isEmpty()) {
                    ed_email.setError("Harus diisi!");
                }else if (pw.isEmpty()) {
                    ed_pw.setError("Harus diisi!");
                }else if (!pw.equals(conpw)) {
                    ed_conpw.setError("Konfirmasi password harus sama!");
                }else if (pw.length()<6) {
                    ed_pw.setError("Minimal 6 karakter!");
                }else {
                    //progress dialog
                    ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setMessage("Creating account...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    //PROSES REGISTER
                    auth.createUserWithEmailAndPassword(email,pw)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //simpan data awal pengguna
                                        //database reference
                                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                                .child(auth.getCurrentUser().getUid());
                                        //prepare data to model
                                        MUser mUser = new MUser("Pengguna","Perpustakaan","");
                                        //save to database
                                        databaseReference.setValue(mUser);

                                        //dismiss progress dialog
                                        progressDialog.dismiss();

                                        //change activity
                                        Intent intent = new Intent(RegisterActivity.this,BookshelfActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        //dismiss progress dialog
                                        progressDialog.dismiss();

                                        //show error message
                                        AlertDialog.Builder errormessage = new AlertDialog.Builder(RegisterActivity.this);
                                        errormessage.setTitle("Failed");
                                        errormessage.setMessage(task.getException().getMessage());
                                        errormessage.show();
                                    }
                                }
                            });

                }

            }
        });

        //TEXT LOGIN FUNC
        //init view
        text_login = findViewById(R.id.ar_tv_login);
        //on click
        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}