USE gaming_dashboard;
CREATE TABLE player_score (
  user_id VARCHAR(500) NOT NULL,
  score int(100) NOT NULL,
  name VARCHAR(500)
  PRIMARY KEY (`user_id`)
);