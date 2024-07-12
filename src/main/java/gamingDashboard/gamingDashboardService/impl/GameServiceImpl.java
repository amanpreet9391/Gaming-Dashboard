package gamingDashboard.gamingDashboardService.impl;

import gamingDashboard.gamingDashboardApi.gameService;
import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.producer.KafkaProducerService;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import gamingDashboard.gamingDashboardService.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl extends AbstractService implements gameService  {

    @Autowired
    public GameUtil gameUtil;

    @Autowired
    public PlayerRepository scoreRepository;

    @Autowired
    private PlayerRepositoryManager playerRepositoryManager;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Override
    public String playGame(Player player) {
        try{
            String userId = player.getUserId();
            String name = player.getName();
            int score = gameUtil.getScore(userId);
            if(playerRepositoryManager.isPlayerPresent(userId)){
                player = playerRepositoryManager.getPlayerById(userId);
                player.setScore(score);
            }
            else{
                player = new Player(userId,score,name);
            }

            //Publish the player details to kafka topic
            kafkaProducerService.sendMessage(player);
            logger.info("Published to Kafka Topic");
            playerRepositoryManager.savePlayer(player);
            logger.info("Player details saved to db");
            return successWithPayload(score);

        } catch (Exception e){
            logger.error("Game execution failed - {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }
}
