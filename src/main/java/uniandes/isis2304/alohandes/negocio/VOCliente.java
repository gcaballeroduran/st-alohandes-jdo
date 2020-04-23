package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de cliente.
 */
public interface VOCliente {
	
	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
	
	/**
	 * 
	 * @return medio de pago del cliente.
	 */
	public String getMedioPago();
	
	/**
	 * 
	 * @return numero de reservas del cliente.
	 */
	public int getReservas();
	
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del cliente.
	 */
	public String toString();

}
