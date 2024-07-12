package gamingDashboard.gamingDashboardService.repository;

import gamingDashboard.Config.ConfigurationConstants;
import gamingDashboard.gamingDashboardService.model.Player;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class PlayerRepositoryManager {

    @Autowired
    private PlayerRepository scoreRepository;

    public Player getPlayerById(String userId){
        Player player;
            if(isPlayerPresent(userId)){
                player = scoreRepository.findById(userId).get();
                return player;
            }
            else {
                throw new NoSuchElementException(String.format("No player found with user Id: %s",userId));
            }
    }

    public boolean isPlayerPresent(String userId){
        return scoreRepository.findById(userId).isPresent();
    }

    public void savePlayer(Player player){
        scoreRepository.save(player);
    }

    public List<Player> fetchTopNPlayerRecords(){
        return scoreRepository.findAll().stream().sorted().limit(ConfigurationConstants.TOP_N_SCORERS).collect(Collectors.toList());
    }

}
