package br.com.treinamento.core;

import io.restassured.http.ContentType;

public interface Constants {
	
	String APP_BASE_URL = "https://barrigarest.wcaquino.me/";
	String APP_BASE_PATH = "";
	Integer APP_PORT = 443;	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	Long MAX_TIMOUT = 3000L;
}