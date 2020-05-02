package uniandes.isis2304.alohandes.negocio;

public class Usuario implements VOUsuario {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Log in unico del usuario.
	 */
	private String logIn;
	
	/**
	 * tipo de identificacion del usuario.
	 */
	private String tipoId;
	
	/**
	 * numero de id del usuario.
	 */
	private long numeroId;
	
	/**
	 * relacion del usuario con la universidad.
	 */
	private String relacionU;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto.
	 */
	public Usuario(){
		
		logIn = "";
		tipoId = null;
		numeroId = 0;
		relacionU = null;
	}
	
	/**
	 * Constructor con valores.
	 * @param logIn - log in del ususario.
	 * @param tipoId - tipo de identificacion del ususario.
	 * @param numeroId - numero de id del ususario.
	 * @param relacionU -  relacion de usuario con la universidad.
	 */
	public Usuario(String logIn, String tipoId, long numeroId, String relacionU){
		
		this.logIn = logIn;
		this.tipoId = tipoId;
		this.numeroId = numeroId;
		this.relacionU = relacionU;
		
	}
	
	/**
	 * 
	 * @return log in
	 */
	public String getLogIn() {
		return logIn;
	}

	/**
	 * 
	 * @param logIn - nuevo log in del ususario.
	 */
	public void setLogIn(String logIn) {
		this.logIn = logIn;
	}

	/**
	 * 
	 * @return tipo de id
	 */
	public String getTipoId() {
		return tipoId;
	}


	/**
	 * 
	 * @param tipoId - nuevo tipo de id del ususario.
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	/**
	 * 
	 * @return numero de id.
	 */
	public long getNumeroId() {
		return numeroId;
	}

	/**
	 * 
	 * @param numeroId - nuevo numero de id del ususario.
	 */
	public void setNumeroId(long numeroId) {
		this.numeroId = numeroId;
	}

	/**
	 * 
	 * @return relacionU.
	 */
	public String getRelacioU() {
		return relacionU;
	}

	/**
	 * 
	 * @param relacionU - nueva relacionU del ususario.
	 */
	public void setRelacioU(String relacionU) {
		this.relacionU = relacionU;
	}

	

}
