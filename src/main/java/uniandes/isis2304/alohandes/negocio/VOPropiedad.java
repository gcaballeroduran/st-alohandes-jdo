package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;

public interface VOPropiedad 
{

	///////////////////////////////////////
	////////////// MÉTODOS ////////////////
	///////////////////////////////////////

	/**
	 * @return El id de la propiedad
	 */
	public long getId();
	
	/**
	 * @return La capacidad de la propiedad
	 */
	public int getCapacidad();
	
	/**
	 * @return El tamanio en m2 de la propiedad
	 */
	public double getTamanio();
	
	/**
	 * @return El precio de la propiedad
	 */
	public double getPrecio();
	
	/**
	 * @return La fecha de creación de la propiedad
	 */
	public Date getFechaCreacion();
	
	/**
	 * @return El número de días reservados de la propiedad
	 */
	public int getDiasReservados();
	
	/**
	 * @return El piso en el que se encuentra la propiedad
	 */
	public int getPiso();
	
	/**
	 * @return La dirección de la propiedad
	 */
	public String getDireccion();
	
}
