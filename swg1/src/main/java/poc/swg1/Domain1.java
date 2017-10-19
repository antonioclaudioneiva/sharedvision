package poc.swg1;

import java.awt.Color;

public class Domain1 {

	private String nome;

	private Integer ano;

	private Color color;

	public Domain1(String nome, Integer ano, Color color) {
		super();
		this.nome = nome;
		this.ano = ano;
		this.color = color;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Domain1 [nome=" + nome + ", ano=" + ano + ", color=" + color + "]";
	}

}
