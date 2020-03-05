package Ejemplos.EjemplosBasic;

public class Ejemplo_Bucles_1_trans {

    public static void main(String[] args) {
        System.out.println("Empieza bucle WHILE:");
        int x = 1;
        if (x <= 10) {
            Object[] result = method1(x);
            x = (Integer) result[0];
        }
        System.out.println();
    }

    public static Object[] method1(int x) {
        {
            System.out.print(" " + x);
            x++;
        }
        if (x <= 10) {
            return method1(x);
        }
        return new Object[] { x };
    }
}
