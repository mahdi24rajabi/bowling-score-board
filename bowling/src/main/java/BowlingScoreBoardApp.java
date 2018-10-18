


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BowlingScoreBoardApp {

    static int NUMBER_OF_FRAMES = 10;

    private List<Frame> frames = new ArrayList<>();
    private Integer currentFrameIndex = 0;
    private List<Frame> unscoredFrames = new ArrayList<>();

    public BowlingScoreBoardApp() {
        for (int i = 0; i < NUMBER_OF_FRAMES; i++) {
            Frame prevFrame = null;

            if (i != 0) {
                prevFrame = frames.get(i - 1);
            }

            Frame frame = new Frame(i, prevFrame);
            frames.add(frame);
        }
    }

    public void startGame() {
        while (!frames.get(NUMBER_OF_FRAMES - 1).isCalculable()) {
            Frame currentFrame = frames.get(currentFrameIndex);

            int availablePins = currentFrame.getAvailablePins();
            if(currentFrameIndex == NUMBER_OF_FRAMES - 1 && !currentFrame.isCalculable()){
                availablePins = 10;
            }

            int knockedPins = Thrower.startThrowing(availablePins);

            Ball ball = new Ball(knockedPins);

            if(currentFrame.isPlayable()) {
                currentFrame.throwBall(ball);
            }

            if (!currentFrame.getFrameStatus().equals(Frame.FrameStatus.PLAYING)){
                if(currentFrameIndex < NUMBER_OF_FRAMES -1 ) {
                    currentFrameIndex++;
                }
            }

            Iterator<Frame> unscoredFrameIterator = unscoredFrames.iterator();
            while (unscoredFrameIterator.hasNext()) {
                Frame unscoredFrame = unscoredFrameIterator.next();
                unscoredFrame.deliverBonus(ball);
                if (!unscoredFrame.getFrameStatus().equals(Frame.FrameStatus.SPARE) &&
                        !unscoredFrame.getFrameStatus().equals(Frame.FrameStatus.STRIKE)
                ) {
                    unscoredFrameIterator.remove();
                }
            }

            if (currentFrame.getFrameStatus().equals(Frame.FrameStatus.SPARE) ||
                    currentFrame.getFrameStatus().equals(Frame.FrameStatus.STRIKE)) {
                unscoredFrames.add(currentFrame);
            }

            for (Frame frame : frames) {
                if (frame.isCalculable()) {
                    frame.calculateScore();
                }
                System.out.println(frame.toString());
            }

            System.out.println("\n\n");
        }

    }

    public static void main(String[] args) {
        System.out.println("*****************************************************************");
        System.out.println("*                                                               *");
        System.out.println("*                                                               *");
        System.out.println("*                                                               *");
        System.out.println("*               WELCOME TO BOWLING SCORE BOARD                  *");
        System.out.println("*                                                               *");
        System.out.println("*                                                               *");
        System.out.println("*                                                               *");
        System.out.println("*****************************************************************");
        System.out.println();
        new BowlingScoreBoardApp().startGame();
    }

}

