package it.adr.cinema.example.domain;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="proiezione")
public class Proiezione {
	@Id
	@Column(name="cod_pr")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long codPr;
	
	@Column(name="film")
	private String film;
	//@Column(name="cod_sala")
	//private String codSala;
	@Column(name="data")
	private Date data;
	@Column(name="ora_inizio")
	private Time oraInizio;
	@Column(name="ora_fine")
	private Time oraFine;
	
	@ManyToOne
	@JoinColumn(name="cod_sala", nullable=false)
	private Sala sala;

	public long getCodPr() {
		return codPr;
	}

	public void setCodPr(long codPr) {
		this.codPr = codPr;
	}

	public String getFilm() {
		return film;
	}

	public void setFilm(String film) {
		this.film = film;
	}

	/*public String getCodSala() {
		return codSala;
	}

	public void setCodSala(String codSala) {
		this.codSala = codSala;
	}*/

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(Time oraInizio) {
		this.oraInizio = oraInizio;
	}

	public Time getOraFine() {
		return oraFine;
	}

	public void setOraFine(Time oraFine) {
		this.oraFine = oraFine;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}
	
	

}
