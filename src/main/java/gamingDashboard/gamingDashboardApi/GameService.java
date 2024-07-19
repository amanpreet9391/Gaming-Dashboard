package gamingDashboard.gamingDashboardApi;

import gamingDashboard.gamingDashboardService.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface GameService {

    @PostMapping("/play-game")
    ResponseEntity<String> playGame(@RequestBody String userId);

    @PostMapping("/create-player")
    ResponseEntity<String> createPlayer(@RequestBody Player player);

}
