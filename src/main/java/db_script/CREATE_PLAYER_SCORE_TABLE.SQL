USE gaming_dashboard;
CREATE TABLE player_score (
  user_id VARCHAR(500) NOT NULL,
  score int(100) NOT NULL
  PRIMARY KEY (`user_id`)
);