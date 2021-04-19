package com.softtech.mbook.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softtech.mbook.R;

public class VHBookshelf extends RecyclerView.ViewHolder {

    //DECLARE VIEW
    public LinearLayout bookshelf_container;
    public ImageView bookshelf_img;
    public TextView bookshelf_label;
    public TextView bookshelf_count;
    public ImageView bookshelf_option;

    public VHBookshelf(@NonNull View itemView) {
        super(itemView);

        //INIT VIEW
        bookshelf_container = itemView.findViewById(R.id.item_bookshelf_container);
        bookshelf_img = itemView.findViewById(R.id.item_bookshelf_img);
        bookshelf_label = itemView.findViewById(R.id.item_bookshelf_label);
        bookshelf_count = itemView.findViewById(R.id.item_bookshelf_count);
        bookshelf_option = itemView.findViewById(R.id.item_bookshelf_option);
    }
}
