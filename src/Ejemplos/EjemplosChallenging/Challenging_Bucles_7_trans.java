package Ejemplos.EjemplosChallenging;

public class Challenging_Bucles_7_trans {

    public static void main(String[] args) {
        int resultado = sumatorio(1, 10);
        System.out.println(resultado);
    }

    public static int sumatorio(int x, int y) {
        System.out.println("Empieza bucle WHILE:");
        bucle1: if (x <= 10) {
            Object[] result = method1(x, y);
            x = (Integer) result[0];
        }
        return x;
    }

    public static Object[] method1(int x, int y) {
        {
            x++;
            bucle2: if (y <= 10) {
                Object[] result = method2(y);
                y = (Integer) result[0];
            }
            break;
        }
        if (x <= 10) {
            return method1(x, y);
        }
        return new Object[] { x, y };
    }

    public static Object[] method2(int y) {
        {
            y--;
            break bucle1;
        }
        if (y <= 10) {
            return method2(y);
        }
        return new Object[] { y };
    }
}
