import greenfoot.Actor;


// Representation object for an actor without direct access to the actor.
public class Thing {

    public final static int ROCK = 0;
    public final static int WATER = 1;
    public final static int PLANT = 2;
    public final static int ANIMAL = 3;
    public final static int BEAR = 4;
    public final static int MOUSE = 5;

    private Actor actor;  // We need this attribute for the attack() method.
    private int type;
    private int x;
    private int y;
    private int energy;
    private int calories;

    public Thing(Actor a) {
        if (a instanceof Rock) {
            this.type = ROCK;
        } else if (a instanceof Water) {
            this.type = WATER;
        } else if (a instanceof Plant) {
            this.type = PLANT;
        } else if (a instanceof Bear) {
            this.type = BEAR;
        } else if (a instanceof Animal) {
            this.type = ANIMAL;
        } else if (a instanceof Mouse) {
            this.type = MOUSE;
        }

        // Save the current attribute values at the time the Thing is created.
        this.actor = a;
        this.x = a.getX();
        this.y = a.getY();
        this.energy = a instanceof Creature ? ((Creature) a).getEnergy() : 0;
        this.calories = a instanceof Creature ? ((Creature) a).getCalories() : 0;
    }

    public int getType() { return this.type; }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public int getEnergy() { return this.energy; }

    public int getCalories() { return this.calories; }

}
