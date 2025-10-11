package com.example.apprecordatorio.entidades;

public class imagen {
    private int ID;
    private int recordatorioID;
    private String imgURL;

    public imagen() {
    }

    public imagen(int recordatorioID, String imgURL, int ID) {
        this.recordatorioID = recordatorioID;
        this.imgURL = imgURL;
        this.ID = ID;
    }

    public int getRecordatorioID() {
        return recordatorioID;
    }

    public void setRecordatorioID(int recordatorioID) {
        this.recordatorioID = recordatorioID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "imagen{" +
                "ID=" + ID +
                ", recordatorioID=" + recordatorioID +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }
}
