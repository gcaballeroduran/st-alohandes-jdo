package uniandes.isis2304.alohandes.negocio;


public interface VOHabitacion 
{
	///////////////////////////////////////
	////////////// METODOS ////////////////
	///////////////////////////////////////

	
	/** 
	 * @return True si la habitación es individual false si la habitación es compartida
	 * */
	public boolean isIndividual();

	/** 
	 * @return Ruta de la imagen con el esquema de la habitación 
	 * */
	public String getEsquema();

	/** 
	 * @return Tipo de habitación definido por las constantes 
	 * */
	public int getTipo();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la habitación
	 */
	public String toString();

}
