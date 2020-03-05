package Ejemplos.EjemplosAdvanced;

// Este código tiene un bucle vacío

public class Evaluation_Bucles_5 {

	
	public static void main(String[] args)
	{
		int x=10;
		if (x>1)
		{
			System.out.println("Empieza bucle WHILE:");	
			while (x>2)
			{
				System.out.println("Empieza bucle WHILE:");	
				while (x>3)
				{
					if (x>4)
					{
						while (x>100)
						{
						}
					}
					x--;
					System.out.print(" x: "+ x);	
				}
				x--;
			}
		}			
	}
}
