package uniandes.isis2304.alohandes.negocio;



public class Servicio implements VOServicio
{
	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////

	/** El identificador ÚNICO del servicio */
	private long id;

	/** El tipo de servicio */
	private String tipo;

	/** El precio del servicio */
	private double precio;

	/** El intervalo en el que se paga este servicio, cada cuanto debe pagarse en días */
	private int intervaloPago;

	///////////////////////////////////////
	/////////// Constructor ///////////////
	///////////////////////////////////////

	/** Constructor por defecto */
	public Servicio()
	{
		id = 0;
		tipo = "";
		precio = 0;
		intervaloPago = 0;
	}

	/**
	 * Constructor con valores
	 * @param pID identificador del servicio
	 * @param pTipo tipo de servicio
	 * @param pPrecio precio del servicio
	 * @param pIntervalo intervalo de pago del servicio
	 */
	public Servicio(long pID, String pTipo, double pPrecio, int pIntervalo)
	{
		id = pID;
		tipo = pTipo;
		precio = pPrecio;
		intervaloPago = pIntervalo;
	}


	///////////////////////////////////////
	////////////// GET / SET //////////////
	///////////////////////////////////////


	public long getId() {
		return id;
	}

	public void setId(long pId) {
		this.id = pId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String pTipo) {
		this.tipo = pTipo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double pPrecio) {
		this.precio = pPrecio;
	}

	public int getIntervaloPago() {
		return intervaloPago;
	}

	public void setIntervaloPago(int pIntervaloPago) {
		this.intervaloPago = pIntervaloPago;
	}

	///////////////////////////////////////
	//////////// Otros M�todos ////////////
	///////////////////////////////////////

	/**
	 * @param prop - El servicio de la propiedad
	 * @return True si tienen el mismo id
	 */
	public boolean equals(Object serv) 
	{
		Servicio tb = (Servicio) serv;
		return id == tb.id;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del servicio
	 */
	public String toString() 
	{
		return "Servicio [id=" + id + ", tipo=" + tipo + ", precio=" + precio + ", intervaloPago=" + intervaloPago + "]";
	}

}
