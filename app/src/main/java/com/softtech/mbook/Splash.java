package com.softtech.mbook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //intent untuk pindah activity
        Intent intent;

        //kondisi untuk activity selanjutnya
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            intent = new Intent(this, LoginActivity.class);
        }else {
            intent = new Intent(this, BookshelfActivity.class);
        }
        startActivity(intent);
        finish();

    }
}
