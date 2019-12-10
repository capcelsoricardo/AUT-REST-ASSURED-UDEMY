package br.treinamento.wcaquino.rest;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnvioDadosTest {
	
	@Test
	public void deveEnviarValorViaQuery() { 
		RestAssured.given()
			.log().all()
		
			.when()
				.get("http://restapi.wcaquino.me/v2/users?format=xml")
			
			.then()
				.log().all()
				.statusCode(200)
				.contentType(ContentType.XML)
				
			;
	}
	
	@Test
	public void deveEnviarValorViaQueryViaParametro() { 
		RestAssured.given()
			.log().all()
			.queryParam("format", "xml")
			.queryParam("outra", "coisa")
		
			.when()
				.get("http://restapi.wcaquino.me/v2/users")
			
			.then()
				.log().all()
				.statusCode(200)
				.contentType(ContentType.XML)
				.contentType(Matchers.containsString("utf-8"))
				
			;
	}
	
	@Test
	public void deveEnviarValorViaHeader() { 
		RestAssured.given()
			.log().all()
			.accept(ContentType.XML)
			.when()
				.get("http://restapi.wcaquino.me/v2/users")
			
			.then()
				.log().all()
				.statusCode(200)
				.contentType(ContentType.XML)
				
			;
	}

}
