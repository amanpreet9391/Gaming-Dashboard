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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    @Mock
    private GameUtil gameUtil;

    @Mock
    private PlayerRepository scoreRepository;

    @Mock
    private PlayerRepositoryManager playerRepositoryManager;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPlayGameIfUserExists() {
        String userId = "user123";
        Player player = new Player();
        player.setUserId(userId);

        when(playerRepositoryManager.isPlayerPresent(userId)).thenReturn(true);
        when(playerRepositoryManager.getPlayerById(userId)).thenReturn(player);
        when(gameUtil.getScore(userId)).thenReturn(100);

        ResponseEntity<String> response = gameServiceImpl.playGame(userId);

        verify(playerRepositoryManager, times(1)).isPlayerPresent(userId);
        verify(playerRepositoryManager, times(1)).getPlayerById(userId);
        verify(gameUtil, times(1)).getScore(userId);
        verify(playerRepositoryManager, times(1)).savePlayer(player);
        verify(kafkaProducerService, times(1)).sendMessage(player);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("100", response.getBody());
    }

    @Test
    public void testPlayGameIfUserDoesNotExists() {
        String userId = "user123";

        when(playerRepositoryManager.isPlayerPresent(userId)).thenReturn(false);

        ResponseEntity<String> response = gameServiceImpl.playGame(userId);

        verify(playerRepositoryManager, times(1)).isPlayerPresent(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("UserId doesn't exist", response.getBody());
    }

    @Test
    public void testPlayGameInternalServerError() {
        String userId = "user123";

        when(playerRepositoryManager.isPlayerPresent(userId)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<String> response = gameServiceImpl.playGame(userId);

        verify(playerRepositoryManager, times(1)).isPlayerPresent(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Game execution failed: Database error", response.getBody());
    }

    @Test
    public void testCreatePlayerSuccess() {
        Player player = new Player();
        player.setUserId("user123");

        ResponseEntity<String> response = gameServiceImpl.createPlayer(player);

        verify(playerRepositoryManager, times(1)).savePlayer(player);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user123", response.getBody());
    }

    @Test
    public void testCreatePlayerEmptyUserId() {
        Player player = new Player();
        player.setUserId("");

        ResponseEntity<String> response = gameServiceImpl.createPlayer(player);

        verify(playerRepositoryManager, never()).savePlayer(any(Player.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Player creation failed: Please provide a valid user Id", response.getBody());
    }

    @Test
    public void testCreatePlayerInternalError() {
        Player player = new Player();
        player.setUserId("user123");

        doThrow(new RuntimeException("Database error")).when(playerRepositoryManager).savePlayer(player);

        ResponseEntity<String> response = gameServiceImpl.createPlayer(player);

        verify(playerRepositoryManager, times(1)).savePlayer(player);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Player creation failed: Database error", response.getBody());
    }
}
