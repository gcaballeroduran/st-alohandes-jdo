package uniandes.isis2304.alohandes.negocio;

public interface VOReservaApartamento {

	/**
	 * @return El id de la reserva
	 */
	public long getIdReserva();
	
	/**
	 * @return El tipo del apartamento
	 */
	public long getIdApartamento();

	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos.
	 */
	public String toString();

	
}
