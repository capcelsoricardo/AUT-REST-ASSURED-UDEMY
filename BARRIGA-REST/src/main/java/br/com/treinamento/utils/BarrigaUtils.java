package br.com.treinamento.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {
	
	public static Integer get_id_conta_pelo_nome(String nome) { 
		return RestAssured.get("https://barrigarest.wcaquino.me/contas?nome="+nome).then().extract().path("id[0]");
	}
	
	public static Integer get_id_mov_pela_descricao(String desc) { 
		return RestAssured.get("https://barrigarest.wcaquino.me/transacoes?descricao="+desc).then().extract().path("id[0]");
	}
}