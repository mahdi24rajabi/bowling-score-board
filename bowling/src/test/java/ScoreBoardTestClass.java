import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScoreBoardTestClass {
    Logger logger = Logger.getLogger("Bowling Logger .");

    BowlingScoreBoardApp bowlingScoreBoardApp;

    @BeforeAll
    public void beforeEachTest(TestInfo testInfo) {
        bowlingScoreBoardApp = new BowlingScoreBoardApp();

        assertAll("Initial State of Board",
                () -> assertEquals(10, bowlingScoreBoardApp.getFrames().size()),
                () -> assertEquals(0, bowlingScoreBoardApp.getUnscoredFrames().size()),
                () -> assertEquals(new Integer(0), bowlingScoreBoardApp.getCurrentFrameIndex())
        );

        logger.info(String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }


    /**
     * Change @CsvFileSource to path of one of files inside test resource directory to test new game
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/boardTest17.csv"})
    public void testCaseOne(@CsvToGameRound GameRound gameRoundTestCase) throws InvalidKnockedBallsException {
        bowlingScoreBoardApp.throwBalls(gameRoundTestCase.getBall());

        for(int i = 0; i < 10; i ++) {
            int finalI = i;
            int finalI1 = i;
            assertAll("Ball ",
                    () -> assertEquals(bowlingScoreBoardApp.getFrames().get(finalI).getFrameStatus(), gameRoundTestCase.getStatusesAfterThrowing().get(finalI)),
                    () -> assertEquals(Integer.valueOf(bowlingScoreBoardApp.getFrames().get(finalI1).getScore()), gameRoundTestCase.getFramesScores().get(finalI1))
            );
        }
    }

    public static class BoardArgumentConverter implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            Ball ball = new Ball(argumentsAccessor.getInteger(0));

            List<Frame.FrameStatus> frameStatuses = new ArrayList<>();
            String[] states = argumentsAccessor.getString(1).split("\\s*,\\s*");

            for (String state : states) {
                frameStatuses.add(Frame.FrameStatus.valueOf(state));
            }

            String[] scores = argumentsAccessor.getString(2).split("\\s*,\\s*");
            List<Integer> frameScores = new ArrayList<>();

            for (String score : scores) {
                frameScores.add(Integer.valueOf(score));
            }

            return new GameRound(ball, frameStatuses, frameScores);
        }
    }

    public static class GameRound{
        private Ball ball;
        private List<Frame.FrameStatus> statusesAfterThrowing = new ArrayList<>();
        private List<Integer> framesScores =  new ArrayList<>();

        public GameRound(Ball ball, List<Frame.FrameStatus> statusesAfterThrowing, List<Integer> framesScores) {
            this.ball = ball;
            this.statusesAfterThrowing = statusesAfterThrowing;
            this.framesScores = framesScores;
        }

        public Ball getBall() {
            return ball;
        }

        public List<Frame.FrameStatus> getStatusesAfterThrowing() {
            return statusesAfterThrowing;
        }

        public List<Integer> getFramesScores() {
            return framesScores;
        }
    }

}
