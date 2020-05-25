package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;

/**
 * Interfaz para los métodos get de reserva.
 */
public interface VOReserva {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * 
	 * @return id
	 */
	public long getId();


	/**
	 * 
	 * @return fechaInicio
	 */
	public String getFechaInicio();

	/**
	 * 
	 * @return fechaFin.
	 */
	public String getFechaFin();

	/**
	 * 
	 * @return personas.
	 */
	public int getPersonas();

	/**
	 * 
	 * @return finCancelacionOporuna.
	 */
	public String getFinCancelacionOportuna();

	/**
	 * 
	 * @return porcentajeAPagar.
	 */
	public double getPorcentajeAPagar();

	/**
	 * 
	 * @return montoTotal.
	 */
	public double getMontoTotal();

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la reserva.
	 */
	public String toString();
	
	

}
