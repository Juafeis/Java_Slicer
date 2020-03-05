package Ejemplos.EjemplosBasic;

public class Ejemplo_Bucles_3_trans {

    public static void main(String[] args) {
        int x;
        System.out.println("Empieza bucle FOR:");
        for (x = 1; x <= 10; x++) {
            System.out.print(" " + x);
        }
        System.out.println();
        System.out.println("Empieza bucle WHILE:");
        x = 1;
        if (x <= 10) {
            Object[] result = method1(x);
            x = (Integer) result[0];
        }
        System.out.println();
        System.out.println("Empieza bucle DO WHILE:");
        x = 1;
        {
            {
                System.out.print(" " + x);
                x++;
            }
            if (x <= 10) {
                Object[] result = method2(x);
                x = (Integer) result[0];
            }
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

    public static Object[] method2(int x) {
        {
            System.out.print(" " + x);
            x++;
        }
        if (x <= 10) {
            return method2(x);
        }
        return new Object[] { x };
    }
}
