package com.ireddragonicy.nadma;

public class Staff {
    private String bil;
    private String nama;
    private String jawatan;
    private String noTelefon;
    private String email;
    private boolean isHeader;

    public Staff() {
    }

    public Staff(String bil, String nama, String jawatan, String noTelefon, String email) {
        this.bil = bil;
        this.nama = nama;
        this.jawatan = jawatan;
        this.noTelefon = noTelefon;
        this.email = email;
        this.isHeader = false;
    }

    public String getBil() {
        return bil;
    }

    public String getNama() {
        return nama;
    }

    public String getJawatan() {
        return jawatan;
    }

    public String getNoTelefon() {
        return noTelefon;
    }

    public String getEmail() {
        return email;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}