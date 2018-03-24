import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.io.*;

public class Servidor {


  public static void main (String args[]) throws ParseException 
  {
	  
	  System.out.println("------------- UDP SERVER ------------");

    try {
    	
   	Properties prop = new Properties();
   	OutputStream output = null; 	

   	int puerto = 6060;
	if(args.length > 0)
	{
		puerto = Integer.parseInt(args[0]);
	}
	
	DatagramSocket socketUDP = new DatagramSocket(puerto);
      byte[] bufer = new byte[1000];

      while (true) 
      {
        // Construimos el DatagramPacket para recibir peticiones
        DatagramPacket peticion =
          new DatagramPacket(bufer, bufer.length);

        // Leemos una petici�n del DatagramSocket
        socketUDP.receive(peticion);

        String nombreFile =  peticion.getAddress().toString();
        output = new FileOutputStream(nombreFile.substring(1, nombreFile.length())+".properties");   		
        
        System.out.print("Datagrama recibido del host: " +
                           peticion.getAddress());
        System.out.println(" desde el puerto remoto: " +
                           peticion.getPort());
        System.out.println(" de largo: "+peticion.getLength());
        String obj =  new String(peticion.getData()).substring(7, peticion.getLength());
        System.out.println(" los datos son: " + obj);
        System.out.println(obj);
        String num = obj.split("!")[0];
        String total = obj.split("!")[1];
        Date llegada = new Date();
        Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(obj.split("!")[2]);
                System.out.println("La resta es: " + (llegada.getTime()-date1.getTime()));

 		prop.setProperty(num, (llegada.getTime()-date1.getTime())+"" + " ms");

		// save properties to project root folder
		prop.store(output, null);
		
		//estadisticas
		String[] elems= new String[Integer.parseInt(total)];
        elems[Integer.parseInt(num)-1]= num;
        if(total.equals(num)) {
        	int contador = 0;
        	int contador1 = 0;
        	for(int i = 0; i< elems.length;i++) {
        		if(elems[i]==null) {
        			contador++;
        		}else {
        			contador1++;
        		}
        	}
        	System.out.println("N�mero de objetos recibidos: "+contador1);
        	System.out.println("N�mero de objetos perdidos: "+contador);
        	System.out.println();
        }
    	//Respuesta
        DatagramPacket respuesta =
          new DatagramPacket(peticion.getData(), peticion.getLength(),
                             peticion.getAddress(), peticion.getPort());

        // Enviamos la respuesta, que es un eco
        socketUDP.send(respuesta);
      }

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }

}