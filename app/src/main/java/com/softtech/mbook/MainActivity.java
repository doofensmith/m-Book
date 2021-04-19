package com.softtech.mbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softtech.mbook.Model.MBookshelf;

public class MainActivity extends AppCompatActivity {

    //declare view
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String id_bookshelf;

    //firebase
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET INTENT EXTRAS
        id_bookshelf = getIntent().getExtras().getString("id_bookshelf");

        //AUTH
        auth = FirebaseAuth.getInstance();
        //ROOT REFERENCE
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(auth.getCurrentUser().getUid());

        //TOOLBAR
        //init toolbar
        toolbar = findViewById(R.id.am_toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.am_collapsingtoolbarlayout);

        //UPDATE UI
        update_UI(databaseReference,id_bookshelf);

    }

    private void update_UI(DatabaseReference databaseReference, String id_bookshelf) {
        //FETCH DATA
        databaseReference.child("Bookshelf").child(id_bookshelf)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            MBookshelf mBookshelf = snapshot.getValue(MBookshelf.class);
                            getSupportActionBar().setTitle(mBookshelf.getbookshelf_label());
                            collapsingToolbarLayout.setTitle(mBookshelf.getbookshelf_label());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}