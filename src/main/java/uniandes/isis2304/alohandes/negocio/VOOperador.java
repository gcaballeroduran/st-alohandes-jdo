package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Interfaz para los métodos get de operador.
 */
public interface VOOperador {

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	/**
	 * 
	 * @return numero de RNT.
	 */
	public int getNumero_RNT();

	/**
	 * 
	 * @return vencimiento del RNT
	 */
	public String getVencimiento_RNT();

	/**
	 * 
	 * @return registro del super turismo del operador.
	 */
	public String getRegistro_Super_Turismo();

	/**
	 * 
	 * @return fecha de vencimiento del super tutimo.
	 */
	public String getVencimiento_Registro_ST();

	/**
	 * 
	 * @return categoria del operador.
	 */
	public String getCategoria();

	/**
	 * 
	 * @return direccion del operador.
	 */
	public String getDireccion();

	/**
	 * 
	 * @return hora de apertura.
	 */
	public String getHora_Apertura();

	/**
	 * 
	 * @return hora de cierre.
	 */
	public String getHora_Cierre();

	/**
	 * 
	 * @return tiempo minmo de estadia.
	 */
	public int getTiempo_Minimo();

	/**
	 * 
	 * @return ganacias del anio actual.
	 */
	public double getGanancia_Anio_Actual();

	/**
	 * 
	 * @return ganacias anio de corrido.
	 */
	public double getGanancia_Anio_Corrido();

	/**
	 * 
	 * @return habitaciones del operador.
	 */
	public ArrayList getHabitaciones();

	/**
	 * 
	 * @return apartamentos del operador.
	 */
	public ArrayList getApartamentos();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador.
	 */
	public String toString();
	
}
