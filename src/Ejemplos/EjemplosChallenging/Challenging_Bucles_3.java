package Ejemplos.EjemplosChallenging;

public class Challenging_Bucles_3 {
	public static void main(String[] args) throws Exception
	{
		int resultado = sumatorio(1);
		System.out.println(resultado);
	}
	
	public static int sumatorio(int x) throws Exception
	{
		// BUCLE WHILE (sin anidamiento)
		System.out.println("Empieza bucle WHILE:");		
		while (x<=10)
		{
			x++; 
			continue;    //  EL WHILE TIENE UN CONTINUE (QUE NO HACE NADA)
		}
		return x;
	}	
}
