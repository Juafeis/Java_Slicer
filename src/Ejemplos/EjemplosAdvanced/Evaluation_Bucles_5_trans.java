package Ejemplos.EjemplosAdvanced;

public class Evaluation_Bucles_5_trans {

    public static void main(String[] args) {
        int x = 10;
        if (x > 1) {
            System.out.println("Empieza bucle WHILE:");
            if (x > 2) {
                Object[] result = method1(x);
                x = (Integer) result[0];
            }
        }
    }

    public static Object[] method1(int x) {
        {
            System.out.println("Empieza bucle WHILE:");
            if (x > 3) {
                Object[] result = method2(x);
                x = (Integer) result[0];
            }
            x--;
        }
        if (x > 2) {
            return method1(x);
        }
        return new Object[] { x };
    }

    public static Object[] method2(int x) {
        {
            if (x > 4) {
                if (x > 100) {
                    Object[] result = method3(x);
                    x = (Integer) result[0];
                }
            }
            x--;
            System.out.print(" x: " + x);
        }
        if (x > 3) {
            return method2(x);
        }
        return new Object[] { x };
    }

    public static Object[] method3(int x) {
        {
        }
        if (x > 100) {
            return method3(x);
        }
        return new Object[] { x };
    }
}
