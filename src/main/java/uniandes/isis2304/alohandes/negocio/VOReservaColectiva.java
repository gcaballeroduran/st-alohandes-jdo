package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

public interface VOReservaColectiva 
{
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
	public int getDuracion();

	/**
	 * 
	 * @return cantidad de propiedades a resrvar.
	 */
	public int getCantidad();

	/**
	 * 
	 * @return reservas individuales.
	 */
	public ArrayList<Reserva> getReservas();

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la reserva.
	 */
	public String toString();

}
