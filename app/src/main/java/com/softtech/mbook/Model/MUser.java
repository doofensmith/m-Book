package com.softtech.mbook.Model;

public class MUser {
    private String nama;
    private String perpustakaan;
    private String foto;

    public MUser() {
        //empty constructor
    }

    public MUser(String nama, String perpustakaan, String foto) {
        this.nama = nama;
        this.perpustakaan = perpustakaan;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPerpustakaan() {
        return perpustakaan;
    }

    public void setPerpustakaan(String perpustakaan) {
        this.perpustakaan = perpustakaan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
