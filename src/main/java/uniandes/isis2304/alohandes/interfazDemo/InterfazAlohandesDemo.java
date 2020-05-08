/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.interfazDemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.alohandes.interfazApp.PanelDatos;
import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.VOApartamento;
import uniandes.isis2304.alohandes.negocio.VOCliente;
import uniandes.isis2304.alohandes.negocio.VOHabitacion;
import uniandes.isis2304.alohandes.negocio.VOOperador;
import uniandes.isis2304.alohandes.negocio.VOPropiedad;
import uniandes.isis2304.alohandes.negocio.VOReserva;
import uniandes.isis2304.alohandes.negocio.VOReservaColectiva;
import uniandes.isis2304.alohandes.negocio.VOServicio;
import uniandes.isis2304.alohandes.negocio.VOUsuario;

/**
 * Clase principal de la interfaz
 * 
 * 
 */
@SuppressWarnings("serial")

public class InterfazAlohandesDemo extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesDemo.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigDemo.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private Alohandes alohandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAlohandesDemo( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        alohandes = new Alohandes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos para la configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			Demos de Cliente
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Cliente
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    public void demoCliente( )
    {
    	try 
    	{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			String logIn = "g.caballero";
			String tipoId = "Cedula";
			String relacionU = "Estudiante";
			String medioPago = "Tarjeta de credito";
			int numeroId = 001;
			boolean error = false;
			int reservas = 0;
			VOUsuario usuario = alohandes.adicionarUsuario(logIn, tipoId, numeroId, relacionU);
			VOCliente cliente = alohandes.adicionarCliente(numeroId,logIn, tipoId, relacionU, medioPago, reservas);
			if (logIn == null)
			{
				cliente = alohandes.darClientePorId(numeroId);
				usuario = alohandes.darUsuarioPorId(numeroId);
				error = true;
			}
			List <VOCliente> lista = alohandes.darVOTCliente();
			long eliminados = alohandes.eliminarClientePorId(usuario.getNumeroId());
			alohandes.eliminarUsuarioPorId(usuario.getNumeroId());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Clientes\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (error)
			{
				resultado += "*** Exception creando cliente !!\n";
				resultado += "*** Es probable que ese cliente ya existiera\n";
				resultado += "*** Revise el log de Alohandes para más detalles\n";
			}
			resultado += "Adicionado el cliente con logIn: " + logIn + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarCliente(lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados + " clientes eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /* ****************************************************************
	 * 			Demos de Operador
	 *****************************************************************/
    /**
     * Demostración de creación, consulta y borrado de Operador
     * Muestra la traza de la ejecución en el panelDatos
     * 
     * Pre: La base de datos está vacía
     * Post: La base de datos está vacía
     */
    @SuppressWarnings("deprecation")
	public void demoOperador( )
    {
    	try 
    	{
    		// Ejecución de la demo y recolección de los resultados
			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
			String logIn = "Vino tinto";
			String tipoId = "Cedula";
			String relacionU = "Estudiante";
			String medioPago = "Tarjeta de credito";
			int numeroId = 001;
			int numeroRNT = 002;
			Date vencimientoRNT = new Date(2025, 04, 12);
			String registroSuperTurismo = "";
			Date vencimientoRegistroSuperTurismo = new Date(2025, 04, 12);
			String categoria = "";
			String direccion = "";
			Date horaApertura = new Date(2020, 12, 01);
			Date horaCierre = new Date(2021, 12, 01); 
			
			boolean error = false;
			int reservas = 0;
			VOUsuario usuario = alohandes.adicionarUsuario(logIn, tipoId, numeroId, relacionU);
			VOOperador operador = alohandes.adicionarOperador(logIn, tipoId, numeroId, relacionU, numeroRNT, vencimientoRNT,
					registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, 3, 20000, 3000000, null, null);
			if (logIn == null)
			{
				operador = alohandes.darOperadorPorId(numeroId);
				usuario = alohandes.darUsuarioPorId(numeroId);
				error = true;
			}
			List <VOOperador> lista = alohandes.darVOOperadores();
			long eliminados = alohandes.eliminarOperadorPorId(usuario.getNumeroId());
			alohandes.eliminarUsuarioPorId(usuario.getNumeroId());
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "Demo de creación y listado de Operador\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (error)
			{
				resultado += "*** Exception creando operador !!\n";
				resultado += "*** Es probable que ese operador ya existiera\n";
				resultado += "*** Revise el log de Alohandes para más detalles\n";
			}
			resultado += "Adicionado el operador con logIn: " + logIn + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarOperador(lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados + " operador eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
   	 * 			Demos de Habitacion
   	 *****************************************************************/
       /**
        * Demostración de creación, consulta y borrado de habitacion
        * Muestra la traza de la ejecución en el panelDatos
        * 
        * Pre: La base de datos está vacía
        * Post: La base de datos está vacía
        */
       @SuppressWarnings("deprecation")
   	public void demoHabitacion( )
       {
       	try 
       	{
       		// Ejecución de la demo y recolección de los resultados
   			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
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
   			
   			boolean error = false;
   			VOPropiedad propiedad = alohandes.adicionarPropiedad(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion);;
   			VOHabitacion habitacion = alohandes.adicionarHabitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pIndiv, pEsquema, pTipo, pOperador);
   			{
   				propiedad = alohandes.darPropiedadPorId(pId);
   				habitacion = alohandes.darHabitacionPorId(pId);
   				error = true;
   			}
   			List <VOHabitacion> lista = alohandes.darVOHabitacion();
   			long eliminados = alohandes.eliminarHabitacionPorId(pId);
   			alohandes.eliminarPropiedadPorId(pId);
   			
   			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
   			String resultado = "Demo de creación y listado de Habitaciones\n\n";
   			resultado += "\n\n************ Generando datos de prueba ************ \n";
   			if (error)
   			{
   				resultado += "*** Exception creando habitacion !!\n";
   				resultado += "*** Es probable que esa habitacion ya existiera\n";
   				resultado += "*** Revise el log de Alohandes para más detalles\n";
   			}
   			resultado += "Adicionado la habitacion con id: " + pId + "\n";
   			resultado += "\n\n************ Ejecutando la demo ************ \n";
   			resultado +=  "\n" + listarHabitacion(lista);
   			resultado += "\n\n************ Limpiando la base de datos ************ \n";
   			resultado += eliminados + " habitaciones eliminadas\n";
   			resultado += "\n Demo terminada";
      
   			panelDatos.actualizarInterfaz(resultado);
   		} 
       	catch (Exception e) 
       	{
//   			e.printStackTrace();
   			String resultado = generarMensajeError(e);
   			panelDatos.actualizarInterfaz(resultado);
   		}
       }

       /* ****************************************************************
      	 * 			Demos de Apartamento
      	 *****************************************************************/
          /**
           * Demostración de creación, consulta y borrado de apartamento
           * Muestra la traza de la ejecución en el panelDatos
           * 
           * Pre: La base de datos está vacía
           * Post: La base de datos está vacía
           */
          @SuppressWarnings("deprecation")
      	public void demoApartamento( )
          {
          	try 
          	{
          		// Ejecución de la demo y recolección de los resultados
      			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
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
      			
      			boolean error = false;
      			VOPropiedad propiedad = alohandes.adicionarPropiedad(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion);;
      			VOPropiedad apartamento = alohandes.adicionarApartamento(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);;
      			{
      				propiedad = alohandes.darPropiedadPorId(pId);
      				apartamento = alohandes.darApartamentoPorId(pId);
      				error = true;
      			}
      			List <VOApartamento> lista = alohandes.darVOApartamento();
      			long eliminados = alohandes.eliminarApartamentoPorId(pId);
      			alohandes.eliminarPropiedadPorId(pId);
      			
      			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
      			String resultado = "Demo de creación y listado de Apartamentos\n\n";
      			resultado += "\n\n************ Generando datos de prueba ************ \n";
      			if (error)
      			{
      				resultado += "*** Exception creando apartamento !!\n";
      				resultado += "*** Es probable que ese apartamento ya existiera\n";
      				resultado += "*** Revise el log de Alohandes para más detalles\n";
      			}
      			resultado += "Adicionado la habitacion con id: " + pId + "\n";
      			resultado += "\n\n************ Ejecutando la demo ************ \n";
      			resultado +=  "\n" + listarApartamento(lista);
      			resultado += "\n\n************ Limpiando la base de datos ************ \n";
      			resultado += eliminados + " apartamentos eliminados\n";
      			resultado += "\n Demo terminada";
         
      			panelDatos.actualizarInterfaz(resultado);
      		} 
          	catch (Exception e) 
          	{
//      			e.printStackTrace();
      			String resultado = generarMensajeError(e);
      			panelDatos.actualizarInterfaz(resultado);
      		}
          }

          /* ****************************************************************
        	 * 			Demos de Servicio
        	 *****************************************************************/
            /**
             * Demostración de creación, consulta y borrado de servicio
             * Muestra la traza de la ejecución en el panelDatos
             * 
             * Pre: La base de datos está vacía
             * Post: La base de datos está vacía
             */
            @SuppressWarnings("deprecation")
        	public void demoServicio( )
            {
            	try 
            	{
            		// Ejecución de la demo y recolección de los resultados
        			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
        			int pIntervalo = 30;
        			double pPrecio = 150000;
        			int pId = 001;
        			String pDireccion = ""; 
        			String pTipo ="";
        			
        			boolean error = false;
        			VOServicio propiedad = alohandes.adicionarServicio(pId, pTipo, pPrecio, pIntervalo);
        			{
        				propiedad = alohandes.darServicioPorId(pId);
        				error = true;
        			}
        			List <VOServicio> lista = alohandes.darVOServicio();
        			long eliminados = alohandes.eliminarServicioPorId(pId);
        			
        			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
        			String resultado = "Demo de creación y listado de Servicios\n\n";
        			resultado += "\n\n************ Generando datos de prueba ************ \n";
        			if (error)
        			{
        				resultado += "*** Exception creando servicios !!\n";
        				resultado += "*** Es probable que ese servicio ya existiera\n";
        				resultado += "*** Revise el log de Alohandes para más detalles\n";
        			}
        			resultado += "Adicionado el servicio con id: " + pId + "\n";
        			resultado += "\n\n************ Ejecutando la demo ************ \n";
        			resultado +=  "\n" + listarServicio(lista);
        			resultado += "\n\n************ Limpiando la base de datos ************ \n";
        			resultado += eliminados + " servicios eliminados\n";
        			resultado += "\n Demo terminada";
           
        			panelDatos.actualizarInterfaz(resultado);
        		} 
            	catch (Exception e) 
            	{
//        			e.printStackTrace();
        			String resultado = generarMensajeError(e);
        			panelDatos.actualizarInterfaz(resultado);
        		}
            }
            
            /* ****************************************************************
        	 * 			Demos de Reserva
        	 *****************************************************************/
            /**
             * Demostración de creación, consulta y borrado de reservas
             * Muestra la traza de la ejecución en el panelDatos
             * 
             * Pre: La base de datos está vacía
             * Post: La base de datos está vacía
             */
            @SuppressWarnings("deprecation")
        	public void demoReserva( )
            {
            	try 
            	{
            		// Ejecución de la demo y recolección de los resultados
        			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
        			int personas = 5;
        			double porcentajeAPagar = 60;
        			double montoTotal = 60000;
        			Date fechaInicio = new Date(2020, 05, 12);
        			Date fechaFin = new Date(2020, 05, 20);
        			Date finCancelacionOportuna = new Date(2020, 05, 10);
        			long idPropiedad = 1234567890;
        			
        			boolean error = false;
        			VOReserva reserva = alohandes.adicionarReserva(fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal, idPropiedad);
        			{
        				reserva = alohandes.darReservasPorId(reserva.getId());
        				error = true;
        			}
        			List <VOReserva> lista = alohandes.darVOReservas();
        			long eliminados = alohandes.eliminarReservasPorId(reserva.getId());
        			
        			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
        			String resultado = "Demo de creación y listado de Reservas\n\n";
        			resultado += "\n\n************ Generando datos de prueba ************ \n";
        			if (error)
        			{
        				resultado += "*** Exception creando reserva !!\n";
        				resultado += "*** Es probable que esa reserva ya existiera\n";
        				resultado += "*** Revise el log de Alohandes para más detalles\n";
        			}
        			resultado += "Adicionado la reserva con id: " + reserva.getId() + "\n";
        			resultado += "\n\n************ Ejecutando la demo ************ \n";
        			resultado +=  "\n" + listarReserva(lista);
        			resultado += "\n\n************ Limpiando la base de datos ************ \n";
        			resultado += eliminados + " RESERVAS eliminadas\n";
        			resultado += "\n Demo terminada";
           
        			panelDatos.actualizarInterfaz(resultado);
        		} 
            	catch (Exception e) 
            	{
//        			e.printStackTrace();
        			String resultado = generarMensajeError(e);
        			panelDatos.actualizarInterfaz(resultado);
        		}
            }
            
            /* ****************************************************************
        	 * 			Demos de ReservaColectiva
        	 *****************************************************************/
            /**
             * Demostración de creación, consulta y borrado de reservas colectivas
             * Muestra la traza de la ejecución en el panelDatos
             * 
             * Pre: La base de datos está vacía
             * Post: La base de datos está vacía
             */
            @SuppressWarnings("deprecation")
        	public void demoReservaColectiva( )
            {
            	try 
            	{
            		// Ejecución de la demo y recolección de los resultados
        			// ATENCIÓN: En una aplicación real, los datos JAMÁS están en el código
        			int pDuracion = 5;
        			int pCantidad = 4;
        			int pId = 001;
        			Date pInicio = new Date(2025, 04, 12);
        			String pTipo ="";
        			
        			boolean error = false;
        			VOReservaColectiva reserva = alohandes.adicionarReserva(pId, pCantidad, pTipo, pInicio, pDuracion);
        			{
        				reserva = alohandes.darReservaColectivaPorId(pId);
        				error = true;
        			}
        			List <VOReservaColectiva> lista = alohandes.darVOReservaColectiva();
        			long eliminados = alohandes.eliminarReservaColectivaPorId(pId);
        			
        			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
        			String resultado = "Demo de creación y listado de Reservas\n\n";
        			resultado += "\n\n************ Generando datos de prueba ************ \n";
        			if (error)
        			{
        				resultado += "*** Exception creando reserva !!\n";
        				resultado += "*** Es probable que esa reserva ya existiera\n";
        				resultado += "*** Revise el log de Alohandes para más detalles\n";
        			}
        			resultado += "Adicionado la reserva con id: " + pId + "\n";
        			resultado += "\n\n************ Ejecutando la demo ************ \n";
        			resultado +=  "\n" + listarReservaColectiva(lista);
        			resultado += "\n\n************ Limpiando la base de datos ************ \n";
        			resultado += eliminados + " RESERVAS eliminadas\n";
        			resultado += "\n Demo terminada";
           
        			panelDatos.actualizarInterfaz(resultado);
        		} 
            	catch (Exception e) 
            	{
//        			e.printStackTrace();
        			String resultado = generarMensajeError(e);
        			panelDatos.actualizarInterfaz(resultado);
        		}
            }
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Alohandes
	 */
	public void mostrarLogAlohandes ()
	{
		mostrarArchivo ("alohandes.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de alohandes
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogAlohandes ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("alohandes.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de alohandes ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de Alohandes
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = alohandes.limpiarAlohandes();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Reserva Habitacion eliminados\n";
			resultado += eliminados [1] + " Reserva Apartamento eliminados\n";
			resultado += eliminados [2] + " Reserva eliminados\n";
			resultado += eliminados [3] + " Reserva Colectiva eliminadas\n";
			resultado += eliminados [4] + " Servicios eliminados\n";
			resultado += eliminados [5] + " Apartamentos eliminados\n";
			resultado += eliminados [6] + " Habitaciones eliminados\n";
            resultado += eliminados [7] + " Propiedades eliminados\n";
            resultado += eliminados [8] + " Clientes eliminados\n";
            resultado += eliminados [8] + " Operadores eliminados\n";
            resultado += eliminados [8] + " Usuarios eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-AlohandesJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Alohandes.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Alohandes.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
    /**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Alohandes Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los clientes recibidos: una línea por cada cliente.
     * @param lista - La lista con los clientes
     * @return La cadena con una líea para cada cliente recibido
     */
    private String listarCliente(List<VOCliente> lista) 
    {
    	String resp = "Los cliente existentes son:\n";
    	int i = 1;
        for (VOCliente tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los operadores recibidos: una línea por cada operador.
     * @param lista - La lista con los operadores.
     * @return La cadena con una líea para cada operador recibido
     */
    private String listarOperador(List<VOOperador> lista) 
    {
    	String resp = "Los  operadores existentes son:\n";
    	int i = 1;
        for (VOOperador tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de las recervas recibidas: una línea por cada reserva.
     * @param lista - La lista con las reservas.
     * @return La cadena con una líea para cada reserva recibido
     */
    private String listarReserva(List<VOReserva> lista) 
    {
    	String resp = "Las reservas existentes son:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los apartamentos recibidos: una línea por cada apartamento.
     * @param lista - La lista con los apartamentos.
     * @return La cadena con una líea para cada apartamento recibido
     */
    private String listarApartamento(List<VOApartamento> lista) 
    {
    	String resp = "Los apartamentos existentes son:\n";
    	int i = 1;
        for (VOApartamento tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la lista de las habitaciones recibidos: una línea por cada habitacion.
     * @param lista - La lista con las habitaciones.
     * @return La cadena con una líea para cada habitacion recibido
     */
    private String listarHabitacion(List<VOHabitacion> lista) 
    {
    	String resp = "Las habitaciones existentes son:\n";
    	int i = 1;
        for (VOHabitacion tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de las reservas colectivas recibidos: una línea por cada reserva colectivas.
     * @param lista - La lista con las reservas colectivas.
     * @return La cadena con una líea para cada reserva colectivas recibido
     */
    private String listarReservaColectiva(List<VOReservaColectiva> lista) 
    {
    	String resp = "Las reservas colectivas existentes son:\n";
    	int i = 1;
        for (VOReservaColectiva tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la lista de los servicios recibidos: una línea por cada servicio.
     * @param lista - La lista con los servicios.
     * @return La cadena con una líea para cada servicio recibido
     */
    private String listarServicio(List<VOServicio> lista) 
    {
    	String resp = "Los servicios existentes son:\n";
    	int i = 1;
        for (VOServicio tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y aloahandes.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohandesDemo.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohandesDemo interfaz = new InterfazAlohandesDemo( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
