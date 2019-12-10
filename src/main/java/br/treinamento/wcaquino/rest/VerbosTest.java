
package br.treinamento.wcaquino.rest;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario() { 
		
		RestAssured.given()
			.log().all()
			.contentType("application/json")
			.body("{ \"name\": \"Jose\", \"age\": 50 }")
		.when()
				.post("https://restapi.wcaquino.me/users")			
		.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.is(notNullValue()))
				.body("name", Matchers.is("Jose"))
				.body("age", Matchers.is(50));					
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMap() { 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		
		RestAssured.given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
				.post("https://restapi.wcaquino.me/users")			
		.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.is(notNullValue()))
				.body("name", Matchers.is("Usuario via map"))
				.body("age", Matchers.is(25));					
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario() { 
		
		User user = new User("Usuario deserializado", 35 );				
		
		User usuarioInserido = RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
				.post("https://restapi.wcaquino.me/users")			
		.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class);
		
		System.out.println(usuarioInserido);
		
		Assert.assertThat(usuarioInserido.getId(), Matchers.notNullValue());
		
		Assert.assertEquals("Usuario deserializado",usuarioInserido.getName());
		
		Assert.assertThat(usuarioInserido.getAge(), Matchers.is(35));
	}
	
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() { 
		
		User user = new User("Usuario via objeto", 27 );				
		
		RestAssured.given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
				.post("https://restapi.wcaquino.me/users")			
		.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.is(notNullValue()))
				.body("name", Matchers.is("Usuario via objeto"))
				.body("age", Matchers.is(27));					
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNome() {
		RestAssured.given()
			.log().all()
			.contentType("application/json")
			.body("{ \"age\" : 50 }")		
		.when()
			.post("https://restapi.wcaquino.me/users")		
		.then()
			.log().all()
			.statusCode(400)
			.body("id", Matchers.is(nullValue()))
			.body("error", Matchers.is("Name é um atributo obrigatório"))					
		;
			 
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
		.when()
				.post("https://restapi.wcaquino.me/usersXML")			
		.then()
				.log().all()
				.statusCode(201)
				.body("user.@id", Matchers.is(notNullValue()))
				.body("user.name", Matchers.is("Jose"))
				.body("user.age", Matchers.is("50"));					
	}
	
	@Test
	public void deveSalvarUsuarioViaXMLUsandoObjeto() { 
		
		User user = new User("Usuario XML", 50);
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
				.post("https://restapi.wcaquino.me/usersXML")			
		.then()
				.log().all()
				.statusCode(201)
				.body("user.@id", Matchers.is(notNullValue()))
				.body("user.name", Matchers.is("Usuario XML"))
				.body("user.age", Matchers.is("50"));					
	}
	
	@Test
	public void deveDeserializarXMLAoSalvarUsuario() { 
		
		User user = new User("Usuario XML", 50);
		
		User userDeserializado = RestAssured.given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
				.post("https://restapi.wcaquino.me/usersXML")			
		.then()
				.log().all()
				.statusCode(201)
				.extract().body().as(User.class);	
		
		Assert.assertThat(userDeserializado.getId(), Matchers.notNullValue());
		
		Assert.assertEquals("Usuario XML",userDeserializado.getName());
		
		Assert.assertThat(userDeserializado.getAge(), Matchers.is(50));
		
		
		System.out.println(userDeserializado);
		
	}
	
	
	
	@Test
	public void deveAlterarUsuario() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Usuario Alterado\", \"age\": 80 }")
		.when()
				.put("https://restapi.wcaquino.me/users/1")			
		.then()
				.log().all()
				.statusCode(200)
				.body("id", Matchers.is(1))
				.body("name", Matchers.is("Usuario Alterado"))
				.body("age", Matchers.is(80))
				.body("salary",Matchers.is(1234.5677f));
	}
	
	@Test
	public void devoCustomizarURL() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Usuario Alterado\", \"age\": 80 }")
		.when()
				.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")			
		.then()
				.log().all()
				.statusCode(200)
				.body("id", Matchers.is(1))
				.body("name", Matchers.is("Usuario Alterado"))
				.body("age", Matchers.is(80))
				.body("salary",Matchers.is(1234.5677f));
	}
	
	@Test
	public void devoCustomizarURLParte2() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Usuario Alterado\", \"age\": 80 }")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
				.put("https://restapi.wcaquino.me/{entidade}/{userId}")			
		.then()
				.log().all()
				.statusCode(200)
				.body("id", Matchers.is(1))
				.body("name", Matchers.is("Usuario Alterado"))
				.body("age", Matchers.is(80))
				.body("salary",Matchers.is(1234.5677f));
	}
	
	@Test
	public void deveRemoverUsuario() {
		RestAssured.given()
			.log().all()
			.when()
				.delete("https://restapi.wcaquino.me/users/1")
			.then()
				.log().all()
				.statusCode(204)
			;
		
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		RestAssured.given()
			.log().all()
			.when()
				.delete("https://restapi.wcaquino.me/users/1000")
			.then()
				.log().all()
				.statusCode(400)
				.body("error", Matchers.is("Registro inexistente"))
			;
		
	}
}
