package Ejemplos.EjemplosBasic;

public class Ejemplo_Bucles_2_trans {

    public static void main(String[] args) {
        System.out.println("Empieza bucle WHILE anidado a otro WHILE:");
        int x = 1;
        char y = 'a';
        if (x <= 10) {
            Object[] result = method1(x, y);
            x = (Integer) result[0];
        }
        System.out.println();
    }

    public static Object[] method1(int x, char y) {
        {
            System.out.print(" " + x);
            y = 'a';
            if (y <= 'c') {
                Object[] result = method2(y);
                y = (Character) result[0];
            }
            x++;
        }
        if (x <= 10) {
            return method1(x, y);
        }
        return new Object[] { x, y };
    }

    public static Object[] method2(char y) {
        {
            System.out.print(" " + y);
            y++;
        }
        if (y <= 'c') {
            return method2(y);
        }
        return new Object[] { y };
    }
}
