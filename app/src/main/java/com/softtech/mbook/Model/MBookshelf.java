package com.softtech.mbook.Model;

public class MBookshelf {

    //ATTRIBUT
    private String id_bookshelf;
    private String bookshelf_img;
    private String bookshelf_label;
    private int book_count;

    //CONSTRUCTOR
    public MBookshelf() {
        //empty constructor
    }

    public MBookshelf(String id_bookshelf, String bookshelf_img, String bookshelf_label, int book_count) {
        this.id_bookshelf = id_bookshelf;
        this.bookshelf_img = bookshelf_img;
        this.bookshelf_label = bookshelf_label;
        this.book_count = book_count;
    }

    //GETTER AND SETTER

    public String getBookshelf_img() {
        return bookshelf_img;
    }

    public void setBookshelf_img(String bookshelf_img) {
        this.bookshelf_img = bookshelf_img;
    }

    public String getId_bookshelf() {
        return id_bookshelf;
    }

    public void setId_bookshelf(String id_bookshelf) {
        this.id_bookshelf = id_bookshelf;
    }

    public String getbookshelf_label() {
        return bookshelf_label;
    }

    public void setbookshelf_label(String bookshelf_label) {
        this.bookshelf_label = bookshelf_label;
    }

    public int getBook_count() {
        return book_count;
    }

    public void setBook_count(int book_count) {
        this.book_count = book_count;
    }
}
