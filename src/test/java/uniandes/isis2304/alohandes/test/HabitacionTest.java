package uniandes.isis2304.alohandes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.sql.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.VOHabitacion;

public class HabitacionTest {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(ClienteTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private Alohandes alohandes;
	
    /* ****************************************************************
	 * 			Métodos de prueba para la tabla Habitacion - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla habitacion
	 * 1. Adicionar una habitacion
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar una habitacion por su identificador
	 */
    @Test
	public void CRDHabitacionTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre habitacion");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de habitacion incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de habitacion incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOHabitacion> lista = alohandes.darVOHabitacion();
			assertEquals ("No debe haber habitacion creados!!", 0, lista.size ());

			// Lectura de los clientes con una habitacion adicionado
			int pPiso = 5;
   			int pCapacidad = 4;
   			double pTamanio = 130.5;
   			double pPrecio = 150000;
   			int pId = 001;
   			int pDiasR = 2;
   			Date pFecha = new Date(2025, 04, 12);
   			String pDireccion = ""; 
   			boolean pIndiv = true;
   			String pEsquema = "";
   			int pTipo =2;
   			long pOperador = 002;
			VOHabitacion habitacion = alohandes.adicionarHabitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pIndiv, pEsquema, pTipo, pOperador);
			lista = alohandes.darVOHabitacion();
			assertEquals ("Debe haber un habitacion creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", habitacion, lista.get (0));

			// Lectura de los clientes con dos clientes adicionados
			int pPiso2 = 5;
   			int pCapacidad2 = 4;
   			double pTamanio2 = 130.5;
   			double pPrecio2 = 150000;
   			int pId2 = 002;
   			int pDiasR2 = 2;
   			Date pFecha2 = new Date(2025, 04, 12);
   			String pDireccion2 = ""; 
   			boolean pIndiv2 = true;
   			String pEsquema2 = "";
   			int pTipo2 =2;
   			long pOperador2 = 002;
			VOHabitacion habitacion2 = alohandes.adicionarHabitacion(pId2, pCapacidad2, pTamanio2, pPrecio2, pFecha2, pDiasR2, pPiso2, pDireccion2, pIndiv2, pEsquema2, pTipo2, pOperador2);
			lista = alohandes.darVOHabitacion();
			assertEquals ("Debe haber dos operadores creados !!", 2, lista.size ());
			assertTrue ("El primer operador adicionado debe estar en la tabla", habitacion.equals (lista.get (0)) || habitacion.equals (lista.get (1)));
			assertTrue ("El segundo operador adicionado debe estar en la tabla", habitacion2.equals (lista.get (0)) || habitacion2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = alohandes.eliminarHabitacionPorId(pId);
			assertEquals ("Debe haberse eliminado una habitacion !!", 1, tbEliminados);
			lista = alohandes.darVOHabitacion();
			assertEquals ("Debe haber un solo habitacion !!", 1, lista.size ());
			assertFalse ("El primer habitacion adicionado NO debe estar en la tabla", habitacion.equals (lista.get (0)));
			assertTrue ("El segundo habitacion adicionado debe estar en la tabla", habitacion2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = alohandes.eliminarClientePorId(pId2);
			assertEquals ("Debe haberse eliminado un habitacion!!", 1, tbEliminados);
			lista = alohandes.darVOHabitacion();
			assertEquals ("La tabla debió quedar vacía !!", 0, lista.size ());
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla habitacion.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla habitacion");
		}
		finally
		{
			alohandes.limpiarAlohandes();
    		alohandes.cerrarUnidadPersistencia ();    		
		}
	}

    /**
     * Método de prueba de la restricción de unicidad sobre el nombre de Cliente
     */
	@Test
	public void unicidadHabitacionTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre de la habitacion");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de habitacion incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de habitacion incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los clienets con la tabla vacía
			List <VOHabitacion> lista = alohandes.darVOHabitacion();
			assertEquals ("No debe haber Habitacion creados!!", 0, lista.size ());

			// Lectura de los clientes con un cliente adicionado
			int pPiso = 5;
   			int pCapacidad = 4;
   			double pTamanio = 130.5;
   			double pPrecio = 150000;
   			int pId = 001;
   			int pDiasR = 2;
   			Date pFecha = new Date(2025, 04, 12);
   			String pDireccion = ""; 
   			boolean pIndiv = true;
   			String pEsquema = "";
   			int pTipo =2;
   			long pOperador = 002;
			VOHabitacion habitacion = alohandes.adicionarHabitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pIndiv, pEsquema, pTipo, pOperador);
			lista = alohandes.darVOHabitacion();
			assertEquals ("Debe haber un cliente creado !!", 1, lista.size ());

			VOHabitacion habitacion2 = alohandes.adicionarHabitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pIndiv, pEsquema, pTipo, pOperador);
			assertNull ("No puede adicionar dos habitaciones con el mismo id !!", habitacion2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla habitacion.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla habitacion");
		}    				
		finally
		{
			alohandes.limpiarAlohandes();
    		alohandes.cerrarUnidadPersistencia ();    		
		}
	}

	/* ****************************************************************
	 * 			Métodos de configuración
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración de tablas válido");
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "HabitacionTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
	
}
