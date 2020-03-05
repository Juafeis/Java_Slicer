package Ejemplos.EjemplosChallenging;

public class Challenging_Bucles_5_trans {

    public static void main(String[] args) throws Exception {
        int resultado = sumatorio(1);
        System.out.println(resultado);
    }

    public static int sumatorio(int x) throws Exception {
        System.out.println("Empieza bucle WHILE:");
        if (x <= 10) {
            Object[] result = method1(x);
            x = (Integer) result[0];
        }
        return x;
    }

    public static Object[] method1(int x) {
        {
            x++;
            if (x < 10) continue;
            x++;
        }
        if (x <= 10) {
            return method1(x);
        }
        return new Object[] { x };
    }
}
