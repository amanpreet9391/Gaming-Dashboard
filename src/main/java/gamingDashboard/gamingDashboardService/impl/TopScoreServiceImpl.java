package gamingDashboard.gamingDashboardService.impl;

import gamingDashboard.Config.ConfigurationConstants;
import gamingDashboard.gamingDashboardApi.TopScoreService;
import gamingDashboard.gamingDashboardService.consumer.KafkaConsumerService;
import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopScoreServiceImpl extends AbstractService implements TopScoreService {

    @Autowired
    private KafkaConsumerService kafkaConsumer;

    @Autowired
    private PlayerRepositoryManager playerRepositoryManager;

    @Autowired
    private PlayerRepository scoreRepository;

    public List<Player> getTopScore(){
        List<Player> topScorers;
        if(kafkaConsumer.getMaxHeap().size()<ConfigurationConstants.TOP_N_SCORERS){
            topScorers = playerRepositoryManager.fetchTopNPlayerRecords();
        }
        else{
            topScorers= kafkaConsumer.getMaxHeap().stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }

        return topScorers;
    }
}
