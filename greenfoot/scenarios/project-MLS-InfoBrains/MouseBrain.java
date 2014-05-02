import greenfoot.Greenfoot;


public class MouseBrain extends Brain {

    private Thing plant = null;
    private int steps = 4;
    private boolean eat = false;

    public void init() {
        this.creature.setCreatureImage("mouse.png");
    }

    public void think() {
        if (eat == true) {
            // If our last turn was to eat, call giveBirth() now.
            eat = false;
            this.creature.giveBirth();
            return;
        }
        if (plant != null) {
            // If we have already found a plant we want to eat,
            // move around it for some time and do not look for other plants.
            if (steps <= 0) {
                this.creature.eat();
                eat = true;
                this.plant = null;
                this.steps = 2;
            } else if (steps % 2 == 1) {
                this.moveTo(plant);
                this.steps -= 1;
            } else {
                this.moveRandom();
                this.steps -= 1;
            }
            return;
        }

        // Otherwise look for the nearest plant.
        Thing[] things = this.creature.lookAround(this.creature.getMaxViewRange());

        int x = this.creature.getX();
        int y = this.creature.getY();

        Thing thing = null;
        double nearest = Double.POSITIVE_INFINITY;
        for (Thing t : things) {
            if (t.getType() == Thing.PLANT && t.getEnergy() > 0) {
                double tmp = this.distance(t.getX(), t.getY(), x, y);
                if (tmp < nearest) {
                    nearest = tmp;
                    thing = t;
                }
            }
            if (thing == null && t.getType() == Thing.ANIMAL && t.getEnergy() > 0) {
                boolean inRange = Math.abs(t.getX() - this.creature.getX()) + Math.abs(t.getY() - this.creature.getY()) <= 1;
                if(inRange) {
                    thing = t;
                }
            }
        }

        // If there is no plant, move to a random direction.
        if (thing == null) {
            this.moveRandom();
            return;
        }

        // If there is an animal standing next to us, attack it.
        if(thing.getType() == Thing.ANIMAL) {
            this.creature.attack(thing);
            return;
        }

        // If the plant is only one field away, this is the plant we want to
        // eat.
        boolean inRange = Math.abs(thing.getX() - this.creature.getX()) + Math.abs(thing.getY() - this.creature.getY()) <= 1;
        if (inRange) {
            this.plant = thing;
        }
        // Move to the direction of the plant.
        this.moveTo(thing);
    }

    private void moveTo(Thing t) {
        int x = this.creature.getX();
        int y = this.creature.getY();

        if (x < t.getX()) {
            this.creature.move(EAST);
        } else if (x > t.getX()) {
            this.creature.move(WEST);
        } else if (y < t.getY()) {
            this.creature.move(SOUTH);
        } else {
            this.creature.move(NORTH);
        }
    }

    private void moveRandom() {
        int direction = Greenfoot.getRandomNumber(4);
        if (direction == 0) {
            this.creature.move(EAST);
        } else if (direction == 1) {
            this.creature.move(WEST);
        } else if (direction == 2) {
            this.creature.move(NORTH);
        } else {
            this.creature.move(SOUTH);
        }
    }

    private double distance(int ax, int ay, int bx, int by) {
        int dx = ax - bx;
        int dy = ay - by;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String getName() {
        return "Grasshopper";
    }
}
