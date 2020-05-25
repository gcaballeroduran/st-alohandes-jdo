package uniandes.isis2304.alohandes.persistencia;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.persistencia.PersistenciaAlohandes;

class SQLApartamento 
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
	public SQLApartamento (PersistenciaAlohandes pp)
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
	public long adicionarApartamento (PersistenceManager pm, long idApt, boolean am, String desMenaje, String descrSeguro, int venceSeguro, long idOp) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaApartamento () + "(id, amueblado, habitaciones, descripcion_menaje, descripcion_seguro, tiene_seguro) values ( ?, ?, ?, ?,?,?)");
        q.setParameters(idApt, am, desMenaje, descrSeguro, venceSeguro);
        return (long) q.executeUnique();
	}



	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SERVICIO de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador de la Habitacion
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarApartamentoPorId (PersistenceManager pm, long idApt)
	{
		String a = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE ";
		a +="BEGIN TRAIN";
        a = "DELETE FROM " + pa.darTablaApartamento () + " WHERE id = ?";
        a +="COMMIT TRAN";
        Query q = pm.newQuery(SQL,a);
        q.setParameters(idApt);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN Habitacion de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador del Habitacion
	 * @return El objeto Habitacion que tiene el identificador dado
	 */
	public Apartamento darApartamentoPorId (PersistenceManager pm, long idSer) 
	{
		String a = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE ";
		a +="BEGIN TRAIN";
		a = "SELECT * FROM " + pa.darTablaApartamento() + " WHERE id = ?";
		a +="COMMIT TRAN";
		Query q = pm.newQuery(SQL, a);
		q.setResultClass(Apartamento.class);
		q.setParameters(idSer);
		return (Apartamento) q.executeUnique();
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Apartamento de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Servicio
	 */
	public List<Apartamento> darApartamentos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaApartamento ());
		q.setResultClass(Apartamento.class);
		return (List<Apartamento>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Servicios de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Apartamento
	 */
	public ArrayList<Apartamento> darApartamentosDisponibles(PersistenceManager pm)
	{
		String sql = "SELECT * FROM " + pa.darTablaApartamento () + " AS ap"; 
		sql+=" INNER JOIN "+ pa.darTablaPropiedad() +" AS prop ON (habilitada=1)";
		sql+= " INNER JOIN "+ pa.darTablaReservaApartamento()+ " AS ra ON (ap.idApartamento=ra.id)";
		sql+= " INNER JOIN "+ pa.darTablaReserva()+ " AS re ON (ra.re = re.id)";
		sql += " WHERE (fechaInicio > currentDate() ) AND (fechaFin < currentDate() )";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
		return (ArrayList<Apartamento>) q.executeList();
	}
	
	public void habilitarApartamento(PersistenceManager pm, long hab)
	{
		String sql = "UPDATE " + pa.darTablaPropiedad();
		sql += "SET habilitada = 1 ";
		sql += "WHERE id= "+hab;
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
	}
	
	public void deshabilitarApartamento(PersistenceManager pm, long hab)
	{
		String sql = "UPDATE " + pa.darTablaPropiedad();
		sql += "SET habilitada = 0 ";
		sql += "WHERE id= "+hab;
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Habitacion.class);
	}
	
	
	

}
