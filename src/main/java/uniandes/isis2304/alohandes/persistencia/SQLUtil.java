/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto ?
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * 
 */
class SQLUtil
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAlohandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param persistenciaAlohandes - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaAlohandes persistenciaAlohandes)
	{
		this.pp = persistenciaAlohandes;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqAlohandes () + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas:
	 * RESERVA_HABITACION, RESERVA_APARTAMENTO, RESERVA, RESERVA_COLECTIVA, SERVICIO,
	 * APARTAMENTO, HABITACION, PROPIEDAD, CLIENTE, OPERADOR, USUARIO
	 * respectivamente
	 */
	public long [] limpiarAlohandes(PersistenceManager pm)
	{
		Query qReservaHabitacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaHabitacion());
		Query qReservaApartamento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaApartamento());
		Query qReserva = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva());
		Query qReservaColectiva = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaColectiva());
		Query qServicio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaServicio());
		Query qApartamento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaApartamento());
		Query qHabitacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaHabitacion());
		Query qPropiedad = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPropiedad());
        Query qCliente = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCliente());          
        Query qOperador = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOperador());
        Query qUsuario = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaUsuario());

        
        
        long reservaApartamentoEliminadas = (long) qReservaApartamento.executeUnique ();
        long reservaHabitacionEliminados = (long) qReservaHabitacion.executeUnique ();
        long reservaEliminadas = (long) qReserva.executeUnique ();
        long reservaColectivaEliminadas = (long) qReservaColectiva.executeUnique ();
        long servicioEliminados = (long) qServicio.executeUnique ();
        long apartamentoEliminados = (long) qApartamento.executeUnique ();
        long habitacionEliminadas = (long) qHabitacion.executeUnique ();
        long propiedadEliminadas = (long) qPropiedad.executeUnique ();
        long clienteEliminados = (long) qCliente.executeUnique ();
        long operadorEliminados = (long) qOperador.executeUnique ();
        long usuarioEliminados = (long) qUsuario.executeUnique ();
        return new long[] { reservaApartamentoEliminadas, 
        		reservaHabitacionEliminados, reservaEliminadas, reservaColectivaEliminadas, 
        		servicioEliminados, apartamentoEliminados, habitacionEliminadas, propiedadEliminadas,
        		clienteEliminados, operadorEliminados, usuarioEliminados};
	}

}
