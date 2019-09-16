package br.treinamento.wcaquino.rest;

import static org.hamcrest.CoreMatchers.allOf;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserXMLTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;
	
	
	
	@BeforeClass
	public static void setup() { 		
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		RestAssured.port = 443;
		//RestAssured.baseURI = "/v2";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
		
	}
	
	@Test
	public void devoTrabalharComXML() { 			
				
		RestAssured.given()
			//.spec(reqSpec)			
		.when()
			.get("/usersXML/3")
		.then()
			//.spec(resSpec)
			
			.rootPath("user")
			.body("name", Matchers.is("Ana Julia"))
			.body("@id", Matchers.is("3"))
			.rootPath("user.filhos")
			.body("name.size()", Matchers.is(2))
			.detachRootPath("filhos")
			.body("filhos.name[0]", Matchers.is("Zezinho"))
			.body("filhos.name[1]", Matchers.is("Luizinho"))
			.appendRootPath("filhos")
			.body("name", Matchers.hasItem("Luizinho"))
			.body("name", Matchers.hasItems("Luizinho", "Zezinho"));
		
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXML() { 
		RestAssured.given()
			.log().all()
		.when()			
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", Matchers.is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", Matchers.is(2))
			.body("users.user.@id", Matchers.hasItems("1", "2", "3"))
			.body("users.user.find{it.age == 25}.name", Matchers.is( "Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", Matchers.hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", Matchers.is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2} ", Matchers.hasItems(40, 50, 60))
			//.body("users.user.name.findAll{it.toSring().startsWith('Maria')}.collect{it.toString().toUpperCase()}", Matchers.is("MARIA JOAQUINA"))
			;
		
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXMLEJAva() { 
		ArrayList<NodeImpl> nomes = RestAssured.given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		
		System.out.println(nomes.toString());
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("ANA JULIA".toUpperCase(), nomes.get(1).toString().toUpperCase());
		
//		Assert.assertEquals("Maria Joaquina".toUpperCase(), nome.toUpperCase());
	}

	
	@Test
	public void devoFazerPesquisasAvancadasXPath() { 
		RestAssured.given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(Matchers.hasXPath("count(/users/user)", Matchers.is("3")))
			.body(Matchers.hasXPath("//user[@id = '2']"))
			.body(Matchers.hasXPath("//name[text() = 'Luizinho']/../../name", Matchers.is("Ana Julia")))
			.body(Matchers.hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(Matchers.containsString("Zezinho"), Matchers.containsString("Luizinho"))))
			.body(Matchers.hasXPath("/users/user/name", Matchers.is("João da Silva")))
			.body(Matchers.hasXPath("count(/users/user/name[contains(., 'n')])", Matchers.is("2")))
			;				
	}	
}