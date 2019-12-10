package br.com.treinamento.rest.tests;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.treinamento.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTests  {
	
	String token;
	private static String CONTA_NAME = "Conta " + System.nanoTime(); 
	private static Integer MOV_ID;
	private static Integer CONTA_ID;
	
	@Before
	public void get_token() { 
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "celsoricardoteste@gmail.com");
		login.put("senha", "123456");
		
		
		token = RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(login)			
		.when()
			.post("https://barrigarest.wcaquino.me/signin")			
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");		
		
		//RestAssured.requestSpecification.header("Authorization", "JWT " + token.toString());		
		// A linha acima fica padrão para todos os cenários 
		// A linha abaixo remove de todos os cenários:
		//FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		//req.removeHeader("Authorization");
	}
	
	@Test
	public void T01_nao_deve_acessar_api_sem_token() {
		RestAssured.given()
			.log().all()
			
		.when()
			.get("https://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.body(Matchers.is("Unauthorized"))
			.statusCode(401);				
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void T02_deve_incluir_conta_com_sucesso() throws IOException, ParseException { 
		
		JSONObject obj = new JSONObject();
		obj.put("nome", CONTA_NAME);

		FileWriter file =  new FileWriter("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/conta.json");
		file.write(obj.toJSONString());		
		file.close();
		
		FileReader reader = new FileReader("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/conta.json");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(reader);
		System.out.println("Esse será o nome da Conta: " + jsonObj.get("nome"));
		
																	
		CONTA_ID = RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.body(new File("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/conta.json"))
			.header("Authorization", "JWT " + token.toString())
		.when()
				.post("https://barrigarest.wcaquino.me/contas")
		.then()
				.log().all()
				.statusCode(201)// Status Code de Inclusão
				.extract().path("id"); 				
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void T03_deve_alterar_conta_com_sucesso() throws IOException { 
		
		JSONObject obj = new JSONObject();
		obj.put("nome", CONTA_NAME + " ALTERADA");

		FileWriter file =  new FileWriter("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/alteracao_conta.json");
		file.write(obj.toJSONString());		
		file.close();		
								
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.pathParam("id", CONTA_ID)
			.body(new File("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/alteracao_conta.json") )
			.header("Authorization", "JWT " + token.toString())
		.when()
			.put("https://barrigarest.wcaquino.me/contas/{id}")
					
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", Matchers.is(CONTA_NAME + " ALTERADA"))
			;
	}
	
	@Test
	public void T04_nao_deve_incluir_conta_com_nome_repetido() {
										
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)			
			.body(new File("/Users/celso/devtest/eclipse-workspace/BARRIGA-REST/src/main/resources/alteracao_conta.json"))
			.header("Authorization", "JWT " + token.toString())
		.when()
				.post("https://barrigarest.wcaquino.me/contas")
		.then()
				.log().all()
				.statusCode(400)
				.body("error", Matchers.is("Já existe uma conta com esse nome!"))
				; // Status Code de Inclusão						
	}
	
	@Test
	public void T05_deve_inserir_movimentacao_com_sucesso() {
							
		RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)
				.header("Authorization", "JWT " + token)
				.body(new File("src/main/resources/inserir_movimentacao.json"))
			.when()
				.post("https://barrigarest.wcaquino.me/transacoes")
							
			.then()
				.statusCode(201)
				.log().all();
		
	}
	
	@Test
	public void T06_deve_inserir_movimentacao_com_sucesso_usando_objeto() {
		
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setContaID(CONTA_ID.toString());
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(-5));
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		movimentacao.setDescricao("Movimentacao feita pelo Objeto");
		movimentacao.setEnvolvido("Esse é Envlvido");
		movimentacao.setStatus(true);
		movimentacao.setTipo("REC");
		movimentacao.setValor(123.23f);
							
		MOV_ID = RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)
				.header("Authorization", "JWT " + token)
				.body(movimentacao)
			.when()
				.post("https://barrigarest.wcaquino.me/transacoes")
							
			.then()
				.statusCode(201)
				.log().all()
				.extract().path("id");
		
	}
	
	@Test
	public void T07_deve_validar_campos_obrigatorios_na_movimentacao() {
									
		RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)
				.header("Authorization", "JWT " + token)
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
	public void T08_nao_deve_cadastrar_movimentacao_com_data_futura() {
		
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setContaID(CONTA_ID.toString());
		movimentacao.setData_pagamento(DataUtils.getDataDiferencaDias(0));
		movimentacao.setData_transacao(DataUtils.getDataDiferencaDias(10));
		movimentacao.setDescricao("Movimentacao feita pelo Objeto");
		movimentacao.setEnvolvido("Esse é Envolvido");
		movimentacao.setStatus(true);
		movimentacao.setTipo("REC");
		movimentacao.setValor(123.23f);
							
		RestAssured.given()
				.log().all()
				.contentType(ContentType.JSON)
				.header("Authorization", "JWT " + token)
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
	public void T09_nao_deve_remover_conta_com_movimentacao() { 
		
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "JWT " + token)			
			.pathParam("id", CONTA_ID)
		.when()
			.delete("https://barrigarest.wcaquino.me/contas/{id}")					
		.then()			
			.log().all()
			.statusCode(500)
			.body("constraint", Matchers.is("transacoes_conta_id_foreign"))
		;					
	}
	
	@Test
	public void T10_deve_calcular_saldo_das_contas() { 
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "JWT " + token)
		.when()
			.get("https://barrigarest.wcaquino.me/saldo")
		
		.then()
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", Matchers.is("123.23"))
			.statusCode(200)
			.log().all()
		;
	}
	
	@Test
	public void T11_deve_remover_movimentacao() { 
		RestAssured.given()
			.log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "JWT " + token)
			.pathParam("id", MOV_ID)
		.when()
			.delete("https://barrigarest.wcaquino.me/transacoes/{id}")							
		.then()
			
			.statusCode(204)
			.log().all()
		;
	}
	
	
}
