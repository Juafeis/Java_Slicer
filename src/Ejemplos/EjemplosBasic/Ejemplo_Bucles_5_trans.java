package Ejemplos.EjemplosBasic;

public class Ejemplo_Bucles_5_trans {

    public static void main(String[] args) {
        int x = 0;
        char y = '0';
        System.out.println("Empieza bucle FOR anidado a otro FOR:");
        for (x = 1; x <= 10; x++) {
            System.out.print(" " + x);
            for (y = 'a'; y <= 'c'; y++) {
                System.out.print(" " + y);
            }
        }
        System.out.println();
        System.out.println("Empieza bucle WHILE anidado a otro WHILE:");
        x = 1;
        if (x <= 10) {
            Object[] result = method1(x, y);
            x = (Integer) result[0];
        }
        System.out.println();
        System.out.println("Empieza bucle FOR anidado a bucle DO WHILE:");
        x = 1;
        {
            {
                System.out.print(" " + x);
                for (y = 'a'; y <= 'c'; y++) {
                    System.out.print(" " + y);
                }
                x++;
            }
            if (x <= 10) {
                Object[] result = method2(x, y);
                x = (Integer) result[0];
            }
        }
        System.out.println();
    }

    public static Object[] method1(int x, char y) {
        {
            System.out.print(" " + x);
            y = 'a';
            if (y <= 'c') {
                Object[] result = method3(y);
                y = (Character) result[0];
            }
            x++;
        }
        if (x <= 10) {
            return method1(x, y);
        }
        return new Object[] { x, y };
    }

    public static Object[] method2(int x, char y) {
        {
            System.out.print(" " + x);
            for (y = 'a'; y <= 'c'; y++) {
                System.out.print(" " + y);
            }
            x++;
        }
        if (x <= 10) {
            return method2(x, y);
        }
        return new Object[] { x, y };
    }

    public static Object[] method3(char y) {
        {
            System.out.print(" " + y);
            y++;
        }
        if (y <= 'c') {
            return method3(y);
        }
        return new Object[] { y };
    }
}
