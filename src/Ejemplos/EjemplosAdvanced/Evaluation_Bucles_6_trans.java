package Ejemplos.EjemplosAdvanced;

public class Evaluation_Bucles_6_trans {

    public static void main(String[] args) {
        metodo();
        Clase2 clase2 = new Clase2();
        clase2.main();
    }

    public static void metodo() {
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

class Clase2 {

    public void main() {
        System.out.println("Empieza bucle WHILE:");
        int x = 1;
        if (x <= 10) {
            Object[] result = method2(x);
            x = (Integer) result[0];
        }
        System.out.println();
        metodo();
    }

    public void metodo() {
        System.out.println("Empieza bucle WHILE:");
        int x = 1;
        if (x <= 10) {
            Object[] result = method3(x);
            x = (Integer) result[0];
        }
        System.out.println();
    }

    public Object[] method2(int x) {
        {
            System.out.print(" " + x);
            x++;
        }
        if (x <= 10) {
            return method2(x);
        }
        return new Object[] { x };
    }

    public Object[] method3(int x) {
        {
            System.out.print(" " + x);
            x++;
        }
        if (x <= 10) {
            return method3(x);
        }
        return new Object[] { x };
    }
}
