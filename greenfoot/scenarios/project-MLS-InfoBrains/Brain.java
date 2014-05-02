public class Brain  {
    public final static int EAST = 0;
    public final static int WEST = 1;
    public final static int NORTH = 2;
    public final static int SOUTH = 3;

    protected Creature creature;

    public void think() {
    }

    public Brain setCreature(Creature c) {
        this.creature = c;
        return this;
    }

    public void init() {
    }

    public String getName() {
        return "No name set.";
    }
}
