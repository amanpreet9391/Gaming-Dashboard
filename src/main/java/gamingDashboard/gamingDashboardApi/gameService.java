package gamingDashboard.gamingDashboardApi;

import gamingDashboard.gamingDashboardService.model.Player;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface gameService {

    @PostMapping("/play-game")
    String playGame(@RequestBody Player player);

}
