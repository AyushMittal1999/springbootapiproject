package com.example.api.model;


import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@ToString


@Document( collection="product")
public class Product {

    @Id
    @Min(value = 1,message = "Enter A valid ID")
    private int id;
    @NotNull(message = "Enter a valid Name ")
    private String name;
    @Min(value = 0,message = "Enter Valid Price")
    private double price;
    private int channelcount;
    private Map<String ,List<Channel> > channelList;

    public Product(){
      channelList = new HashMap<>();
    }

    public Product(@Min(value = 1, message = "Enter A valid ID") int id, @NotNull(message = "Enter a valid Name ") String name, @Min(value = 0, message = "Enter Valid Price") double price, int channelcount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.channelcount = channelcount;
    }

    public Product(@Min(value = 1, message = "Enter A valid ID") int id, @NotNull(message = "Enter a valid Name ") String name, @Min(value = 0, message = "Enter Valid Price") double price, int channelcount, Map<String, List<Channel>> channelList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.channelcount = channelcount;
        this.channelList = channelList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getChannelcount() {
        return channelcount;
    }

    public void setChannelcount(int channelcount) {
        this.channelcount = channelcount;
    }

    public Map<String, List<Channel>> getChannelList() {
        return channelList;
    }

    public void setChannelList(Map<String, List<Channel>> channelList) {
        this.channelList = channelList;
    }




}
