package br.com.treinamento.core;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class BaseTest implements Constants {
	
	@BeforeClass
	public static void setup() {
		System.out.println("Base Test");
		
		RestAssured.baseURI = APP_BASE_URL;
		RestAssured.port = APP_PORT;
		RestAssured.basePath = APP_BASE_PATH;
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(APP_CONTENT_TYPE);
		RestAssured.requestSpecification = reqBuilder.build();
		
		 ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		 resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMOUT));
		 
		 RestAssured.responseSpecification = resBuilder.build();
		 
		 RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
}