import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPclient 
{
	
	private static String direccionIP;
	private static int puerto;
	private static int numObjetosEnviar;

	public static void main(String[] args) 
	{
		try
		{	
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DatagramSocket clientSocket = new DatagramSocket();
			
			System.out.println("----------- UDP CLIENT ------------");
			System.out.println();
			System.out.println("IP del servidor:");
			direccionIP = inFromUser.readLine();
			System.out.println();
			System.out.println("Puerto:");
			puerto = Integer.parseInt(inFromUser.readLine());
			System.out.println();
			System.out.println("N�mero de objetos a generar y enviar:");
			numObjetosEnviar = Integer.parseInt(inFromUser.readLine());
			System.out.println();
			

			InetAddress IPAddress = InetAddress.getByName(direccionIP);
			
			
			for (int i = 0; i < numObjetosEnviar; i++) 
			{
				byte[] sendData = new byte[1024];
				byte[] receiveData = new byte[1024];
				Date date  = new Date();
				DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String marcaTiempo = hourdateFormat.format(date);
				String num =""+(i+1);
				ObjetoEnviar objetoEnviar = new ObjetoEnviar((i+1), marcaTiempo);
				String objetoString = num+"!"+numObjetosEnviar+"!"+marcaTiempo.toString();
				sendData = toByteArray(objetoString.trim());
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puerto);
				clientSocket.send(sendPacket);
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("FROM SERVER:" + modifiedSentence);
				System.out.println();
			}
			
			clientSocket.close(); 
		}
		catch(Exception e)
		{
			System.out.println("Error " + e.getMessage());
		}
	}
	
	public static byte[] toByteArray(Object obj) throws IOException 
	 {
	        byte[] bytes = null;
	        ByteArrayOutputStream bos = null;
	        ObjectOutputStream oos = null;
	        try 
	        {
	            bos = new ByteArrayOutputStream();
	            oos = new ObjectOutputStream(bos);
	            oos.writeObject(obj);
	            oos.flush();
	            bytes = bos.toByteArray();
	        } 
	        finally 
	        {
	            if (oos != null) 
	            {
	                oos.close();
	            }
	            if (bos != null) 
	            {
	                bos.close();
	            }
	        }
	        return bytes;
	    }

}
