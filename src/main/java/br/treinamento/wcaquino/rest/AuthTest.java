package br.treinamento.wcaquino.rest;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

public class AuthTest {
	
	@Test
	public void deveAcessarSWAPI() {
		RestAssured.given()
			.log().all()
		
		.when()
			.get("https://swapi.co/api/people/1")
		.then()
			.statusCode(200)
			.body("name", Matchers.is("Luke Skywalker"))
			.log().all()
		
		;
	}
	
	@Test
	public void deveObterClima() { 
		RestAssured.given()
		.log().all()
		.queryParam("q", "Fortaleza,BR")
		.queryParam("appid", "70d6f2629583485c39f4505289bc5996")
		.queryParam("units", "metric")
	
	.when()
		.get("http://api.openweathermap.org/data/2.5/weather")
	.then()
		.statusCode(200)
		.log().all()
		.body("name", Matchers.is("Fortaleza"))
		.body("coord.lon", Matchers.is(-38.52f))
		.body("main.temp", Matchers.greaterThan(25f))
		
		//http://api.openweathermap.org/data/2.5/weather?q=Fortaleza,BR&appid=70d6f2629583485c39f4505289bc5996&units=metric
	;
		
	}	

	@Test
	public void naoDeveAcessarSemSenha() { 
		RestAssured.given()
		.log().all()		
	
	.when()
		.get("http://restapi.wcaquino.me/basicauth")
	.then()
		.statusCode(401)
		.log().all()
		//http://restapi.wcaquino.me/basicauth
	;
		
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() { 
		RestAssured.given()
		.log().all()				
	.when()
		.get("http://admin:senha@restapi.wcaquino.me/basicauth")
	.then()
		.statusCode(200)
		.log().all()
		.body("status", Matchers.is("logado"));		
	}	
	
	@Test
	public void deveFazerAutenticacaoBasica2() { 
		RestAssured.given()
		.log().all()	
		.auth().basic("admin", "senha")
	
	.when()
		.get("http://restapi.wcaquino.me/basicauth")
	.then()
		.statusCode(200)
		.log().all()
		.body("status", Matchers.is("logado"));
		//http://restapi.wcaquino.me/basicauth		
	}
	
	@Test
	public void deveFazerAutenticacaoBasicaChallengeOuPrimitiva() { 
		RestAssured.given()
		.log().all()	
		.auth().preemptive().basic("admin", "senha")
	
	.when()
		.get("http://restapi.wcaquino.me/basicauth2")
	.then()
		.statusCode(200)
		.log().all()
		.body("status", Matchers.is("logado"));
		//http://restapi.wcaquino.me/basicauth
		
	}
	
	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "celsoricardoteste@gmail.com");
		login.put("senha", "123456");
		
		//Login na API
		//Receber o Token
		String token = RestAssured.given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token");
		
		//Obter as contas
		
		RestAssured.given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			.body("nome", Matchers.is("Conta de Teste"))
			.statusCode(200);
					
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() { 
		
		String cookie = RestAssured.given()
			.log().all()
			.formParam("email", "celsoricardoteste@gmail.com")
			.formParam("senha","123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))		
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
	
		.then()	
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
			;
		
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
		
		String body = RestAssured.given()
			.log().all()
			.cookie("connect.sid", cookie)
				
		.when()
			.get("http://seubarriga.wcaquino.me/contas")

		.then()	
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", Matchers.is("Conta de Teste"))
			.extract().body().asString();				
		;
		
		System.out.println("-----------------");
		
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
		
	}
}
