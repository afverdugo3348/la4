import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class Cliente 
{

	private static String state;
	private final static int PUERTO = 8083;

	public static void main(String[] args)
	{

		state = "";
		try
		{
			String sentence;
			String modifiedSentence;
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			Socket clientSocket = new Socket(InetAddress.getLocalHost(), PUERTO);
			System.out.println("Connected to server: "+ clientSocket.getLocalSocketAddress());
			if(clientSocket.isConnected())
			{
				state = "Connected";
			}
			else
			{
				if(clientSocket.isClosed())
					state = "Closed";
				else
					state = "No connected";
			}

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			System.out.println();
			System.out.println("Envie un mensaje de saludo para iniciar la comunicación");
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			System.out.println("Estado de la conexión: "+state );

			System.out.println();
			System.out.println();
			System.out.println("Seleccione el archivo que desea recibir:");
			System.out.println("1. Imagen");
			System.out.println("2. Archivo.pdf");
			System.out.println("3. Archivo.txt");

			int opcion = Integer.parseInt(inFromUser.readLine());
			outToServer.writeBytes((opcion+"") + '\n');
			
			if(opcion == 1)
			{

				String nombreArchivo = "./enviado.jpg";
			}
			else if(opcion == 2)
			{

				String nombreArchivo = "./enviado.pdf";
			}
			else if(opcion == 3)
			{
				String nombreArchivo = "./enviado.txt";
			}
			
			  DataOutputStream output;
		        BufferedInputStream bis;
		        BufferedOutputStream bos;

		        byte[] receivedData;
		        int in1;
		        String file;
		        System.out.println("El servido dice: "+inFromServer.readLine());
		        outToServer.writeBytes("Envie el archivo"+ '\n');
        		long startTime = Long.parseLong(inFromServer.readLine());

		        
		                receivedData = new byte[1024];
		                bis = new BufferedInputStream(clientSocket.getInputStream());
		                DataInputStream dis=new DataInputStream(clientSocket.getInputStream());
		            
		                //recibimos el nombre del fichero
		                file = dis.readUTF();
		                file = file.substring(file.indexOf('/')+1,file.length());

		                bos = new BufferedOutputStream(new FileOutputStream(file));
		                while ((in1 = bis.read(receivedData)) != -1)
		                {
		                    bos.write(receivedData,0,in1);
		                }

                		long endTime =  java.lang.System.currentTimeMillis();
                		
                		long totalTime = endTime - startTime;
                		System.out.println("Tiempo total de transferencia = " + totalTime + "ms");
                	
		                bos.close();
		                dis.close();         
		            
	        


			System.out.println("listo");

			clientSocket.close(); 
		}
		catch(Exception e)
		{
			System.out.println("Epic fail: "+e.getMessage());
		}
	}

}
