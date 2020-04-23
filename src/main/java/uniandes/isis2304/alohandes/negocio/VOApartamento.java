package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;

public interface VOApartamento 
{
	///////////////////////////////////////
	////////////// METODOS ////////////////
	///////////////////////////////////////

	/**
	 * @return Si el apartamento esta amoblado
	 */
	public boolean isAmueblado();

	/**
	 * @return El numero de habitaciones en el apartamento
	 */
	public int getHabitaciones();
	
	/**
	 * @return La descripcion del apartamento
	 */
	public String getDescripcionMenaje();
	
	/**
	 * @return La fecha de vencimiento del seguro del apartamento
	 */
	public Date getVencimientoSeguro();
	
	/**
	 * @return La descripcion del seguro del apartamento
	 */
	public String getDescripcionSeguro();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del apartamento
	 */
	public String toString();

}

