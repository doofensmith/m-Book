package com.softtech.mbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softtech.mbook.Adapter.ABookshelf;
import com.softtech.mbook.Model.MBookshelf;
import com.softtech.mbook.Model.MUser;
import com.softtech.mbook.ViewHolder.VHBookshelf;

public class BookshelfActivity extends AppCompatActivity {

    //deklarasi view
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    //view profile
    TextView namapengguna;
    TextView namaperpustakaan;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<MBookshelf> options;
    FirebaseRecyclerAdapter<MBookshelf, VHBookshelf> adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf_drawer);

        //AUTH
        auth = FirebaseAuth.getInstance();

        //ROOT DATABASE REFERENCE
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(auth.getCurrentUser().getUid());

        //DRAWER LAYOUT FUNCTIONALITY
        //toolbar
        toolbar = findViewById(R.id.ab_toolbar);
        setSupportActionBar(toolbar);
        //add drawer to toolbar
        drawerLayout = findViewById(R.id.ab_drawer);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //MENU DRAWER LAYOUT FUNC
        navigationView = findViewById(R.id.ab_naview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_ab_editprofile:
                        Toast.makeText(BookshelfActivity.this, "Edit Profil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_ab_addbookshelf:
                        add_bookshelf(databaseReference);
                        break;
                    case R.id.menu_ab_setting:
                        Toast.makeText(BookshelfActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_ab_logout:
                        log_out();
                        break;
                }
                return true;
            }
        });

        //UPDATE UI
        update_UI(databaseReference);

        //INFLATING BOOKSHELF ITEM
        //option
        options = new FirebaseRecyclerOptions.Builder<MBookshelf>()
                .setQuery(databaseReference.child("Bookshelf"),MBookshelf.class).build();
        //adapter
        adapter = new ABookshelf(options,BookshelfActivity.this,databaseReference);
        //recycler view
        recyclerView = findViewById(R.id.ab_rv);
        recyclerView.setHasFixedSize(true);
        //LinearLayoutManager manager = new LinearLayoutManager(BookshelfActivity.this);
        //manager.setOrientation(RecyclerView.VERTICAL);
        //recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter!=null){
            adapter.startListening();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter!=null){
            adapter.startListening();
        }

    }

    @Override
    public void onStop() {

        if (adapter!=null){
            adapter.stopListening();
        }

        super.onStop();
    }

    //UPDATE UI
    private void update_UI(DatabaseReference databaseReference) {
        View headerLayout = navigationView.getHeaderView(0);
        namapengguna = headerLayout.findViewById(R.id.ab_tv_namapengguna);
        namaperpustakaan = headerLayout.findViewById(R.id.ab_tv_namaperpustakaan);

        //listener database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //kondisi proses jika tersedia data
                if (snapshot.exists()) {
                    //simpan data kedalam model
                    MUser mUser = snapshot.getValue(MUser.class);
                    namapengguna.setText(mUser.getNama());
                    namaperpustakaan.setText(mUser.getPerpustakaan());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //FUNC EDIT PROFILE

    //FUNC ADD BOOKSHELF
    private void add_bookshelf(DatabaseReference databaseReference) {
        //close drawer
        drawerLayout.closeDrawers();

        //Inflater untuk inflate view dialog add bookshelf
        LayoutInflater inflater = LayoutInflater.from(BookshelfActivity.this);
        View view_add_bookshelf = inflater.inflate(R.layout.dialog_add_bookshelf,null);
        //init view pada dialog add bookshelf
        TextInputEditText ed_label = view_add_bookshelf.findViewById(R.id.dialog_add_bookshelf_ed_label);

        //BUAT DIALOG
        AlertDialog.Builder dialog_add = new AlertDialog.Builder(BookshelfActivity.this);
        dialog_add.setView(view_add_bookshelf);
        dialog_add.setCancelable(false);
        dialog_add.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get text from ed label
                String bookshelf_label = ed_label.getText().toString();

                //kondisi ed label
                if (bookshelf_label.isEmpty()) {
                    AlertDialog.Builder error_message = new AlertDialog.Builder(BookshelfActivity.this);
                    error_message.setTitle("Failed to Add");
                    error_message.setMessage("Bookshelf label can't be empty!");
                    error_message.show();
                }else {
                    //create bookshelf key
                    String bookshelf_key = databaseReference.child("Bookshelf").push().getKey();
                    //bookshelf model
                    MBookshelf mBookshelf = new MBookshelf(bookshelf_key,"",bookshelf_label,0);
                    //save to database
                    databaseReference.child("Bookshelf").child(bookshelf_key).setValue(mBookshelf)
                            .addOnCompleteListener(BookshelfActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        AlertDialog.Builder dialog_error = new AlertDialog.Builder(BookshelfActivity.this);
                                        dialog_error.setTitle("Failed to Add");
                                        dialog_error.setMessage(task.getException().getMessage());
                                        dialog_error.show();
                                    }
                                }
                            });
                }


            }
        });
        dialog_add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add.show();

    }

    //FUNC SETTING

    //FUNC LOGOUT
    private void log_out() {
        //dismiss drawer
        drawerLayout.closeDrawers();

        //show progressdialog
        ProgressDialog progress_logout = new ProgressDialog(BookshelfActivity.this);
        progress_logout.setMessage("Logging out...");
        progress_logout.setCancelable(false);
        progress_logout.show();

        AuthUI.getInstance().signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //dismiss progress dialog
                            progress_logout.dismiss();

                            //change activity
                            Intent intent = new Intent(BookshelfActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            //dismiss progress dialog
                            progress_logout.dismiss();

                            //dismiss drawer
                            drawerLayout.closeDrawers();

                            //show error message
                            AlertDialog.Builder error_message = new AlertDialog.Builder(BookshelfActivity.this);
                            error_message.setTitle("Failed to Log Out");
                            error_message.setMessage(task.getException().getMessage());
                            error_message.show();
                        }
                    }
                });
    }
}