package br.com.treinamento.rest.tests.reset;

import org.hamcrest.Matchers;
import org.junit.Test;

import br.com.treinamento.core.BaseTest;
import br.com.treinamento.utils.BarrigaUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SaldoTests extends BaseTest  {
			
	@Test
	public void deve_calcular_saldo_das_contas() { 
		
		Integer CONTA_ID = BarrigaUtils.get_id_conta_pelo_nome("Conta para saldo");
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
		.when()
			.get("https://barrigarest.wcaquino.me/saldo")
		
		.then()
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", Matchers.is("534.00"))
			.statusCode(200)
			.log().all()
		;
	}	
}
