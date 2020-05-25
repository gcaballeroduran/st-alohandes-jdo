package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

public class Operador extends Usuario implements VOOperador {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	

	/**
	 *  numero de RNT del operador.
	 */
	private int numero_RNT;
	
	/**
	 * fecha de vencimiento del RNT.
	 */
	private Date vencimiento_RNT;
	
	/**
	 *  regsitro del super turismo del operador.
	 */
	private String registro_Super_Turismo;
	
	/**
	 * fecha devencimiento del regsitro del super turismo del operador..
	 */
	private Date vencimiento_Registro_ST;
	
	/**
	 *  Categoria a la que pertenece el operador.
	 */
	private String categoria;
	
	/**
	 * Direccion del operador.
	 */
	private String direccion;
	
	/**
	 *  Hora de apertura.
	 */
	private Date hora_Apertura;
	
	/**
	 *  Hora de cierre.
	 */
	private Date hora_Cierre;
	
	/**
	 * Tiempo minimo de estadia.
	 */
	private int tiempo_Minimo;
	
	/**
	 *  Ganancias actuales.
	 */
	private double ganancia_Anio_Actual;
	
	/**
	 *  Ganancias del anio corrido.
	 */
	private double ganancia_Anio_Corrido;
	
	/**
	 * Habitaciones del operador.
	 */
	private ArrayList<Habitacion> habitaciones;
	
	/**
	 *  Apartamentos del operador.
	 */
	private ArrayList<Apartamento> apartamentos;
	
	private long id;
	
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto.
	 */
	public Operador(){
		
		super(0,null, "", null);
		numero_RNT = 0;
		vencimiento_RNT = null;
		registro_Super_Turismo = "";
		vencimiento_Registro_ST = null;
		categoria = null;
		direccion = "";
		hora_Apertura = null;
		hora_Cierre = null;
		tiempo_Minimo = 1;
		ganancia_Anio_Actual = 0;
		ganancia_Anio_Corrido = 0;
		habitaciones = new ArrayList<>();
		apartamentos = new ArrayList<>();
		id = 0;
	}
	
	/**
	 * Constructor con valores.
	 * @param id -  id del operador.
	 * @param numeroRNT -  numero de RNT.
	 * @param vencimientoRNT - fecha de vencimiento de RNT.
	 * @param registroSuperTurismo -  registro del super turismo del operador.
	 * @param vencimientoRegistroSuperTurismo fecha de vencimiento del registro de super turismo.
	 * @param categoria - categoria del operador.
	 * @param direccion -  direccion del operador.
	 * @param horaApertura - hora de apertura.
	 * @param horaCierre - hora de cierre.
	 * @param tiempoMinimo -  tiempo minimo (Mayor a 0)
	 * @param gananciaAnioActual - ganacia del anio actual.
	 * @param gananciAnioCorrido - ganacia anio corrido.
	 * @param habitaciones - habitaciones del operador.
	 * @param apartamentos - apartamentos del operador.
	 */
	public Operador(String logIn,String tipoId, long numeroId,String relacionU, int numero_RNT, Date vencimiento_RNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,String categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempo_Minimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos ){
		
		super(numeroId, tipoId, logIn, relacionU);
		this.numero_RNT = numero_RNT;
		this.vencimiento_RNT = vencimiento_RNT;
		this.registro_Super_Turismo = registroSuperTurismo;
		this.vencimiento_Registro_ST = vencimientoRegistroSuperTurismo;
		this.categoria = categoria;
		this.direccion = direccion;
		this.hora_Apertura = horaApertura;
		this.hora_Cierre = horaCierre;
		this.tiempo_Minimo = tiempo_Minimo;
		this.ganancia_Anio_Actual = gananciaAnioActual;
		this.ganancia_Anio_Corrido = gananciAnioCorrido;
		this.habitaciones = habitaciones;
		this.apartamentos = apartamentos;
		this.id = numeroId;
		
	}
	
	
	/**
	 * 
	 * @return numero de RNT.
	 */
	public int getNumero_RNT() {
		return numero_RNT;
	}

	/**
	 * 
	 * @param numeroRNT - nuevo numeroRNT del operador.
	 */
	public void setNumero_RNT(int numero_RNT) {
		this.numero_RNT = numero_RNT;
	}

	/**
	 * 
	 * @return vencimiento del RNT
	 */
	public Date getVencimiento_RNT() {
		return vencimiento_RNT;
	}

	/**
	 * 
	 * @param vencimientoRNT - nueva fecha de vencimiento del RNT del operador.
	 */
	public void setVencimiento_RNT(Date vencimiento_RNT) {
		this.vencimiento_RNT = vencimiento_RNT;
	}

	/**
	 * 
	 * @return registro del super turismo del operador.
	 */
	public String getRegistro_Super_Turismo() {
		return registro_Super_Turismo;
	}

	/**
	 * 
	 * @param registroSuperTurismo - nuevo registro del super turismo del operador.
	 */
	public void setRegistro_Super_Turismo(String registroSuperTurismo) {
		this.registro_Super_Turismo = registroSuperTurismo;
	}

	/**
	 * 
	 * @return fecha de vencimiento del super tutimo.
	 */
	public Date getVencimiento_Registro_ST() {
		return vencimiento_Registro_ST;
	}

	/**
	 * 
	 * @param vencimientoRegistroSuperTurismo - nueva fecha de vencimiento del super turismo.
	 */
	public void setVencimiento_Registro_ST(Date vencimientoRegistroSuperTurismo) {
		this.vencimiento_Registro_ST = vencimientoRegistroSuperTurismo;
	}

	/**
	 * 
	 * @return categoria del operador.
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * 
	 * @param categoria - nueva categoria del operador.
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * 
	 * @return direccion del operador.
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * 
	 * @param direccion - nueva direccion del operador.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * 
	 * @return hora de apertura.
	 */
	public Date getHora_Apertura() {
		return hora_Apertura;
	}

	/**
	 * 
	 * @param horaApertura - nueva hora de apertura.
	 */
	public void setHora_Apertura(Date horaApertura) {
		this.hora_Apertura = horaApertura;
	}

	/**
	 * 
	 * @return hora de cierre.
	 */
	public Date getHora_Cierre() {
		return hora_Cierre;
	}

	/**
	 * 
	 * @param horaCierre - nueva hora de cierre.
	 */
	public void setHora_Cierre(Date horaCierre) {
		this.hora_Cierre = horaCierre;
	}
	
	/**
	 * 
	 * @param tiempo_Minimo -  nuevo tiempo_Minimo.
	 */
	public void setTiempo_Minimo(int tiempo_Minimo) {
		this.tiempo_Minimo = tiempo_Minimo;
		
	}

	/**
	 * 
	 * @return tiempo_Minimo de estadia.
	 */
	public int getTiempo_Minimo() {
		return tiempo_Minimo;
	}

	
	/**
	 * 
	 * @return ganacias del anio actual.
	 */
	public double getGanancia_Anio_Actual() {
		return ganancia_Anio_Actual;
	}

	/**
	 * 
	 * @param gananciaAnioActual - nuevas ganancias del anio actual.
	 */
	public void setGanancia_Anio_Actual(double gananciaAnioActual) {
		this.ganancia_Anio_Actual = gananciaAnioActual;
	}

	/**
	 * 
	 * @return ganacias anio de corrido.
	 */
	public double getGanancia_Anio_Corrido() {
		return ganancia_Anio_Corrido;
	}

	/**
	 * 
	 * @param gananciAnioCorrido - nuevas ganancias anio de corrido.
	 */
	public void setGananciAnioCorrido(double gananciAnioCorrido) {
		this.ganancia_Anio_Corrido = gananciAnioCorrido;
	}

	/**
	 * 
	 * @return habitaciones del operador.
	 */
	public ArrayList getHabitaciones() {
		return habitaciones;
	}

	/**
	 * 
	 * @param habitaciones - nuevas habitaciones del operador.
	 */
	public void setHabitaciones(ArrayList habitaciones) {
		this.habitaciones = habitaciones;
	}
	
	public void agregarHabitacion(int pID, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso,  boolean pIndiv, String pEsquema, int pTipo, long pOperador)
	{
		Habitacion nuevo = new Habitacion(pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pIndiv, pEsquema, pTipo, pOperador);
		habitaciones.add(nuevo);
	}
	
	/**
	 * 
	 * @return apartamentos del operador.
	 */
	public ArrayList getApartamentos() {
		return apartamentos;
	}

	/**
	 * 
	 * @param apartamentos - nuevos apartamentos del operador.
	 */
	public void setApartamentos(ArrayList apartamentos) {
		this.apartamentos = apartamentos;
	}
	
	
	public void agregarApartamento(int pID, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso, boolean pAmueblado, int pHabitaciones, String pDMenaje, Date pVenceSeguro, String pDSeguro)
	{
		Apartamento nuevo = new Apartamento(pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, this.getNumeroId());
		apartamentos.add(nuevo);
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador.
	 */
	public String toString() 
	{
		return "Operador [logIn="+super.getLogIn()+", tipo Id="+super.getTipoId()+", numero de Id=" + getId()
		        +", relacion U="+super.getRelacioU()+ ", numero de RNT=" + numero_RNT + ", vencimiento de RNT=" + vencimiento_RNT +
				", registro de super turismo="+ registro_Super_Turismo+",vencimiento de super turismo="+ vencimiento_Registro_ST+
				",categoria="+categoria+",direccion= "+direccion+ ", hora de apertura="+ hora_Apertura+", hora de cierre="+hora_Cierre+
				",tiempo minimo="+tiempo_Minimo+", ganacia del anio actual="+ganancia_Anio_Actual+", ganancia de anio de corrido="+ ganancia_Anio_Corrido+
				", habitaciones="+habitaciones+ ",apatamentos="+ apartamentos+"]";
	}

}
