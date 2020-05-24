package uniandes.isis2304.alohandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Cliente;

public class SQLCliente {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	
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
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLCliente (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	  * Crea y ejecuta la sentencia SQL para adicionar un CLIENTE a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @param id - id del cliente.
	 * @param medioPago - medio de pago del cliente.
	 * @param reservas - reservas del cliente.
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarCliente(PersistenceManager pm,long id, String medioPago, int reservas){
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCliente() + "(id, medio_Pago,reservas) values (?, ?, ?)");
        q.setParameters(id, medioPago,reservas);
        return (long) q.executeUnique();
		
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar CLIENTES de la base de datos de Alohandes, por su id
	 * @param pm - El manejador de persistencia
	 * @param idCliente - id del cliente
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarClientePorId (PersistenceManager pm, long idCliente)
	{
		String sql = "DELETE FROM " + pp.darTablaCliente() + " WHERE id = " +idCliente;
        Query q = pm.newQuery(SQL,sql);
        q.setParameters(idCliente);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN CLIENTE de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idCliente - El identificador del cliente
	 * @return El objeto CLIENTE que tiene el identificador dado
	 */
	public Cliente darClientePorId (PersistenceManager pm, long idCliente) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql ="SELECT * FROM " + pp.darTablaCliente() + " WHERE id = "+ idCliente;
		sql += "COMMIT TRAN";
	    Query q = pm.newQuery(SQL,sql);
		q.setResultClass(Cliente.class);
		q.setParameters(idCliente);
		return (Cliente) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS CLIENTES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CLIENTE
	 */
	public List<Cliente> darClientes(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente());
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	/* *****************************************************
	 *                REQUERIMIENTO: RFC8
	******************************************************* */
	/**
	 * Mostrar clientes frecuentes.
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de objetos, de tamaño 2. Los elementos del arreglo corresponden a los datos del cliente:
	 * 		(id, medio de pago, reservas) 
	 */
	public List<Object> darDineroAnioActual(PersistenceManager pm)
	{
		String sql = "with tiempo as(";
		sql += " count( cli.reservas )as num, (res.fecha_fin - res.fecha_inicio ) as noches";
		sql += " FROM " + pp.darCliente()+"cl";
		sql+= "INNER JOIN" + pp.darTablaReserva()+"res ON(cl.reservas = res.id)";
		sql += "group by (res.fecha_fin - res.fecha_inicio) ";
		sql += ")";
		sql +="SELECT cl.*";
		sql += "FROM tiempo t, "+ pp.darCliente()+"cl";
		sql += "WHERE t.num >= 3 OR t.noches >= 15";
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}

	
}
