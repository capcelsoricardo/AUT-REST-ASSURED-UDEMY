package br.treinamento.wcaquino.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
	
	@Test
	public void testOlaMundo() {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertEquals(200, response.statusCode());
		
//		throw new RuntimeException();
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
	}
	
	@Test
	public void devoConhecerOutrasFormasRestAssured() {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		get("http://restapi.wcaquino.me:80/ola").then().statusCode(200);
		
		given() // Pré condições
			
			.when() // Ação				
				.get("http://restapi.wcaquino.me:80/ola")
			.then() // Assertivas
				.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria"));
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));
		Assert.assertThat(128d, Matchers.isA(Double.class));
		Assert.assertThat(128d, Matchers.greaterThan(120d));
		Assert.assertThat(128d, Matchers.lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,2,3,4,5);
		Assert.assertThat(impares, Matchers.hasSize(5));
		Assert.assertThat(impares, Matchers.contains(1,2,3,4,5));
		Assert.assertThat(impares, Matchers.containsInAnyOrder(1,3,2,4,5));
		Assert.assertThat(impares, Matchers.hasItem(1));
		Assert.assertThat("Maria", Matchers.is(not("João")));
		Assert.assertThat("Maria", Matchers.not("João"));
		Assert.assertThat("Maria", Matchers.anyOf(Matchers.is("Maria"), Matchers.is("Joaquim")));
		Assert.assertThat("Maria", Matchers.allOf(Matchers.startsWith("Ma"), Matchers.endsWith("ia"), Matchers.containsString("a")));
	}
	
	@Test
	public void devoValidarBody() {
		
		given().when() .get("http://restapi.wcaquino.me:80/ola").then().statusCode(200).body(Matchers.is("Ola Mundo!"))
		.body(Matchers.containsString("Mundo"))
		.body(Matchers.is(not(nullValue())));
	}

}
