package br.com.treinamento.rest.tests.refec.suite;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.com.treinamento.core.BaseTest;
import br.com.treinamento.rest.tests.reset.AuthTest;
import br.com.treinamento.rest.tests.reset.ContasTests;
import br.com.treinamento.rest.tests.reset.MovimentacaoTests;
import br.com.treinamento.rest.tests.reset.SaldoTests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(org.junit.runners.Suite.class)

@SuiteClasses({
	ContasTests.class,
	MovimentacaoTests.class,
	SaldoTests.class,
	AuthTest.class
})

public class Suite extends BaseTest {
	
	@BeforeClass
	public static void login() { 
		System.out.println("Before Class");
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "celsoricardoteste@gmail.com");
		login.put("senha", "123456");
		
		
		String TOKEN  = RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(login)			
		.when()
			.post("https://barrigarest.wcaquino.me/signin")			
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");		
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);						
		RestAssured.get("https://barrigarest.wcaquino.me/reset").then().statusCode(200);		
	}

}
