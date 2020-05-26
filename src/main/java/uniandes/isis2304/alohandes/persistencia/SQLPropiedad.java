package uniandes.isis2304.alohandes.persistencia;

import java.sql.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Operador;
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
	public long adicionarPropiedad (PersistenceManager pm, long idProp, int capacidad, double precio, double tam, int diasR,String fechaCrea, int piso) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaPropiedad () + "(id, capacidad, precio, tamanio, dias_reservados,piso, fecha_creacion) values (?,?,?, ?, ?,?, TO_DATE(?))");
		q.setParameters(idProp, capacidad, precio, tam, diasR, piso,  fechaCrea);
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
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pa.darTablaPropiedad () + " WHERE id = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
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
		String sql = "SELECT * FROM " + pa.darTablaPropiedad() + " WHERE id = ?";
		Query q = pm.newQuery(SQL, sql);
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
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pa.darTablaPropiedad ();
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Propiedad.class);
		return (List<Propiedad>) q.executeList();
	}
	
	/**  RFC7
	 * Mayor ocupacion segun un tiempo y tipo
	 * @param pm - El manejador de persistencia
	 * @param tiempo - intervalo ded tiempo
	 * @param tipo - tipo de habitacion
	 * @return Una lista de arreglos de objetos, de tamaño 2. Los elementos del arreglo corresponden a los datos de la propuedad y el monto maximo
	 */
	public List<Object> darMayorOcupacion(PersistenceManager pm, Date tiempo, String tipo)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "with tab as(";
		sql +="select count(p.dias_reservados) m";
		sql += " FROM " + pa.darTablaPropiedad()+"p";
		sql+= "INNER JOIN" + pa.darTablaHabitacion()+"hab ON(p.id = hab.id)";
		sql+= "INNER JOIN" + pa.darTablaApartamento()+"ap ON(p.id = ap.id)";
		sql+= "INNER JOIN" + pa.darTablaReservaHabitacion()+"resha ON(hab.id = resha.id_Habitacion)";
		sql+= "INNER JOIN" + pa.darTablaReservaApartamento()+"resap ON(ap.id = resap.id_Apartamento)";
		sql+= "INNER JOIN" + pa.darTablaReserva()+"res ON(res.id = resha.id_Reserva AND res.id = resap.id_Reserva)";
		sql += "WHERE "+ tiempo + ">= res.fecha_Inicio AND tiempo <= res.fecha_ AND "+ tipo +" = hab.tipo";
		sql += ")";
		sql +="Where max(t.m), select  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += "FROM tab t, "+ pa.darTablaPropiedad()+"p";
		sql +="group by  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}

	/**  RFC7
	 * Menor ocupacion segun un tiempo y tipo
	 * @param pm - El manejador de persistencia
	 * @param tiempo - intervalo ded tiempo
	 * @param tipo - tipo de habitacion
	 * @return Una lista de arreglos de objetos, de tamaño 2. Los elementos del arreglo corresponden a los datos de la propuedad y el monto maximo
	 */
	public List<Object> darMenorOcupacion(PersistenceManager pm, Date tiempo, String tipo)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "with tab as(";
		sql +="select  count(p.dias_reservados) m";
		sql += " FROM " + pa.darTablaPropiedad()+"p";
		sql+= "INNER JOIN" + pa.darTablaHabitacion()+"hab ON(p.id = hab.id)";
		sql+= "INNER JOIN" + pa.darTablaApartamento()+"ap ON(p.id = ap.id)";
		sql+= "INNER JOIN" + pa.darTablaReservaHabitacion()+"resha ON(hab.id = resha.id_Habitacion)";
		sql+= "INNER JOIN" + pa.darTablaReservaApartamento()+"resap ON(ap.id = resap.id_Apartamento)";
		sql+= "INNER JOIN" + pa.darTablaReserva()+"res ON(res.id = resha.id_Reserva AND res.id = resap.id_Reserva)";
		sql += "WHERE "+ tiempo + ">= res.fecha_Inicio AND tiempo <= res.fecha_ AND "+ tipo +" = hab.tipo";
		sql += ")";
		sql +="Where min(t.m), select  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += "FROM tab t, "+ pa.darTablaPropiedad()+"p";
		sql +="group by  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += sql += "COMMIT TRAN";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}
	
	/**  RFC7
	 * Menor ocupacion segun un tiempo y tipo
	 * @param pm - El manejador de persistencia
	 * @param tiempo - intervalo ded tiempo
	 * @param tipo - tipo de habitacion
	 * @return Una lista de arreglos de objetos, de tamaño 2. Los elementos del arreglo corresponden a los datos de la propuedad y el monto maximo
	 */
	public List<Object> darMayorIngresos(PersistenceManager pm, Date tiempo, String tipo)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "with tab as(";
		sql +=" select count( res.monto_total )as m";
		sql += " FROM " + pa.darTablaPropiedad()+"p";
		sql+= "INNER JOIN" + pa.darTablaHabitacion()+"hab ON(p.id = hab.id)";
		sql+= "INNER JOIN" + pa.darTablaApartamento()+"ap ON(p.id = ap.id)";
		sql+= "INNER JOIN" + pa.darTablaReservaHabitacion()+"resha ON(hab.id = resha.id_Habitacion)";
		sql+= "INNER JOIN" + pa.darTablaReservaApartamento()+"resap ON(ap.id = resap.id_Apartamento)";
		sql+= "INNER JOIN" + pa.darTablaReserva()+"res ON(res.id = resha.id_Reserva AND res.id = resap.id_Reserva)";
		sql += "WHERE "+tiempo+" >= res.fecha_Inicio AND tiempo <= res.fecha_ AND "+tipo+" = hab.tipo";
		sql += ")";
		sql +="Where max(t.m), select  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += "FROM tab t, "+ pa.darTablaPropiedad()+"p";
		sql +="group by  p.id , p.capacidad, p.tamanio, p.dias_reservados, p.fecha_creacion, p.piso";
		sql += sql += "COMMIT TRAN";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	} 
	
	/* *****************************************************
	 *                REQUERIMIENTO: RFC12
	******************************************************* */

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de la oferta de alohamiento con mayor ocupación
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de propiedades 
	 */
	public List<Propiedad> darOfertaMayorOcupacion(PersistenceManager pm)
	{
		String sql =  "with semana as(";
		sql += " SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , MAX(p.dias_reservados) AS personas, p.id as id ";
		sql += " FROM " + pa.darTablaReserva()+" r ";
		sql+= "  INNER JOIN " +pa.darTablaPropiedad()+" p ON (p.id = r.propiedad) ";
		sql+= " group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),p.id ";
		sql+= ")";
		sql+= " SELECT p.*";
		sql+= " FROM " + pa.darTablaPropiedad() +" p ";
		sql +="  INNER JOIN semana s ON (s.id = p.id)";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de la oferta de alohamiento con menor ocupación
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de propiedades 
	 */
	public List<Propiedad> darOfertaMenorOcupacion(PersistenceManager pm)
	{
		String sql =  "with semana as(";
		sql += " SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , MIN(p.dias_reservados) AS personas, p.id as id ";
		sql += " FROM " + pa.darTablaReserva()+" r ";
		sql+= "  INNER JOIN " +pa.darTablaPropiedad()+" p ON (p.id = r.propiedad) ";
		sql+= " group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),p.id ";
		sql+= ")";
		sql+= " SELECT p.*";
		sql+= " FROM " + pa.darTablaPropiedad() +" p ";
		sql +="  INNER JOIN semana s ON (s.id = p.id)";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información del operador menos solicidado
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de Operadores 
	 */
	public List<Object> darOperadorMenosSolicitado(PersistenceManager pm)
	{
		String sql =  "with semana as(";
		sql += " SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , COUNT(r.propiedad) AS res, o.id as id";
		sql += " FROM " + pa.darTablaReserva()+" r ";
		sql+= "  INNER JOIN " +pa.darTablaPropiedad()+" p ON (p.id = r.propiedad) ";
		sql+= "  INNER JOIN " +pa.darTablaOperador()+" o ON (o.id = p.operador) ";
		sql+= " group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),o.id ";
		sql+= ")";
		sql+= " SELECT MIN(s.res), o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, o.categoria, o.direccion, o.hora_apertura,o.hora_cierre"+
         ",o.tiempo_minimo, o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento";
		sql+= " FROM " + pa.darTablaOperador() +" o ";
		sql +="  INNER JOIN semana s ON (s.id = o.id)";
		sql+="group by o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, o.categoria, o.direccion, o.hora_apertura, o.hora_cierre, o.tiempo_minimo," 
        +"o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información del operador mas solicidado
	 * @param pm - El manejador de persistencia
	 * @return Una lista de arreglos de Operadores 
	 */
	public List<Object> darOperadorMasSolicitado(PersistenceManager pm)
	{
		String sql =  "with semana as(";
		sql += " SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , COUNT(r.propiedad) AS res, o.id as id";
		sql += " FROM " + pa.darTablaReserva()+" r ";
		sql+= "  INNER JOIN " +pa.darTablaPropiedad()+" p ON (p.id = r.propiedad) ";
		sql+= "  INNER JOIN " +pa.darTablaOperador()+" o ON (o.id = p.operador) ";
		sql+= " group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),o.id ";
		sql+= ")";
		sql+= " SELECT MAX(s.res), o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, o.categoria, o.direccion, o.hora_apertura,o.hora_cierre"+
         ",o.tiempo_minimo, o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento";
		sql+= " FROM " + pa.darTablaOperador() +" o ";
		sql +="  INNER JOIN semana s ON (s.id = o.id)";
		sql+="group by o.id, o.numero_rnt, o.vencimiento_rnt, o.registro_super_turismo, o.vencimiento_registro_st, o.categoria, o.direccion, o.hora_apertura, o.hora_cierre, o.tiempo_minimo," 
        +"o.ganancia_anio_actual, o.ganancia_anio_corrido, o.habitacion, o.apartamento";
		
		Query q = pm.newQuery(SQL, sql); 
		return q.executeList();
	}

}
