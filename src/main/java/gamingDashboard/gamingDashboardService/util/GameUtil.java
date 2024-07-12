package gamingDashboard.gamingDashboardService.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GameUtil {

    public int getScore(String userId){
        Random rand = new Random();
        int score = rand.nextInt(50);
        return score;
    }

}
