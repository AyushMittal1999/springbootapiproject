package com.example.api.model;




public class Channel {

    String channel_Id;
    String name ;
    double price;
    String genre;

    public Channel(String channel_Id, String name, double price, String genre) {
        this.channel_Id = channel_Id;
        this.name = name;
        this.price = price;
        this.genre = genre;
    }

    public String getChannel_Id() {
        return channel_Id;
    }

    public void setChannel_Id(String channel_Id) {
        this.channel_Id = channel_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
