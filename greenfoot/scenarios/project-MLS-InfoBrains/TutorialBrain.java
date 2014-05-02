public class TutorialBrain extends Brain {

    // We have to use the init method instead of the
    // constructor for initialization code.
    public void init() {
        this.creature.setCreatureImage("snake2.png");
    }

    public void think() {
        this.creature.move(EAST);
    }

    public String getName() {
        return "Tutorial Brain";
    }
}
