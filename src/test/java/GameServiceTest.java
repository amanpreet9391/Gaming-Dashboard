import gamingDashboard.gamingDashboardService.impl.GameServiceImpl;
import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.producer.KafkaProducerService;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import gamingDashboard.gamingDashboardService.util.GameUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameUtil gameUtil;

    @Mock
    private PlayerRepository scoreRepository;

    @Mock
    private PlayerRepositoryManager playerRepositoryManager;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private GameServiceImpl gameService;

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
    public void testPlayGameForNewPlayer(){
        when(playerRepositoryManager.isPlayerPresent(userId)).thenReturn(false);
        when(gameUtil.getScore(userId)).thenReturn(score);

        String response = gameService.playGame(player);

        assertNotNull(response);
        assertTrue(response.contains(String.valueOf(score)));
        verify(playerRepositoryManager, times(1)).savePlayer(any(Player.class));
        verify(kafkaProducerService, times(1)).sendMessage(any(Player.class));
    }

    @Test
    public void testPlayGameForExistingPlayer(){
        when(playerRepositoryManager.isPlayerPresent(userId)).thenReturn(true);
        when(gameUtil.getScore(userId)).thenReturn(score);
        when(playerRepositoryManager.getPlayerById(userId)).thenReturn(player);

        String response = gameService.playGame(player);
        assertNotNull(response);
        assertTrue(response.contains(String.valueOf(score)));
        verify(playerRepositoryManager, times(1)).savePlayer(any(Player.class));
        verify(kafkaProducerService, times(1)).sendMessage(any(Player.class));

    }

    @Test
    public void testPlayGameForExceptionInGetScore(){
        when(gameUtil.getScore(userId)).thenThrow(new RuntimeException("Game execution failed"));
        RuntimeException exception = assertThrows(RuntimeException.class,() -> {
            gameService.playGame(player);
        });

        assertEquals("Game execution failed",exception.getMessage());
    }


}
