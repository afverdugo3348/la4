import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjetoEnviar implements Serializable
{

	private int numSecuencia;
	private String marcaDeTiempo;
	
	public ObjetoEnviar(int pNumSecuencia, String pMarcaDeTiempo) 
	{
		numSecuencia = pNumSecuencia;
		marcaDeTiempo = pMarcaDeTiempo;
		System.out.println("Objeto generado para enviar");
		System.out.println("numero de secuencia: " + numSecuencia);
		System.out.println("marca de tiempo: " + marcaDeTiempo);
		System.out.println();
	}

}
