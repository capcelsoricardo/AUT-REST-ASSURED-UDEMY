package br.treinamento.wcaquino.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;

public class FileTest {
	
	@Test
	public void deveObrigarEnvioArquivo() { 
		RestAssured.given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		
		.then()
			.log().all()
			.statusCode(404) //DEVERIA SER 400
			.body("error", Matchers.is("Arquivo não enviado"))
		;
			
	}
	
	@Test
	public void deveFazerUploadArquivo() { 
		RestAssured.given()
			.log().all()
			.multiPart("arquivo",  new File("src/main/resources/users.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		
		.then()
			.log().all()
			.statusCode(200) //DEVERIA SER 400
			.body("name", Matchers.is("users.pdf"));
			
	}
	
	@Test
	public void naoDeveFazerUploadArquivo() { 
		RestAssured.given()
			.log().all()
			.multiPart("arquivo",  new File("src/main/resources/temp.xlsx"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		
		.then()
			.log().all()
			.time(Matchers.lessThan(5000L))
			.statusCode(413)						
			.body("name", Matchers.is("temp.xlsx"));
			
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException { 
		
		byte[] image = 
		RestAssured.given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		
		.then()
			.log().all()
			.statusCode(200)	
			.extract().asByteArray()
			;
		
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		
		System.out.println(imagem.length());
		
		Assert.assertThat(imagem.length(), Matchers.lessThan(1000000L)  );
		
			
	}

}
