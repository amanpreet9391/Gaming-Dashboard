import gamingDashboard.Config.ConfigurationConstants;
import gamingDashboard.gamingDashboardService.consumer.KafkaConsumerService;
import gamingDashboard.gamingDashboardService.impl.TopScoreServiceImpl;
import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopScoreServiceTest {

    @Mock
    private KafkaConsumerService kafkaConsumer;

    @Mock
    private PlayerRepositoryManager playerRepositoryManager;

    @Mock
    private PlayerRepository scoreRepository;

    @InjectMocks
    private TopScoreServiceImpl topScoreService;

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
    public void TestGetTopScoreWhenHeapSizeIsLessThan5(){
        when(kafkaConsumer.getMaxHeap()).thenReturn(new PriorityQueue<Player>(Arrays.asList(player1,player2,player5)));
        when(playerRepositoryManager.fetchTopNPlayerRecords()).thenReturn(Arrays.asList(player4,player2,player3,player1,player5));

        List<Player> topScorers = topScoreService.getTopScore();

        List<Player> expectedList = Arrays.asList(player4,player2,player3,player1,player5);
        assertEquals(expectedList,topScorers);
        verify(kafkaConsumer,times(1)).getMaxHeap();
        verify(playerRepositoryManager,times(1)).fetchTopNPlayerRecords();
    }

    @Test
    public void TestGetTopScoreWhenHeapSizeIsGreaterThan5(){
        PriorityQueue<Player> maxHeap = new PriorityQueue<Player>(Arrays.asList(player6,player5,player3,player1,player2,player4));
        when(kafkaConsumer.getMaxHeap()).thenReturn(maxHeap);

        List<Player> topScorers = topScoreService.getTopScore();
        List<Player> expectedList = Arrays.asList(player4,player2,player3,player1,player5);

        assertEquals(expectedList,topScorers);
        verify(kafkaConsumer,times(2)).getMaxHeap();
        verify(playerRepositoryManager,never()).fetchTopNPlayerRecords();
    }



}
