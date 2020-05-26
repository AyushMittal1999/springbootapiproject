package com.example.api.controller;

import com.example.api.ApiApplication;
import com.example.api.model.Channel;
import com.example.api.model.Product;

import com.example.api.service.ProductService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.*;

@RestController
public class ProductController {


    @Autowired
    private ProductService service;

    public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    //Get all products
    @GetMapping("/products")
    public ResponseEntity<Apiresponse> getProducts(){
        logger.info("Controller Get All products method called  ");
        try {
            Apiresponse  result = new Apiresponse("200", "Success","Product List returned", service.getProducts());

            logger.info("Controller Get ALl products method Response received returning list  ");

            return new ResponseEntity<Apiresponse>( result  , HttpStatus.OK) ;

        }
        catch ( Exception e ){
            logger.info( " Controller Get ALl products method Exception thrown from service " +e.getMessage());
            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;
        }

    }

    //Get a product with Specified ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Apiresponse>  getProductById(@PathVariable int id){

        try {
            Product prod =  service.getProductById(id);

            logger.info("Controller getProductById method Response received returning product  ");

            Apiresponse  result = new Apiresponse("200", "Success","Product detials returned", Collections.singletonList(prod ));

            return new ResponseEntity<Apiresponse>( result  , HttpStatus.OK) ;

        }
        catch(Exception e ){

            logger.info( " Controller Get ALl products method Exception thrown from service " +e.getMessage());

            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;
        }
    }

    //Add new product
    @PostMapping("/products")
    public ResponseEntity<Apiresponse> addProduct(@RequestBody @Valid Product product , Errors errors){

        //Product entered have some not supported Values
        if( errors.hasErrors()){
            logger.info( " Controller addproduct method invalid product received from user  " );

            return new ResponseEntity<Apiresponse>( exception("Incorrect Input" )  , HttpStatus.BAD_REQUEST) ;
        }

        try{
            Product insertedthis = service.addProduct( product );

            logger.info("Controller addproduct method Response received saved successfully "+insertedthis.getId());

            Apiresponse  result = new Apiresponse("200", "Success "," Product added ", Collections.singletonList(insertedthis));

            return new ResponseEntity<Apiresponse>( result  , HttpStatus.OK) ;
        }
        catch ( Exception e){
            logger.info( " Controller addproduct method Exception thrown from service " +e.getMessage());

            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;

        }

    }

    //Update existing product
    @PutMapping("/products")
    public ResponseEntity<Apiresponse>  updateProduct(@RequestBody @Valid Product product , Errors errors){

        if( errors.hasErrors()){
            logger.info( " Controller updateProduct method invalid product received from user  " );

            return new ResponseEntity<Apiresponse>( exception("Incorrect Input" )  , HttpStatus.BAD_REQUEST) ;
        }
        try{
            Apiresponse  result = new Apiresponse("200", "Success "," Product updated ", Collections.singletonList(service.updateProduct(product)));
            logger.info("Controller updateProduct method Response received saved successfully ");

            return new ResponseEntity<Apiresponse>( result  , HttpStatus.OK) ;

        }
        catch (Exception e){
            logger.info( " Controller updateProduct method Exception thrown from service " +e.getMessage());

            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;

        }


    }

    //Add a channel to an existing product
    @PutMapping("/add_channel/{id}")
    public ResponseEntity<Apiresponse> add_channel(@PathVariable int id , @RequestBody Channel channel){

        try{
            service.add_channel(id , channel);

            logger.info("Controller addchannel method Response channel added successfully ");

            return new ResponseEntity<Apiresponse>( new Apiresponse("200" ,"Success","Channel added Updated the product" ) , HttpStatus.OK ) ;
        }
        catch (Exception e){
            logger.info( " Controller addchannel method Exception thrown from service " +e.getMessage());

            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;
        }
    }

    //Delete a existing product
    @DeleteMapping("/delete/{id1}")
    public ResponseEntity<Apiresponse> delete( @PathVariable int id1 ){

        try{
            service.delete(id1);
            logger.info("Controller delete method Response received deleted successfully ");

            return new ResponseEntity<Apiresponse>( new Apiresponse("200" ,"success" ,"Succesfully deleted" ) , HttpStatus.OK ) ;
        }
        catch (Exception e){

            logger.info( " Controller delete method Exception thrown from service " +e.getMessage());

            return new ResponseEntity<Apiresponse>( exception(e.getLocalizedMessage() )  , HttpStatus.BAD_REQUEST) ;
        }

    }



    public Apiresponse exception( String s ){

        return new Apiresponse( "400" ,"Fail" , "Something went wrong "+s );
    }


    @Getter
    @Setter
    public class Apiresponse{

        String code;
        String status;
        String message;

        List<Product> response;

        public Apiresponse(String status,String status_string, String message, List<Product> product) {
            this.message = message;
            this.status = status_string ;
            this.code = status;
            this.response = product;
        }

        public Apiresponse(String status, String status_string, String message) {
            this.code = status;
            this.status = status_string;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Product> getResponse() {
            return response;
        }

        public void setResponse(List<Product> reponse) {
            this.response = reponse;
        }
    }


}

