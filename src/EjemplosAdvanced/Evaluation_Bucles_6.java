package EjemplosAdvanced;

//Este código tiene varias clases

public class Evaluation_Bucles_6 {
	
	public static void main(String[] args)
	{
		metodo();
		
		Clase2 clase2 = new Clase2();
		clase2.main();
	}
	
	public static void metodo()
	{
		System.out.println("Empieza bucle WHILE:");
		int x=1;
		while (x<=10)
		{
			System.out.print(" "+x);
			x++;
		}
		System.out.println();	
	}
}

class Clase2 {
	
	public void main()
	{
		System.out.println("Empieza bucle WHILE:");
		int x=1;
		while (x<=10)
		{
			System.out.print(" "+x);
			x++;
		}
		System.out.println();	

		metodo();			
	}
	
	public void metodo()
	{
		System.out.println("Empieza bucle WHILE:");
		int x=1;
		while (x<=10)
		{
			System.out.print(" "+x);
			x++;
		}
		System.out.println();	
	}
}