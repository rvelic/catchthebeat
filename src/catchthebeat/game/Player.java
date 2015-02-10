package catchthebeat.game;

/**
 * Class Player
 * 
 * This class serves as a data container of a single player.
 * It holds their name, human/computer flag and in/out of the game flag.
 * 
 * Then, a variety of getter and setter methods are included in the class,
 * which are self-explanatory.
 * 
 * @author Michal Kabát
 * @version 2012.04
 */
public class Player {

    private String name;
    private boolean isOut;
    private boolean isComputer;

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        this.isOut = false;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public boolean isOut() {
        return isOut;
    }

    public void playerOut() { // i.e. setIsOut(true)
        this.isOut = true;
    }

    public String getName() {
        return this.name;
    }
}
