package Ejemplos.EjemplosChallenging;

public class Challenging_Bucles_2 {
	public static void main(String[] args)
	{
		int resultado = sumatorio(1);
		System.out.println(resultado);
	}
	
	public static int sumatorio(int x)
	{
		// BUCLE WHILE (sin anidamiento)
		System.out.println("Empieza bucle WHILE:");		
		while (x<=10)
		{
			x++;
			break;    //  EL WHILE TIENE UN BREAK
		}
		return x;
	}	
}
