package com.softtech.mbook.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.softtech.mbook.BookshelfActivity;
import com.softtech.mbook.MainActivity;
import com.softtech.mbook.Model.MBookshelf;
import com.softtech.mbook.R;
import com.softtech.mbook.ViewHolder.VHBookshelf;

public class ABookshelf extends FirebaseRecyclerAdapter<MBookshelf, VHBookshelf> {

    Context context;
    DatabaseReference databaseReference;

    public ABookshelf(@NonNull FirebaseRecyclerOptions<MBookshelf> options, Context context, DatabaseReference databaseReference) {
        super(options);
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @Override
    protected void onBindViewHolder(@NonNull VHBookshelf holder, int position, @NonNull MBookshelf model) {

        //BOOKSHELF LABEL
        holder.bookshelf_label.setText(model.getbookshelf_label());

        //BOOKSHELF COUNT
        holder.bookshelf_count.setText("Book count : "+model.getBook_count());

        //BOOKSHELF IMG
        //library

        //BOOKSHELF OPTION
        holder.bookshelf_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CREATE MENU
                PopupMenu menu = new PopupMenu(holder.bookshelf_option.getContext(),holder.bookshelf_option, Gravity.END);
                menu.getMenuInflater().inflate(R.menu.menu_item_bookshelf,menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_bs_edit:
                                menu_edit(databaseReference,model,context);
                                break;
                            case R.id.menu_item_bs_delete:
                                menu_delete(databaseReference,model);
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

        //BOOKSHELF CONTAINER
        holder.bookshelf_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent +put extra
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("id_bookshelf",model.getId_bookshelf());
                context.startActivity(intent);
            }
        });

    }

    //POP UP MENU EDIT
    private void menu_edit(DatabaseReference databaseReference, MBookshelf model, Context context) {
        //Inflater untuk inflate view dialog add bookshelf
        LayoutInflater inflater = LayoutInflater.from(context);
        View view_add_bookshelf = inflater.inflate(R.layout.dialog_add_bookshelf,null);
        //init view pada dialog add bookshelf
        TextView title = view_add_bookshelf.findViewById(R.id.dialog_add_bookshelf_title);
        TextInputEditText ed_label = view_add_bookshelf.findViewById(R.id.dialog_add_bookshelf_ed_label);

        //set title and text ed label
        title.setText("Edit Bookshelf");
        ed_label.setText(model.getbookshelf_label());

        //BUAT DIALOG
        AlertDialog.Builder dialog_add = new AlertDialog.Builder(context);
        dialog_add.setView(view_add_bookshelf);
        dialog_add.setCancelable(false);
        dialog_add.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get text from ed label
                String bookshelf_label = ed_label.getText().toString();

                //kondisi ed label
                if (bookshelf_label.isEmpty()) {
                    AlertDialog.Builder error_message = new AlertDialog.Builder(context);
                    error_message.setTitle("Failed to Edit");
                    error_message.setMessage("Bookshelf label can't be empty!");
                    error_message.show();
                }else {
                    //bookshelf model
                    MBookshelf mBookshelf = new MBookshelf(model.getId_bookshelf(),"",bookshelf_label,0);
                    //save to database
                    databaseReference.child("Bookshelf").child(model.getId_bookshelf()).child("bookshelf_label").setValue(bookshelf_label)
                            .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        AlertDialog.Builder dialog_error = new AlertDialog.Builder(context);
                                        dialog_error.setTitle("Failed to Edit");
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

    //POP UP MENU DELETE
    private void menu_delete(DatabaseReference databaseReference, MBookshelf model) {
        //ref yang ingin dihapus
        databaseReference.child("Bookshelf").child(model.getId_bookshelf()).removeValue();
    }

    @NonNull
    @Override
    public VHBookshelf onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //SATU tipe
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookshelf,parent,false);
        return new VHBookshelf(view);

//        //DIFF type
//        View view = null;
//        RecyclerView.ViewHolder viewHolder = null;
//        switch (viewType) {
//            case 0:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookshelf,parent,false);
//                viewHolder = new VHBookshelf(view);
//            case 1:
//
//
//        }
//        return viewHolder;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position%2==0) {
//            return 0;
//        }else {
//            return 1;
//        }
//    }
}
