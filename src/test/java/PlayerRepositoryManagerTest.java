import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerRepositoryManagerTest {

    @Mock
    private PlayerRepository scoreRepository;

    @InjectMocks
    private PlayerRepositoryManager playerRepositoryManager;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;

    @BeforeEach
    public void setUp(){
        player1 = new Player("test_user1",12,"Test User 1");
        player2 = new Player("test_user2",23,"Test User 2");
        player3 = new Player("test_user3",15,"Test User 3");
        player4 = new Player("test_user4",49,"Test User 4");
        player5 = new Player("test_user5",10,"Test User 5");
        player6 = new Player("test_user6",2,"Test User 6");

    }

    @Test
    public void testGetPlayerByIdIfPlayerExists(){
        when(scoreRepository.findById("test_user1")).thenReturn(Optional.ofNullable(player1));
        Player result = playerRepositoryManager.getPlayerById("test_user1");
        assertEquals(player1, result);
        verify(scoreRepository, times(2)).findById("test_user1");
    }

    @Test
    public void testGetPlayerByIdIfPlayerDoesntExist(){
        when(scoreRepository.findById("test_user1")).thenReturn(Optional.empty());
        NoSuchElementException e = assertThrows(NoSuchElementException.class,() -> {
            playerRepositoryManager.getPlayerById("test_user1");
        });
        assertEquals("No player found with user Id: test_user1", e.getMessage());
        verify(scoreRepository, times(1)).findById("test_user1");
    }

    @Test
    public void testFetchTop5PlayerRecords() {

        List<Player> allPlayers = Arrays.asList(player1, player2, player3, player4, player5, player6);
        when(scoreRepository.findAll()).thenReturn(allPlayers);
        List<Player> result = playerRepositoryManager.fetchTopNPlayerRecords();
        List<Player> expectedTopPlayers = Arrays.asList(player5, player4, player2, player3, player1,player6).stream()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());
        assertEquals(expectedTopPlayers, result);
        verify(scoreRepository, times(1)).findAll();
    }

}
