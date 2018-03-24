import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPclient2 
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
			System.out.println("Número de objetos a generar y enviar:");
			numObjetosEnviar = Integer.parseInt(inFromUser.readLine());
			System.out.println();


			InetAddress IPAddress = InetAddress.getByName(direccionIP);


			for (int i = 0; i < numObjetosEnviar; i++) 
			{
				byte[] sendData = new byte[1024];
				//byte[] receiveData = new byte[1024];
				Date date  = new Date();
				DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String marcaTiempo = hourdateFormat.format(date);
				String num =""+(i+1);
				ObjetoEnviar objetoEnviar = new ObjetoEnviar((i+1), marcaTiempo);
				String objetoString = num+"!"+numObjetosEnviar+"!"+marcaTiempo.toString();
				sendData = toByteArray(objetoString.trim());
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puerto);
				clientSocket.send(sendPacket);
				/**
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());
				System.out.println("FROM SERVER:" + modifiedSentence);*/
			}

			System.out.println();
			System.out.println("Se han enviado todos los objetos");
			System.out.println();

			// AJUSTAR EL TAMAÑO DEL BUFFER DE ENVIO Y DE RECEPCION
			
			boolean salir = false;
			while(!salir)
			{
				System.out.println("Presione 1 para ajustar el tamaño del buffer de envío");
				System.out.println("Presione 2 para ajustar el tamaño del buffer de recepción");
				System.out.println("Presione 3 para recibir un archivo por parte del servidor");
				System.out.println("Presione 4 para terminar");
				
				int o = Integer.parseInt(inFromUser.readLine());
				if(o == 3)
				{
					// RECIBIR EL ARCHIVO Y ALMACENARLO EN DISCO
					byte[] buf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					clientSocket.receive(packet);
					//APLICAR HASH
					
					// reportar si el archivo está completo y correcto y el tiempo total de transferencia
					
					//FALTA MEDIR EL TIEMPO DE TRANSFERENCIA
					
					buf = packet.getData();
					FileOutputStream fos = new FileOutputStream(new File("./data/archivoRecibido.txt"));
					fos.write(buf);
					fos.close();
					
					System.out.println();
					System.out.println("Archivo recibido con exito, busque en la carpeta data");
					System.out.println();
				}
				else if(o == 2)
				{
					System.out.println("Digte el nuevo tamaño del buffer de recepción");
					int newSize = inFromUser.read();
					clientSocket.setReceiveBufferSize(newSize);
				}
				else if(o == 1)
				{
					System.out.println("Digte el nuevo tamaño del buffer de envío");
					int newSize = inFromUser.read();
					clientSocket.setSendBufferSize(newSize);
				}
				else if(o == 4)
				{
					break;
				}
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
