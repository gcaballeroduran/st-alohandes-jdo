package uniandes.isis2304.alohandes.negocio;

public interface VOReservaHabitacion {
	
	/**
	 * @return El id de la reserva
	 */
	public long getIdReserva();
	
	/**
	 * @return El tipo de la habitacion
	 */
	public long getIdHabitacion();

	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos.
	 */
	public String toString();


}
