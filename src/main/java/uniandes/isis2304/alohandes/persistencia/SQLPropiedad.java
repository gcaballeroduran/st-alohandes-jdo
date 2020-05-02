package uniandes.isis2304.alohandes.persistencia;

import java.sql.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Propiedad;

class SQLPropiedad
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
	public SQLPropiedad (PersistenciaAlohandes pp)
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
	public long adicionarPropiedad (PersistenceManager pm, long idProp, int capacidad, double precio, double tam, int diasR, int piso, Date fechaCrea, String direccion) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaPropiedad () + "(id, capacidad, precio, tamanio, dias_reservados, piso, fecha_creacion, direccion) values (?, ?, ?, ?, ?)");
		q.setParameters(idProp, capacidad, precio, tam, diasR, piso,  fechaCrea, direccion);
		return (long) q.executeUnique();
	}



	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SERVICIO de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSer - El identificador de la Habitacion
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarPropiedadPorId (PersistenceManager pm, long idSer)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaPropiedad () + " WHERE id = ?");
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
	public Propiedad darPropiedadPorId (PersistenceManager pm, long idSer) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaPropiedad() + " WHERE id = ?");
		q.setResultClass(Propiedad.class);
		q.setParameters(idSer);
		return (Propiedad) q.executeUnique();
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS Apartamento de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Servicio
	 */
	public List<Propiedad> darPropiedades (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaPropiedad ());
		q.setResultClass(Propiedad.class);
		return (List<Propiedad>) q.executeList();
	}


}
