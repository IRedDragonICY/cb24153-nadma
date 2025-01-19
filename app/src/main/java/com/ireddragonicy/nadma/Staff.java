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
        this.isHeader=false;
    }

    public String getBil() {
        return bil;
    }

    public void setBil(String bil) {
        this.bil = bil;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJawatan() {
        return jawatan;
    }

    public void setJawatan(String jawatan) {
        this.jawatan = jawatan;
    }

    public String getNoTelefon() {
        return noTelefon;
    }

    public void setNoTelefon(String noTelefon) {
        this.noTelefon = noTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}