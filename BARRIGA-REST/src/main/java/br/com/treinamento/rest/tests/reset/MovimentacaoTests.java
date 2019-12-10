package br.com.treinamento.rest.tests.reset;

import org.hamcrest.Matchers;
import org.junit.Test;

import br.com.treinamento.core.BaseTest;
import br.com.treinamento.rest.tests.Movimentacao;
import br.com.treinamento.utils.BarrigaUtils;
import br.com.treinamento.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class MovimentacaoTests extends BaseTest  {
			
	@Test
	public void deve_inserir_movimentacao_com_sucesso() {
							
		RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)				
				.body(get_movimentacao())
			.when()
				.post("https://barrigarest.wcaquino.me/transacoes")
							
			.then()
				.statusCode(201)
				.log().all();		
	}
	
	@Test
	public void deve_validar_campos_obrigatorios_na_movimentacao() {
									
		RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)				
				.body("{}")
			.when()
				.post("https://barrigarest.wcaquino.me/transacoes")							
			.then()
				.statusCode(400)
				.body("$", Matchers.hasSize(8))
				.body("msg", Matchers.hasItems(
						"Data da Movimentação é obrigatório",
						"Data do pagamento é obrigatório",
						"Descrição é obrigatório",
						"Interessado é obrigatório",
						"Valor é obrigatório",
						"Valor deve ser um número",					
						"Conta é obrigatório",
						"Situação é obrigatório"						
						))								
				.log().all();		
	}
	
	@Test
	public void nao_deve_cadastrar_movimentacao_com_data_futura() {
		
		Movimentacao movimentacao = get_movimentacao();				
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(2));
									
		RestAssured.given()						
				.body(movimentacao)
			.when()
				.post("https://barrigarest.wcaquino.me/transacoes")
							
			.then()
				.statusCode(400)
				.body("$", Matchers.hasSize(1))
				.body("msg", Matchers.hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
				.log().all();		
	}
	
	@Test
	public void nao_deve_remover_conta_com_movimentacao() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.pathParam("id", BarrigaUtils.get_id_conta_pelo_nome("Conta com movimentacao").toString())
		.when()
			.delete("https://barrigarest.wcaquino.me/contas/{id}")					
		.then()			
			.log().all()
			.statusCode(500)
			.body("constraint", Matchers.is("transacoes_conta_id_foreign"))
		;					
	}
	
	
	@Test
	public void deve_remover_movimentacao() { 
		
		Integer MOV_ID = BarrigaUtils.get_id_mov_pela_descricao("Movimentacao para exclusao");
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.pathParam("id", MOV_ID)
		.when()
			.delete("https://barrigarest.wcaquino.me/transacoes/{id}")							
		.then()
			
			.statusCode(204)
			.log().all()
		;
	}
	
	
	public Movimentacao get_movimentacao() { 
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setContaID(BarrigaUtils.get_id_conta_pelo_nome("Conta para movimentacoes").toString());
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(-5));
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		movimentacao.setDescricao("Movimentacao feita pelo Objeto");
		movimentacao.setEnvolvido("Esse é Envlvido");
		movimentacao.setStatus(true);
		movimentacao.setTipo("REC");
		movimentacao.setValor(123.23f);
		return movimentacao;
		
	}
}
