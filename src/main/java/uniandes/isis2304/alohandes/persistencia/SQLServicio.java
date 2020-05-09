package uniandes.isis2304.alohandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Servicio;

public class SQLServicio {

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
	public SQLServicio(PersistenciaAlohandes pp)
	{
		this.pa = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un Servicio a la base de datos de Alohandes
	 * @param pID identificador del servicio
	 * @param pTipo tipo de servicio
	 * @param pPrecio precio del servicio
	 * @param pIntervalo intervalo de pago del servicio
	 * @return El número de tuplas insertadas
	 */
	public long adicionarServicio(PersistenceManager pm, long idServ, String pTipo, double pPrecio, int pIntervalo) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaServicio() + "(id, tipo, precio, intervaloPago) values (?, ?, ?, ?)");
        q.setParameters(idServ, pTipo, pPrecio, pIntervalo);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SERVICIO de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Servicio
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarServicioPorId (PersistenceManager pm, long idSer)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pa.darTablaServicio() + " WHERE id = ?";
		sql += "COMMIT TRAN";
        Query q = pm.newQuery(SQL, sql);
        q.setParameters(idSer);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SERVICIO de la base de datos de Alohandes, por su tipo
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Servicio
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarServicioPorTipo (PersistenceManager pm, long idSer)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pa.darTablaServicio() + " WHERE tipo = ?";
		sql += "COMMIT TRAN";
        Query q = pm.newQuery(SQL, sql);
        q.setParameters(idSer);
        return (long) q.executeUnique();
	}
	

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN servicio de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Habitacion
	 * @return El objeto Habitacion que tiene el identificador dado
	 */
	public Servicio darServicioPorId (PersistenceManager pm, long idSer) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaServicio() + " WHERE id = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Servicio.class);
		q.setParameters(idSer);
		return (Servicio) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN servicio de la 
	 * base de datos de Alohandes, por su tipo
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Habitacion
	 * @return El objeto Habitacion que tiene el identificador dado
	 */
	public Servicio darServicioPorTipo (PersistenceManager pm, long idSer) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaServicio() + " WHERE tipo = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Servicio.class);
		q.setParameters(idSer);
		return (Servicio) q.executeUnique();
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Servicios de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Servicio
	 */
	public List<Servicio> darServicios(PersistenceManager pm)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaApartamento ();
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Servicio.class);
		return (List<Servicio>) q.executeList();
	}
	
}
