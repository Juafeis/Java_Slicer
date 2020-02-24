package EjemplosAdvanced;

public class Evaluation_Bucles_4 {

// Este código tiene un "foreach"
	
	public static void main(String[] args)
	{
		String[] fruits = new String[] { "Orange", "Apple", "Pear", "Strawberry" };
		String[] animals = new String[] { "Bear", "Monkey" };		
		int x=1;

		while (x<=3)
		{
			System.out.println(" "+x);

			for (String fruit : fruits) {
				System.out.println(fruit);
				for (String animal : animals) {
					System.out.println(animal);
				}
			}
			x++;
		}
		while (x<=6)
		{
			for (String animal : animals) {
				System.out.println(animal);
				x++;
			}
		}
		System.out.println();			
	}
}
