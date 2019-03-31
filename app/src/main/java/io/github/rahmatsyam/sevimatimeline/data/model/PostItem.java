package io.github.rahmatsyam.sevimatimeline.data.model;

public class PostItem {

    private int id;
    private String status, dateStatus;

    public PostItem(int id,  String status, String dateStatus) {
        this.id = id;
        this.status = status;
        this.dateStatus = dateStatus;


    }

    public int getId() {
        return id;
    }


    public String getStatus() {
        return status;
    }

    public String getDateSatus() {
        return dateStatus;
    }


}
