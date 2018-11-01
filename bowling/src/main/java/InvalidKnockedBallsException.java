
public class InvalidKnockedBallsException extends Exception {
    private int availablePins = 0;

    public InvalidKnockedBallsException(int availablePins) {
        super();
        this.availablePins = availablePins;
    }

    public int getAvailablePins() {
        return availablePins;
    }
}
