
import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int availablePins = 10;
    private int number;
    private int score = 0;
    private FrameStatus frameStatus = FrameStatus.WAITING;
    private List<Ball> playedBalls = new ArrayList<>();
    private List<Ball> bonusBalls = new ArrayList<>();
    private Frame previousFrame;

    public enum FrameStatus {
        PLAYING,
        PLAYED,
        SPARE,
        STRIKE,
        STRIKE_BONUS_RECEIVED,
        SPARE_BONUS_RECEIVED,
        WAITING
    }

    public Frame(int number, Frame previousFrame) {
        this.number = number;
        this.previousFrame = previousFrame;
    }

    public FrameStatus getFrameStatus() {
        return frameStatus;
    }

    public int getNumber() {
        return number;
    }

    public int getScore() {
        return score;
    }

    public int getAvailablePins() {
        return availablePins;
    }

    public void calculateScore() {
        score = previousFrame != null ? previousFrame.score : 0;

        for (Ball receiveBall : playedBalls) {
            score += receiveBall.getKnockedPins();
        }

        for (Ball receiveBall : bonusBalls) {
            score += receiveBall.getKnockedPins();
        }

    }

    public boolean isCalculable(){
        return frameStatus.equals(FrameStatus.PLAYED) || frameStatus.equals(FrameStatus.STRIKE_BONUS_RECEIVED) || frameStatus.equals(FrameStatus.SPARE_BONUS_RECEIVED);
    }

    public boolean isPlayable(){
        return frameStatus.equals(FrameStatus.PLAYING) || frameStatus.equals(FrameStatus.WAITING);
    }


    public void throwBall(Ball ball) throws InvalidKnockedBallsException {
        if(ball.getKnockedPins() > availablePins){
            throw new InvalidKnockedBallsException();
        }

        availablePins -= ball.getKnockedPins();
        playedBalls.add(ball);

        if (playedBalls.size() == 1 && availablePins == 0) {
            frameStatus = FrameStatus.STRIKE;
        } else if (playedBalls.size() == 2 && availablePins == 0) {
            frameStatus = FrameStatus.SPARE;
        } else if (playedBalls.size() == 2 && availablePins != 0) {
            frameStatus = FrameStatus.PLAYED;
        } else {
            frameStatus = FrameStatus.PLAYING;
        }

    }

    public void deliverBonus(Ball ball) {
        switch (frameStatus) {
            case STRIKE:
                if (bonusBalls.size() < 2) {
                    bonusBalls.add(ball);

                    if (bonusBalls.size() == 2) {
                        frameStatus = FrameStatus.STRIKE_BONUS_RECEIVED;
                    }
                }
                break;

            case SPARE:
                if (bonusBalls.size() < 1) {
                    bonusBalls.add(ball);

                    if (bonusBalls.size() == 1) {
                        frameStatus = FrameStatus.SPARE_BONUS_RECEIVED;
                    }
                }
                break;
        }
    }

    @Override
    public String toString() {
        return String.format(" ------------------------------------------------------------------------------------\n|" +
                "Number = %2s" +
                ", Available Pins = %2s" +
                ", Score = %4s" +
                ", Frame status = %21s" +
                "|", number, availablePins, score, frameStatus);
    }
}
