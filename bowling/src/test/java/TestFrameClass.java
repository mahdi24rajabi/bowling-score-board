import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class TestFrameClass {

    Logger logger = Logger.getLogger("Bowling Logger .");

    Frame prevFrame;
    Frame frame;

    @BeforeEach
    public void beforeEachTest(TestInfo testInfo){
        frame = new Frame(0, prevFrame);

        logger.info(String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @Test
    @DisplayName("Test WAIT state of Frame")
    public void testWaitFrame() {
        assertAll("WAITING",
                () -> assertEquals(10, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.WAITING, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertTrue(frame.isPlayable())
        );
    }

    @Test
    @DisplayName("Test Playing state of Frame")
    public void testPlayingFrame() throws InvalidKnockedBallsException {
        frame.throwBall(new Ball(4));
        assertAll("Playing",
                () -> assertEquals(6, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.PLAYING, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertTrue(frame.isPlayable())
        );

        assertThrows(InvalidKnockedBallsException.class, ()->frame.throwBall(new Ball(8)));
    }

    @Test
    @DisplayName("Test Played state of Frame")
    public void testPlayedFrame() throws InvalidKnockedBallsException {
        Ball ball = new Ball(3);
        frame.throwBall(ball);

        assertAll("Playing",
                () -> assertEquals(7, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.PLAYING, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertTrue(frame.isPlayable())
        );

        assertThrows(InvalidKnockedBallsException.class, ()->frame.throwBall(new Ball(8)));

        frame.throwBall(new Ball(6));

        assertAll("Played",
                () -> assertEquals(1, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.PLAYED, frame.getFrameStatus()),
                () -> assertTrue(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        frame.calculateScore();

        assertEquals(9, frame.getScore());
    }

    @Test
    @DisplayName("Test Strike state of Frame")
    public void testStrikeFrame() throws InvalidKnockedBallsException {
        Ball ball = new Ball(10);
        frame.throwBall(ball);

        assertAll("STRIKE",
                () -> assertEquals(0, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.STRIKE, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        assertThrows(InvalidKnockedBallsException.class, ()->frame.throwBall(new Ball(7)));

        frame.deliverBonus(new Ball(9));

        assertAll("FIRST BONUS",
                () -> assertEquals(0, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.STRIKE, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        frame.deliverBonus(new Ball(9));

        assertAll("SECOND BONUS",
                () -> assertEquals(0, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.STRIKE_BONUS_RECEIVED, frame.getFrameStatus()),
                () -> assertTrue(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        frame.calculateScore();

        assertEquals(28, frame.getScore());
    }


    @Test
    @DisplayName("Test Spare state of Frame")
    public void testSpareFrame() throws InvalidKnockedBallsException {
        Ball ball = new Ball(4);
        frame.throwBall(ball);

        assertAll("PLAYING",
                () -> assertEquals(6, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.PLAYING, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertTrue(frame.isPlayable())
        );

        assertThrows(InvalidKnockedBallsException.class, ()->frame.throwBall(new Ball(7)));

        frame.throwBall(new Ball(6));

        assertAll("SPARE",
                () -> assertEquals(0, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.SPARE, frame.getFrameStatus()),
                () -> assertFalse(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        frame.deliverBonus(new Ball(3));

        assertAll("FIRST BONUS",
                () -> assertEquals(0, frame.getAvailablePins()),
                () -> assertEquals(Frame.FrameStatus.SPARE_BONUS_RECEIVED, frame.getFrameStatus()),
                () -> assertTrue(frame.isCalculable()),
                () -> assertFalse(frame.isPlayable())
        );

        frame.calculateScore();

        assertEquals(13, frame.getScore());
    }
}
