package gamingDashboard.gamingDashboardService.consumer;

import gamingDashboard.gamingDashboardService.model.Player;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;

@Service
public class KafkaConsumerService {

    private static PriorityQueue<Player> maxHeap = new PriorityQueue<>();

    @KafkaListener(topics = "player-score", groupId = "gameScoreGroupId")
    public void consume(Player message) {
        maxHeap.offer(message);
        System.out.println("Updated Max Heap: " + maxHeap);
    }

    public PriorityQueue<Player> getMaxHeap() {
        return maxHeap;
    }

}

