package gamingDashboard.gamingDashboardService.model;


import gamingDashboard.Config.ConfigurationConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Table storing the player userId, name and score.
@Entity
@Table(name = ConfigurationConstants.PLAYER_SCORE_TABLE_NAME)
public class Player implements Comparable<Player>{

    @Id
    @Column(name="user_id")
    private String userId;
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Player(){

    }

    public Player(String userId, String name){
        this.userId = userId;
        this.name = name;
    }

    public Player(String userId, int score, String name){
        this.userId = userId;
        this.score = score;
        this.name = name;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "playerScore{" +
                "userId='" + userId + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score); // Max heap based on score
    }
}
