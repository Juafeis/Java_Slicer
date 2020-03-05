package Ejemplos.EjemplosChallenging;

public class Challenging_Bucles_7 {
	public static void main(String[] args)
	{
		int resultado = sumatorio(1,10);
		System.out.println(resultado);
	}
	
	public static int sumatorio(int x, int y)
	{
		// BUCLE WHILE (sin anidamiento)
		System.out.println("Empieza bucle WHILE:");
		bucle1:
		while (x<=10)
		{
			x++;
			bucle2:
			while (y<=10)
			{
				y--;
				break bucle1;    //  EL WHILE TIENE UN BREAK con una label
			}

			break;    //  EL WHILE TIENE UN BREAK sin label
		}
		return x;
	}	
}
