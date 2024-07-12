package gamingDashboard.gamingDashboardApi;

import gamingDashboard.gamingDashboardService.model.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public interface TopScoreService {

    @GetMapping("/top-score")
    List<Player> getTopScore();
}
