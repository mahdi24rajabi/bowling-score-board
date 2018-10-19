
import java.util.Scanner;

public class Thrower {

    public static int startThrowing(int availablePins) {
        System.out.println("Throw ball : ");
        Scanner scanner = new Scanner(System.in);
        int knockedPins = scanner.nextInt();

        while (knockedPins > availablePins) {
            System.out.println("Invalid Number. Available balls: "+ availablePins +"\nThrow Again: ");
            knockedPins = scanner.nextInt();
        }
        scanner.close();
        return knockedPins;
    }
}
