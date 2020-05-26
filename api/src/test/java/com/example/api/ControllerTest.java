package com.example.api;


import com.example.api.controller.ProductController;
import com.example.api.model.Channel;
import com.example.api.model.Product;
import com.example.api.repository.ProductRepository;
import com.example.api.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.*;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest( ProductController.class)
class ControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductService service1;

    @MockBean
    private ProductRepository repository;



    @Test
    void getProducts() throws Exception {


        Map<String , List<Channel>> m = new HashMap<>();
        m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );
        List<Product> list =new ArrayList<>();
        list.add (new Product(1,"ayush",10.0, 2  ,m  ));
        list.add (new Product(2,"second",2.0,3 , new HashMap<>() ) );


        when( service1.getProducts() ).thenReturn(list) ;

      //  System.out.println( productController.getProducts().getBody().getReponse().get(0) );

        MvcResult result =  mvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();


        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );

        assertEquals( "200", o.getString("code") );

    }


    @Test
    void getProductById() throws Exception {


        Map<String , List<Channel>> m = new HashMap<>();
        m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );

        Product p =new Product(1,"ayush",10.0, 2  ,m  )  ;


        when( service1.getProductById(1) ).thenReturn(p) ;

        MvcResult result =  mvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();



        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( p.getName(), new JSONObject( o.getJSONArray("response").get(0).toString() ).getString("name")  );

    }

    @Test
    void getProductById_withexception() throws Exception {

        when( service1.getProductById(1) ).thenThrow( new NullPointerException("ID not found" ) ) ;

        MvcResult result =  mvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();


        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "400", o.getString("code") );

    }

    @Test
    void delete_withException() throws Exception {

       Mockito.doThrow(new NullPointerException("Product Not Found") ).when( service1).delete(1);

        MvcResult result =  mvc.perform(delete("/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();


        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "400", o.getString("code") );

    }


    @Test
    void deletetest() throws Exception {

        Mockito.doNothing( ).when( service1).delete(1);

        MvcResult result =  mvc.perform(delete("/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();

        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "200", o.getString("code") );

    }

    @Test
    public void addProduct() throws Exception {

        Map<String , List<Channel>> m = new HashMap<>();
        m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );

        Product p =new Product(1,"ayush",10.0, 2  ,m  )  ;


        Mockito.doReturn( p ).when( service1).addProduct( argThat(new ArgumentMatcher<Product>() {
            @Override
            public boolean matches(Product product) {
                if( product.getId() == p.getId())
                return true;
                return false;
            }
        }) );

        MvcResult result =  mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(p) ))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();

        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "200", o.getString("code") );

    }

    @Test
    public void addProduct_withexception() throws Exception {

        Map<String , List<Channel>> m = new HashMap<>();
        m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );

        Product p =new Product(1,"ayush",10.0, 2  ,m  )  ;


        Mockito.doThrow( new NullPointerException("Product Already Existed") ).when( service1).addProduct( argThat(new ArgumentMatcher<Product>() {
            @Override
            public boolean matches(Product product) {
                if( product.hashCode() == p.hashCode())
                    return true;
                return false;
            }
        }) );

        MvcResult result =  mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(p) ))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();

        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "400", o.getString("code") );

    }


    @Test
    public void updateProduct() throws Exception {

        Map<String , List<Channel>> m = new HashMap<>();
        m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );

        Product p =new Product(1,"ayush",10.0, 2  ,m  )  ;


        Mockito.doReturn( p ).when( service1).updateProduct( argThat(new ArgumentMatcher<Product>() {
            @Override
            public boolean matches(Product product) {
                if( product.getId() == p.getId())
                    return true;
                return false;
            }
        }) );

        MvcResult result =  mvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(p) ))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();

        JSONObject o = new JSONObject( result.getResponse().getContentAsString() );
        assertEquals( "200", o.getString("code") );

    }

    @Test
    public void addchannel() throws Exception {

        Channel p = ( new Channel( "C001" ,"Channel1" ,2,"kids")  );


        MvcResult result =  mvc.perform(put("/add_channel/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content( new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(p) ))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) .andReturn();

        ArgumentCaptor<Channel> argumentCaptor = ArgumentCaptor.forClass(Channel.class);

        ArgumentCaptor<Integer> a = ArgumentCaptor.forClass(Integer.class) ;

        Mockito.verify( service1 , times(1) ).add_channel( a.capture() , argumentCaptor.capture()) ;

        assertEquals( argumentCaptor.getValue().getChannel_Id() , p.getChannel_Id() );

        assertEquals( (int)1 , (int) a.getValue() );

    }

}

