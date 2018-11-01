
import java.util.Scanner;

public class Thrower {

    public static int throwBall() {
        System.out.println("Throw ball : ");
        Scanner scanner = new Scanner(System.in);
        int knockedPins = scanner.nextInt();
        return knockedPins;
    }
}
