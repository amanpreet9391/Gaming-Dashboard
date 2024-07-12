package gamingDashboard.gamingDashboardService.producer;

import gamingDashboard.gamingDashboardService.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "player-score";

    @Autowired
    private KafkaTemplate<String, Player> kafkaTemplate;

    public void sendMessage(Player score) {
        kafkaTemplate.send(TOPIC, score);
    }
}
