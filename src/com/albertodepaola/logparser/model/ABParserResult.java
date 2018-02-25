package com.albertodepaola.logparser.model;

public class ABParserResult {

	private Long quantidadeDeClientes;
	private Long quantidadeDeVendedores;
	private Long idMaiorVenda;
	private String worstSeller;

	public ABParserResult(Long quantidadeDeClientes, Long quantidadeDeVendedores, Long idMaiorVenda,
			String worstSeller) {
		super();
		this.quantidadeDeClientes = quantidadeDeClientes;
		this.quantidadeDeVendedores = quantidadeDeVendedores;
		this.idMaiorVenda = idMaiorVenda;
		this.worstSeller = worstSeller;

	}

	public Long getQuantidadeDeClientes() {
		return quantidadeDeClientes;
	}

	public Long getQuantidadeDeVendedores() {
		return quantidadeDeVendedores;
	}

	public Long getIdMaiorVenda() {
		return idMaiorVenda;
	}

	public String getWorstSeller() {
		return worstSeller;
	}

}
