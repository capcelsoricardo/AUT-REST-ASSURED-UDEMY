package br.com.treinamento.rest.tests.reset;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import br.com.treinamento.core.BaseTest;
import br.com.treinamento.utils.BarrigaUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ContasTests extends BaseTest  {
			
	@Test
	public void deve_incluir_conta_com_sucesso() throws IOException, ParseException { 
		
		System.out.println("Incluir Conta");
																					
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.body("{\"nome\":\"Conta 20567137758935\"}")						
		.when()
			.post("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(201);		 				
	}
		
	@Test
	public void deve_alterar_conta_com_sucesso() throws IOException {
		
		System.out.println("Alterar Conta");
		
		Integer CONTA_ID = BarrigaUtils.get_id_conta_pelo_nome("Conta para alterar");
														
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.pathParam("id", CONTA_ID)
			.body("{\"nome\":\"Conta ALTERADA\"}")			
		.when()
			.put("https://barrigarest.wcaquino.me/contas/{id}")
					
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", Matchers.is("Conta ALTERADA"))
			;
	}
	
	@Test
	public void nao_deve_incluir_conta_com_nome_repetido() {
										
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.body("{\"nome\":\"Conta mesmo nome\"}")
			
		.when()
			.post("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", Matchers.is("Já existe uma conta com esse nome!"));
 	
	}
}