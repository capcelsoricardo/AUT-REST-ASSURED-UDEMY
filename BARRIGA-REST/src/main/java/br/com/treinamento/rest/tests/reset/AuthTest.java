package br.com.treinamento.rest.tests.reset;

import org.hamcrest.Matchers;
import org.junit.Test;

import br.com.treinamento.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest  {
		
	@Test
	public void nao_deve_acessar_api_sem_token() {
		
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization");
		
		RestAssured.given()
			.log().all()
			
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.body(Matchers.is("Unauthorized"))
			.statusCode(401);				
	}
	
	public Integer get_id_conta_pelo_nome(String nome) { 
		return RestAssured.get("https://barrigarest.wcaquino.me/contas?nome="+nome).then().extract().path("id[0]");
	}
	
}
