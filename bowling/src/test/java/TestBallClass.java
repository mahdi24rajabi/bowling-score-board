import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBallClass {

    @Test
    @DisplayName("Test Ball creation")
    public void testBall() {
        Ball ball = new Ball(10);
        assertEquals(10, ball.getKnockedPins());
    }
}
