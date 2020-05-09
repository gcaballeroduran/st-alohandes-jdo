package uniandes.isis2304.alohandes.persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.persistencia.PersistenciaAlohandes;

class SQLHabitacion 
{

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra aca para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicacion
	 */
	private PersistenciaAlohandes pa;

	/* ****************************************************************
	 * 			Metodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicacion
	 */
	public SQLHabitacion (PersistenciaAlohandes pp)
	{
		this.pa = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un Habitacion a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @param idHab - El identificador del Habitacion
	 * @param tipo - Tipo de Habitacion
	 * @param individual - Booleano si la habitacion es individual (t) o compartida (f)
	 * @param esquema - Ruta del esquema de la Habitacion
	 * @return El número de tuplas insertadas
	 */
	public long adicionarHabitacion (PersistenceManager pm, long idHab, int tipo, boolean individual, String esquema, long idOp) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaHabitacion () + "(id, tipo, individual, esquema) values (?, ?, ?, ?, ?, ?)");
		q.setParameters(idHab, tipo, individual, esquema, idOp);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar Habitacion de la base de datos de Alohandes, por su tipo
	 * @param pm - El manejador de persistencia
	 * @param tipoS - El tipo del Habitacion
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarHabitacionPorTipo (PersistenceManager pm, String tipoS)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pa.darTablaHabitacion () + " WHERE tipo = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL,sql);
		q.setParameters(tipoS);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SERVICIO de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador de la Habitacion
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarHabitacionPorId (PersistenceManager pm, long idSer)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pa.darTablaHabitacion () + " WHERE id = ?";
		Query q = pm.newQuery(SQL, sql);
		sql += "COMMIT TRAN";
		q.setParameters(idSer);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN Habitacion de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Habitacion
	 * @return El objeto Habitacion que tiene el identificador dado
	 */
	public Habitacion darHabitacionPorId (PersistenceManager pm, long idSer) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaHabitacion () + " WHERE id = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
		q.setParameters(idSer);
		return (Habitacion) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS BARES de la 
	 * base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param tipoSer - El tipo del servicio buscado
	 * @return Una lista de objetos SERVICIO que tienen el tipo dado
	 */
	public List<Habitacion> darHabitacionPorTipo(PersistenceManager pm, String tipoSer) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaHabitacion () + " WHERE tipo = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL,sql);
		q.setResultClass(Habitacion.class);
		q.setParameters(tipoSer);
		return (List<Habitacion>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Servicios de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Servicio
	 */
	public List<Habitacion> darHabitaciones (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaHabitacion ());
		q.setResultClass(Habitacion.class);
		return (List<Habitacion>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Servicios de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Servicio
	 */
	public ArrayList<Habitacion> darHabitacionesDisponibles(PersistenceManager pm)
	{
		
		String sql = "SELECT * FROM " + pa.darTablaHabitacion () + "AS hab"; 
		sql+="INNER JOIN "+ pa.darTablaPropiedad() +"AS prop ON (habilitada=1)";
		sql+= "INNER JOIN "+ pa.darTablaReservaHabitacion()+ "AS ra ON (ap.idHabitacion=ra.id)";
		sql+= "INNER JOIN "+ pa.darTablaReserva()+ "AS re ON (ra.re = re.id)";
		sql += "WHERE (fechaInicio > currentDate() ) AND (fechaFin < currentDate() )";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
		return (ArrayList<Habitacion>) q.executeList();
	}

	public void habilitarHabitacion(PersistenceManager pm, long hab)
	{
		String sql = "UPDATE " + pa.darTablaPropiedad();
		sql += "SET habilitada = 1 ";
		sql += "WHERE id= "+hab;
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
	}
	
	public void deshabilitarHabitacion(PersistenceManager pm, long hab)
	{
		String sql = "UPDATE " + pa.darTablaPropiedad();
		sql += "SET habilitada = 0 ";
		sql += "WHERE id= "+hab;
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
	}


}
