package uniandes.isis2304.alohandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.Usuario;

public class SQLUsuario {

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
	public SQLUsuario (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	  * Crea y ejecuta la sentencia SQL para adicionar un USUARIO a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	  * @param logIn - log in del ususario.
	 * @param tipoId - tipo de identificacion del ususario.
	 * @param numeroId - numero de id del ususario.
	 * @param relacionU -  relacion de usuario con la universidad.
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarUsuario(PersistenceManager pm,String logIn, String tipoId, long numeroId, String relacionU){
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaUsuario() + "(logIn,tipoId,Id,relacionU) values (?, ?, ?, ?)");
        q.setParameters( logIn,tipoId,numeroId,relacionU);
        return (long) q.executeUnique();
		
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar USUARIOS de la base de datos de Alohandes, por su logIn
	 * @param pm - El manejador de persistencia
	 * @param id - id del usuario
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarUsuarioPorId(PersistenceManager pm, long id)
	{
		String sql =  "DELETE FROM " + pp.darTablaUsuario()+ " WHERE id = "+ id;
		 Query q = pm.newQuery(SQL, sql);
	     q.setParameters(id);
	     return (long) q.executeUnique();               
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN USUARIO de la 
	 * base de datos de Alohandes, por su logIn
	 * @param pm - El manejador de persistencia
	 * @param id - id del usuario
	 * @return El objeto USUARIO que tiene el identificador dado
	 */
	public Usuario darUsuarioPorId (PersistenceManager pm, long id) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaUsuario() + " WHERE id = "+ id;
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql );
		q.setResultClass(Reserva.class);
		q.setParameters(id);
		return (Usuario) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS USUARIOS de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos USUARIO
	 */
	public List<Usuario> darUsuarios(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaUsuario());
		q.setResultClass(Usuario.class);
		return (List<Usuario>) q.executeList();
	}
	
}
