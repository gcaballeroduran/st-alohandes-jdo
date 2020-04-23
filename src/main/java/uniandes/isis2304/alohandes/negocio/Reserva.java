package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;

public class Reserva implements VOReserva {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/

	/**
	 *  id unico de la reserva.
	 */
	private long id;
	
	/**
	 * fecha de inicio de la reservacion.
	 */
	private Date fechaInicio;
	
	/**
	 * fecha del fin de la reservacion.
	 */
	private Date fechaFin;
	
	/**
	 * numero de personas para la reservacion.
	 */
	private int personas;
	
	/**
	 *  fecha final para cancelar la reservacion.
	 */
	private Date finCancelacionOportuna;
	
	/**
	 *  porcentaje que se tiene que pagar de la reservacion.
	 */
	private double porcentajeAPagar;
	
	/**
	 *  monto total de la reservacion.
	 */
	private double montoTotal;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto.
	 */
	public Reserva(){
		
		id = 0;
		fechaInicio = null;
		fechaFin = null;
		personas = 1;
		finCancelacionOportuna = null;
		porcentajeAPagar = 0;
		montoTotal = 0;
		
	}
	
	/**
	 * Constructor con valores.
	 * @param id - id de la reserva.
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @param montoTotal - monto total de la reserva.
	 */
	public Reserva(long id, Date fechaInicio, Date fechaFin, int personas, Date finCancelacionOportuna,
			double porcentajeAPagar, double montoTotal) {
		
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.personas = personas;
		this.finCancelacionOportuna = finCancelacionOportuna;
		this.porcentajeAPagar = porcentajeAPagar;
		this.montoTotal = montoTotal;
	}

	/**
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 
	 * @param id - nuevo id de la reserva.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/***
	 * 
	 * @param fechaInicio -  nueva fecha de inicio de la reserva.
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * 
	 * @return fechaFin.
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * 
	 * @param fechaFin - nueva fecha final de la reserva.
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * 
	 * @return personas.
	 */
	public int getPersonas() {
		return personas;
	}

	/**
	 * 
	 * @param personas - nuevo numero de personas de la reserva.
	 */
	public void setPersonas(int personas) {
		this.personas = personas;
	}

	/**
	 * 
	 * @return finCancelacionOporuna.
	 */
	public Date getFinCancelacionOportuna() {
		return finCancelacionOportuna;
	}

	/**
	 * 
	 * @param finCancelacionOportuna - nueva fecha de cancelacion oportuna.
	 */
	public void setFinCancelacionOportuna(Date finCancelacionOportuna) {
		this.finCancelacionOportuna = finCancelacionOportuna;
	}

	/**
	 * 
	 * @return porcentajeAPagar.
	 */
	public double getPorcentajeAPagar() {
		return porcentajeAPagar;
	}

	/**
	 * 
	 * @param porcentajeAPagar - nuevo porcentaje a pagar.
	 */
	public void setPorcentajeAPagar(double porcentajeAPagar) {
		this.porcentajeAPagar = porcentajeAPagar;
	}

	/**
	 * 
	 * @return montoTotal.
	 */
	public double getMontoTotal() {
		return montoTotal;
	}

    /**
     * 
     * @param montoTotal - nuevo monto total.
     */
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la reserva.
	 */
	public String toString() 
	{
		return "Reserva [id=" + id + ", fecha de inicio=" + fechaInicio + ", fecha final=" + fechaFin +
				", personas="+personas+ ", fecha final de cancelacion oportuna=" + finCancelacionOportuna+
				", porcentaje a pagar="+ porcentajeAPagar+ ", monto total="+ montoTotal+"]";
	}
	
}
