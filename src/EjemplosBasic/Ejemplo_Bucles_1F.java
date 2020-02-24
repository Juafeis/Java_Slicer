package EjemplosBasic;

public class Ejemplo_Bucles_1F {

    public static void main(String[] args) {
        System.out.println("Empieza bucle WHILE:");
        int x = 1;
        if (x <= 10) {
            Object[] result = method1(x);
            x = (Integer) result[0];
        }
        System.out.println();
    }

    static Object[] method1(int x) {
        {
            System.out.print(" " + x);
            x++;
        }
        throw new RuntimeException("The compiler doesn't know that this statement is unreachable");
    }
}
