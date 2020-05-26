package com.example.api.service;

import com.example.api.controller.ProductController;
import com.example.api.model.Channel;
import com.example.api.model.Product;
import com.example.api.repository.ProductRepository;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository repository;

    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    //Get all products
    public List<Product> getProducts() throws MongoException {

        logger.info("Service getProduct method called ");

        return  repository.findAll();

    }


    //Get a product with Specified ID
    public Product getProductById(@PathVariable int id){

        logger.info("Service getProductByID method called with ID "+ String.valueOf(id) );

        return repository.findById(id).get();
    }



    //Add new product
    public Product addProduct   (Product product ){

        logger.info("Service addProduct method called ");


        //Check whether product is already there
        if( repository.findById(product.getId() ).isPresent() ){
            logger.info("Service addProduct method Product already existed exception thrown ");

            throw  new NullPointerException( "Product Already Existed" );
        }

        //Insert product in database
        logger.info("Service addProduct method Saving product with ID " + product.getId()+" in database");
        return repository.insert(product);

    }


    public Product  updateProduct( Product product){

        //Check whether product is already there
        if( ! repository.findById(product.getId() ).isPresent() ){

            logger.warn("Service updateproduct method Product Id not found in database Cannot update it");

            throw new NullPointerException( "Product was Not in database ");

        }

        //overwrite the product
        logger.info("Service updateproduct method Saving product " + product.getId() + " in database");

        return repository.save(product);

    }

    //Add a channel to an existing product
    public void add_channel( int id ,  Channel channel){

        if( repository.findById(id).isPresent() ) {

            logger.info("Service add_channel method product found in database");

            //Product Found
            Product existed = repository.findById(id).get() ;

            Map<String , List<Channel> > now = existed.getChannelList();

            if( ! now.containsKey( channel.getGenre() ) ){
                // Add a new Genre
                now.put( channel.getGenre() , new ArrayList<>() );
            }

            // Check Channel with same channel is already there if , then throw error
            for(  Channel p :existed.getChannelList().get(channel.getGenre())   ){
                if( p.getChannel_Id().equalsIgnoreCase(channel.getChannel_Id() ) ) {

                    logger.warn("Service add_channel method Product have channel already ERROR THROWING ");

                    throw new NullPointerException("Product have channel already  Exist" );
                }
            }

            // Add a channel to a genre category
            now.get(channel.getGenre()).add(channel);
            existed.setChannelcount( existed.getChannelcount() +1 );

            //Overwrite the product records
            logger.info("Service add_channel method Saving product with added channel in database");

            repository.save( existed);

        }
        else{

            //Product Doesnot exist in database
            logger.warn("Service updateproduct method  product NOT in database thro NULL POINTER EXCEPTION");

            throw new NullPointerException("Product Doesnot Exist" );
        }

    }

    //Delete an existing Channel
    public void delete(  int id ){

        // Check whether product exist
        if( repository.findById(id).isPresent() ){
            logger.info("Service delete method Product found deleting in database ");

            repository.delete( repository.findById(id).get() );
        }
        else{
            logger.info("Service delete method product NOT in database throw NULL POINTER EXCEPTION");

            throw new NullPointerException("No product existed");
        }

    }



}
