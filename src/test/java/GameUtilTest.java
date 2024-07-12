import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.util.GameUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameUtilTest {

    @InjectMocks
    private GameUtil gameUtil;

    private String userId;
    private int score;
    private Player player;
    private String name;

    @BeforeEach
    public void setUp() {
        userId = "test_user";
        score = 48;
        name = "test_name";
        player = new Player(userId, score,name);
    }

    @Test
    public void testGetScore(){
        int score = gameUtil.getScore(userId);

        assertTrue(score>=0 && score<50);
    }
}
