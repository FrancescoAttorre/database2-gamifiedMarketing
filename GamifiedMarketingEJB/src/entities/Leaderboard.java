package entities;

public class Leaderboard {

	User user;
	int points;
	
	public Leaderboard() {
		super();
	}
	
	public Leaderboard(User user, int points) {
		super();
		this.user = user;
		this.points = points;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
