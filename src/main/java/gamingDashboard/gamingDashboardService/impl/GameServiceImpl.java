package gamingDashboard.gamingDashboardService.impl;

import gamingDashboard.gamingDashboardApi.GameService;
import gamingDashboard.gamingDashboardService.model.Player;
import gamingDashboard.gamingDashboardService.producer.KafkaProducerService;
import gamingDashboard.gamingDashboardService.repository.PlayerRepository;
import gamingDashboard.gamingDashboardService.repository.PlayerRepositoryManager;
import gamingDashboard.gamingDashboardService.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class GameServiceImpl extends AbstractService implements GameService {

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
    public ResponseEntity<String> playGame(String userId) {
        Player player = null;
        int score = 0;
        try{

            if(playerRepositoryManager.isPlayerPresent(userId)){
                player = playerRepositoryManager.getPlayerById(userId);
                score = gameUtil.getScore(userId);
                player.setScore(score);
            }
            else{
                logger.error("UserId doesn't exist");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserId doesn't exist");
            }

            //Publish the player details to kafka topic
            kafkaProducerService.sendMessage(player);
            logger.info("Published to Kafka Topic");
            playerRepositoryManager.savePlayer(player);
            logger.info("Player details saved to db");
            return ResponseEntity.ok(successWithPayload(score));

        } catch (Exception e){
            logger.error("Game execution failed - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Game execution failed: " + e.getMessage());
        }

    }

    public ResponseEntity<String> createPlayer(Player player) {
        try{
            //Can add more validations and checks for userId or other fields.
            if(player.getUserId().isEmpty()){
                throw new RuntimeException("Please provide a valid user Id");
            }
            playerRepositoryManager.savePlayer(player);
            return ResponseEntity.ok(successWithPayload(player.getUserId()));

        } catch (Exception e){
            logger.error("Player creation failed - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Player creation failed: " + e.getMessage());

        }

    }
}
