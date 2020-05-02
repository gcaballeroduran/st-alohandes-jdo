package uniandes.isis2304.alohandes.negocio;

/**
 * Interfaz para los métodos get de usuario.
 */
public interface VOUsuario {
	

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	/**
	 * 
	 * @return log in
	 */
	public String getLogIn();


	/**
	 * 
	 * @return tipo de id
	 */
	public String getTipoId();

	/**
	 * 
	 * @return numero de id.
	 */
	public long getNumeroId();


	/**
	 * 
	 * @return relacionU.
	 */
	public String getRelacioU();

	
}
