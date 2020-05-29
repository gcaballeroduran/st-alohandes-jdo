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
	public long adicionarCliente(PersistenceManager pm,String id, String medioPago, int reservas){
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCliente() + "(id, medio_Pago) values (?, ?)");
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
		String sql = "SELECT * FROM " + pp.darTablaCliente() + " WHERE id = "+ idCliente;
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
		sql += " SELECT count( cli.reservas )as num, (res.fecha_fin - res.fecha_inicio ) as noches";
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
	
	/* *****************************************************
	 *                REQUERIMIENTO: RFC13
	******************************************************* */
	

	/**
	 * Mostrar clientes buenos.
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de objetos,con los datos del cliente y de por que es un buen cliente.
	 */
	public List<Object> darBuenosClientes(PersistenceManager pm)
	{
		String sql = "with res as (";
		sql += " SELECT count(*) AS mes , c.id as id, count(c.reservas) as reservas";
		sql += " FROM " + pp.darTablaCliente()+" c";
		sql+= " INNER JOIN " + pp.darTablaReserva()+" r ON (c.reservas = r.id)";
		sql+= " INNER JOIN " + pp.darTablaPropiedad()+" p ON (p.id = r.propiedad)";
		sql += " WHERE to_char(r.fecha_inicio,'MM')- to_char(( ";
		sql += " SELECT r2.fecha_inicio";
		sql += " FROM " + pp.darTablaCliente()+" c2";
		sql+= " INNER JOIN " + pp.darTablaReserva()+" r2 ON (c2.reservas = r2.id)";
		sql+= " INNER JOIN " + pp.darTablaPropiedad()+" p2 ON (p2.id = r2.propiedad)";
		sql += " WHERE c.id = c2.id";
		sql += ")) = -1";
		sql += "  group by c.id";
		sql += ")";
		sql +=" SELECT  c.*, p.precio , h.tipo, re.mes";
		sql += " FROM "+ pp.darTablaCliente()+" c";
		sql+= " INNER JOIN " + pp.darTablaReserva()+" r ON (c.reservas = r.id)";
		sql+= " INNER JOIN " + pp.darTablaPropiedad()+" p ON (p.id = r.propiedad)";
		sql+= " INNER JOIN " + pp.darTablaHabitacion()+" h ON (p.id = h.id)";
		sql+= " INNER JOIN " + pp.darTablaApartamento()+" ap ON (p.id = ap.id)";
		sql +=" INNER JOIN res re ON (re.id = c.id)";
		sql += " WHERE p.precio > 150 OR h.tipo = 3 OR re.mes = re.reservas";
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}
	
}
