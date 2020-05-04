package uniandes.isis2304.parranderos.test;

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
import uniandes.isis2304.alohandes.negocio.VOPropiedad;

public class ApartamentoTest {
	
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
	 * 			Métodos de prueba para la tabla Apartamento - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba los apartamentos sobre la tabla habitacion
	 * 1. Adicionar un apartamento
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar una habitacion por su identificador
	 */
    @Test
	public void CRDApartamentoTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre Apartamento");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de apartamento incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de apartamento incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOPropiedad> lista = alohandes.darVOPropiedad();
			assertEquals ("No debe haber apartamento creados!!", 0, lista.size ());

			// Lectura de los clientes con una habitacion adicionado
			int pPiso = 5;
  			int pCapacidad = 4;
  			double pTamanio = 130.5;
  			double pPrecio = 150000;
  			int pId = 001;
  			int pDiasR = 2;
  			Date pFecha = new Date(2025, 04, 12);
  			String pDireccion = ""; 
  			boolean pAmueblado= true;
  			String pDMenaje = "";
  			int pTipo =2;
  			int pHabitaciones = 3;
  			Date pVenceSeguro = new Date(2023, 06, 21);
  			String pDSeguro = "";
  			long pOperador = 002;
			VOPropiedad apartamento = alohandes.adicionarApartamento(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);
			lista = alohandes.darVOPropiedad();
			assertEquals ("Debe haber un Apartamento creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", apartamento, lista.get (0));

			// Lectura de los clientes con dos clientes adicionados
			int pPiso2 = 5;
  			int pCapacidad2 = 4;
  			double pTamanio2 = 130.5;
  			double pPrecio2 = 150000;
  			int pId2 = 002;
  			int pDiasR2 = 2;
  			Date pFecha2 = new Date(2025, 04, 12);
  			String pDireccion2 = ""; 
  			boolean pAmueblado2= true;
  			String pDMenaje2 = "";
  			int pTipo2 =2;
  			int pHabitaciones2 = 3;
  			Date pVenceSeguro2 = new Date(2023, 06, 21);
  			String pDSeguro2 = "";
  			long pOperador2 = 002;
			VOPropiedad apartamento2 = alohandes.adicionarApartamento(pId2, pCapacidad2, pTamanio2, pPrecio2, pFecha2, pDiasR2, pPiso2, pDireccion2, pAmueblado2, pHabitaciones2, pDMenaje2, pVenceSeguro2, pDSeguro2, pOperador2);
			lista = alohandes.darVOPropiedad();
			assertEquals ("Debe haber dos apartamentos creados !!", 2, lista.size ());
			assertTrue ("El primer apartamento adicionado debe estar en la tabla", apartamento.equals (lista.get (0)) || apartamento.equals (lista.get (1)));
			assertTrue ("El segundo apartamento adicionado debe estar en la tabla", apartamento2.equals (lista.get (0)) || apartamento2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = alohandes.eliminarHabitacionPorId(pId);
			assertEquals ("Debe haberse eliminado una apartamento !!", 1, tbEliminados);
			lista = alohandes.darVOPropiedad();
			assertEquals ("Debe haber un solo apartamento !!", 1, lista.size ());
			assertFalse ("El primer apartamento adicionado NO debe estar en la tabla", apartamento.equals (lista.get (0)));
			assertTrue ("El segundo apartamento adicionado debe estar en la tabla", apartamento2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = alohandes.eliminarClientePorId(pId2);
			assertEquals ("Debe haberse eliminado un apartamento!!", 1, tbEliminados);
			lista = alohandes.darVOPropiedad();
			assertEquals ("La tabla debió quedar vacía !!", 0, lista.size ());
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de apartamento sobre la tabla habitacion.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla apartamento");
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
	public void unicidadApartamentoTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre de la apartamento");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de apartamento incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de apartamento incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los apartamento con la tabla vacía
			List <VOPropiedad> lista = alohandes.darVOPropiedad();
			assertEquals ("No debe haber Habitacion creados!!", 0, lista.size ());

			// Lectura de los apartamento con un cliente adicionado
			int pPiso = 5;
  			int pCapacidad = 4;
  			double pTamanio = 130.5;
  			double pPrecio = 150000;
  			int pId = 001;
  			int pDiasR = 2;
  			Date pFecha = new Date(2025, 04, 12);
  			String pDireccion = ""; 
  			boolean pAmueblado= true;
  			String pDMenaje = "";
  			int pTipo =2;
  			int pHabitaciones = 3;
  			Date pVenceSeguro = new Date(2023, 06, 21);
  			String pDSeguro = "";
  			long pOperador = 002;
			VOPropiedad apartamento = alohandes.adicionarApartamento(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);
			lista = alohandes.darVOPropiedad();
			assertEquals ("Debe haber un Apartamento creado !!", 1, lista.size ());

			VOPropiedad apartamento2 = alohandes.adicionarApartamento(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);
			assertNull ("No puede adicionar dos Apartamento con el mismo id !!", apartamento2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla apartamento.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla apartamento");
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "ApartamentoTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	

}
