package com.example.api;

import com.example.api.model.Channel;
import com.example.api.model.Product;
import com.example.api.repository.ProductRepository;
import com.example.api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Stream;


@RunWith( SpringRunner.class)
@SpringBootTest
class ApiApplicationTests  {

	@Autowired
	private ProductService service;

	@MockBean
	private ProductRepository repository;

	@Test
	public void getProductsTest(){

				Map<String , List<Channel> > m = new HashMap<>();
				m.put ("kids"  ,  Collections.singletonList( new Channel( "C001" ,"Channel1" ,2,"kids") )  );
				 List<Product> list =new ArrayList<>();
				 list.add (new Product(1,"ayush",10.0, 2  ,m  ));
				 list.add (new Product(2,"second",2.0,3 , new HashMap<>() ) );

		when( repository.findAll() ).thenReturn( list );

		assertEquals(2 , service.getProducts().size() );

	}
	@Test
	public void add_Channel(){
		Product p = (new Product(1,"ayush",10.0,2 , new HashMap<>()) );
		when( repository.findById(1) ).thenReturn(Optional.of(p));


		Channel c=  new Channel( "C001" ,"Channel1" ,2,"kids") ;

		service.add_channel( 1 ,c );

		ArgumentCaptor<Product> ag = ArgumentCaptor.forClass(Product.class);

		verify(repository , times(1 )  ).save( ag.capture()) ;
		assertEquals( 1 , ag.getValue().getChannelList().size() );

	}

	@Test
	public void add_Channel_with_exception(){
		Product p = (new Product(1,"ayush",10.0,2 , new HashMap<>()) );
		when( repository.findById(1) ).thenReturn(Optional.ofNullable(null));


		Channel c=  new Channel( "C001" ,"Channel1" ,2,"kids") ;

		Exception e = assertThrows( NullPointerException.class , ()->{ service.add_channel( 1, c) ; }  ) ;

		assertTrue( e.getMessage().contains("Product Doesnot Exist" ));

	}


	@Test
	public void add_Channel_with_exception2(){
		Product p = (new Product(1,"ayush",10.0,2 , new HashMap<>()) );
		when( repository.findById(1) ).thenReturn(Optional.ofNullable(p));


		Channel c=  new Channel( "C001" ,"Channel1" ,2,"kids") ;

		p.getChannelList(  ).put( c.getGenre() , Collections.singletonList(c) );

		Exception e = assertThrows( NullPointerException.class , ()->{ service.add_channel( 1, c) ; }  ) ;

		assertTrue( e.getMessage().contains("Product have channel already  Exist" ));

	}

	@Test
	public void getProductById(){

		Product p = (new Product(1,"ayush",10.0,2 , new HashMap<>()) );

		when( repository.findById(1) ).thenReturn(Optional.of(p));


		assertEquals( p , service.getProductById(1) );

	}
	@Test
	public void addProductsucess() {


		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));


		when( repository.findById( 1) ).thenReturn( Optional.ofNullable(null) );
		when( repository.insert(p) ).thenReturn(p);

		assertEquals( p , service.addProduct(p) );

	}

	@Test
	public void addProductFails(){

		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));

		when( repository.findById(1) ).thenThrow( new  NullPointerException( "Product Already Existed" ) ) ;

		Exception exception = assertThrows(NullPointerException.class, () -> {
			service.addProduct(p) ;
		});

		assertTrue(exception.getMessage().contains( "Product Already Existed" ));
	}

	@Test
	public void updateProduct(){

		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));

		when( repository.findById( 1) ).thenReturn( Optional.ofNullable(p) );

		when(repository.save(p)).thenReturn((p));

		assertEquals(p, service.updateProduct(p) );

	}

	@Test
	public void updateProductexception(){

		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));

		Mockito.doReturn(Optional.ofNullable(null) ).when(repository).findById(1);

		when(repository.save(p)).thenReturn((p));

		Exception e = assertThrows( NullPointerException.class , ()->{ service.updateProduct(p) ; } ) ;

		assertTrue( e.getMessage().contains("Product was Not in database" ));

	}
	@Test
	public void delete(){
		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));

		Mockito.doReturn(Optional.ofNullable(p) ).when(repository).findById(1);

		ArgumentCaptor<Product> captor = ArgumentCaptor.forClass( Product.class) ;

		service.delete(1);
		verify( repository , times(1 ) ).delete( captor.capture() );

		assertEquals( p  , captor.getValue() );

	}

	@Test
	public void delete_with_exception(){
		Product p = (new Product(1, "ayush", 10.0, 2, new HashMap<>()));

		when( repository.findById(1) ).thenReturn( Optional.ofNullable(null) );

		Exception e = assertThrows( NullPointerException.class , ()->{ service.delete(1); } ) ;

		assertTrue( e.getMessage().contains( "No product existed" ));

	}


}
