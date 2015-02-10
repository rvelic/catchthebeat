package catchthebeat.game;

/**
 * Class Score
 * 
 * This class serves as a Score data container.
 * It holds the current winner (Player object) and number of points earned 
 * (i.e. size of current beat array).
 * 
 * Then, a variety of getter and setter methods are included in the class to
 * update the score during the gameplay. These methods are self-explanatory.
 * 
 * The class also implements Comparable interface which is useful when checking
 * whether the reached score beats any of the high scores.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class Score implements Comparable {

    private Player winner;
    private int points;

    /** 
     * This constructor is used when a new game is created.
     */
    public Score() {
        this.winner = null;
        this.points = 0;
    }

    /** 
     * This constructor is used to construct the individual high scores 
     * to compare them to the score reached in the last game.
     */
    public Score(Player winner, int points) {
        this.winner = winner;
        this.points = points;
    }

    // The following getter and setter methods are self-explanatory.
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player topPlayer) {
        this.winner = topPlayer;
    }

    /** 
     * Implementation of Comparable abstract method comparing the score
     * variables.
     */
    @Override
    public int compareTo(Object t) {
        Score aScore = (Score) t;
        if (this.points < aScore.points) {
            return 1;
        } else if (this.points > aScore.points) {
            return -1;
        } else {
            return 0;
        }
    }
}
