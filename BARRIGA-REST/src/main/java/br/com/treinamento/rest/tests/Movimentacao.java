package br.com.treinamento.rest.tests;

public class Movimentacao {
	
	public String conta_id;
	public String descricao;
	public String envolvido;
	public String tipo;
	public String data_transacao;
	public String data_pagamento;
	public float valor;
	public boolean status;
	
	public String getConta_id() {
		return conta_id;
	}
	public void setContaID(String conta_id) {
		this.conta_id = conta_id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getEnvolvido() {
		return envolvido;
	}
	public void setEnvolvido(String envolvido) {
		this.envolvido = envolvido;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getData_transacao() {
		return data_transacao;
	}
	public void setData_transacao(String data_transacao) {
		this.data_transacao = data_transacao;
	}
	public String getData_pagamento() {
		return data_pagamento;
	}
	public void setData_pagamento(String data_pagamento) {
		this.data_pagamento = data_pagamento;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
}
