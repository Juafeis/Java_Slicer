package Ejemplos.EjemplosBasic;

public class Ejemplo_Bucles_4_trans {

    public static void main(String[] args) {
        int x = 1;
        if (x < 10) {
            Object[] result = method1(x);
            x = (Integer) result[0];
        }
        int suma = 0;
        int y = 1;
        if (y < 10) {
            Object[] result = method2(suma, y);
            suma = (Integer) result[0];
        }
        System.out.println(suma);
        int sumatorio = 0;
        int min = 10;
        int max = 100;
        for (int num = min; num <= max; num++) {
            sumatorio += num;
        }
        System.out.println(sumatorio);
        int count = 0;
        if (count < 10) {
            Object[] result = method3(count);
            count = (Integer) result[0];
        }
        System.out.println(count);
    }

    public static Object[] method1(int x) {
        {
            System.out.println(x);
            x++;
        }
        if (x < 10) {
            return method1(x);
        }
        return new Object[] { x };
    }

    public static Object[] method2(int suma, int y) {
        {
            suma += y;
            y++;
        }
        if (y < 10) {
            return method2(suma, y);
        }
        return new Object[] { suma, y };
    }

    public static Object[] method3(int count) {
        count++;
        if (count < 10) {
            return method3(count);
        }
        return new Object[] { count };
    }
}
