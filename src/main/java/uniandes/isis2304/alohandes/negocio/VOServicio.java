package uniandes.isis2304.alohandes.negocio;


/**
 * Interfaz para los métodos get de BAR.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Angela Vargas
 */
public interface VOServicio 
{
	/////////////////////////////////////////
	///////////// Métodos ///////////////////
	/////////////////////////////////////////
	
	/**
	 * @return El id del servicio
	 */
	public long getId();
	
	/**
	 * @return El tipo de servicio
	 */
	public String getTipo();
	
	/**
	 * @return El precio del servicio
	 */
	public double getPrecio();
		
	/**
	 * @return El tintervalo de pago del servicio
	 */
	public int getIntervalo_Pago();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del servicio
	 */
	public String toString();

}

