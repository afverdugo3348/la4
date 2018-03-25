import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	private static ServerSocket server;

	private static void listen() throws Exception 
	{
		String data = null;
		Socket client = server.accept();
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("\r\nNew connection from " + clientAddress);

		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));  

		if( (data = in.readLine()) != null ) 
		{
			System.out.println("\r\nMessage from " + clientAddress + ": " + data);
		}

		DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
		String answer = "Mensaje recibido";
		System.out.println("\r\nAnswer to "+ clientAddress + ": " + answer);
		outToClient.writeBytes(answer+ '\n');



		if( (data = in.readLine()) != null ) 
		{
			if(data.equals("1"))
				data = "Imagen.jpg";
			else if(data.equals("2"))
				data = "Archivo.pdf";
			else
				data = "Archivo.txt";

			System.out.println("\r\nMessage from " + clientAddress + ": " + data);
		}
		
		DataInputStream input;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        int in1;
        byte[] byteArray;
		outToClient.writeBytes("Recibido"+ '\n');
        System.out.println("El cliente dice: " + in.readLine());
        long timeStart = java.lang.System.currentTimeMillis();
        outToClient.writeBytes(timeStart+""+ '\n');
        
        final String filename = "./"+data;

 
            final File localFile = new File( filename );
            bis = new BufferedInputStream(new FileInputStream(localFile));
            bos = new BufferedOutputStream(client.getOutputStream());

            //enviamos el nombre del archivo            
            DataOutputStream dos=new DataOutputStream(client.getOutputStream());
            dos.writeUTF(localFile.getName());

            byteArray = new byte[8192];
            
            int cont = 0;
            while ((in1 = bis.read(byteArray)) != -1)
            {
                bos.write(byteArray,0,in1);
            }
            

            bis.close();
            bos.close();



	      
		
         System.out.println("enviado");
		//outToClient.writeBytes("Archivo enviado"+ '\n');
	}

	public static InetAddress getSocketAddress() 
	{
		return server.getInetAddress();
	}

	public static int getPort() 
	{
		return server.getLocalPort();
	}
	public static void main(String[] args) throws Exception 
	{
		server = new ServerSocket(8083); //LOCALHOST
		//server = new ServerSocket(0, 1, InetAddress.getByName("ipAddress"));

		System.out.println("\r\nRunning Server: " + 
				"Host=" + getSocketAddress().getHostAddress() + 
				" Port=" + getPort());

		listen();
	}
}
