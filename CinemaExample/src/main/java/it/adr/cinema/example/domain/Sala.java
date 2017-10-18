package it.adr.cinema.example.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="sala")

public class Sala {
	@Id
	@Column(name="cod_sala")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long codSala;
	
	@Column(name="nome")
	private String nome;
	@Column(name="schermo")
	private String schermo;
	@Column(name="audio")
	private String audio;
	@Column(name="capienza")
	private Integer capienza;
	
	@OneToMany (mappedBy="sala")
	private Set<Proiezione> proiezione;
	
	public Set<Proiezione> getProiezione() {
		return proiezione;
	}
	public void setProiezione(Set<Proiezione> proiezione) {
		this.proiezione = proiezione;
	}
	public long getCodSala() {
		return codSala;
	}
	public void setCodSala(long codSala) {
		this.codSala = codSala;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSchermo() {
		return schermo;
	}
	public void setSchermo(String schermo) {
		this.schermo = schermo;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public Integer getCapienza() {
		return capienza;
	}
	public void setCapienza(Integer capienza) {
		this.capienza = capienza;
	}
	
	

}
