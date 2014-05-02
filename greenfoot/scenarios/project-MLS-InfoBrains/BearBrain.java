import greenfoot.*;


public class BearBrain extends Brain {

    public void init() {
        this.creature.setCreatureImage("polar-bear.png");
    }

    public void think() {
        Thing[] things = this.creature.lookAround(this.creature.getMaxViewRange());

        // Try to find the nearest animal.
        int x = this.creature.getX();
        int y = this.creature.getY();
        double nearest = Double.POSITIVE_INFINITY;
        Thing thing = null;
        for (Thing t : things) {
            if (t.getType() == Thing.ANIMAL && t.getEnergy() > 0) {
                double tmp = this.distance(t.getX(), t.getY(), x, y);
                if (tmp < nearest) {
                    nearest = tmp;
                    thing = t;
                }
            }
        }

        // If we see an animal...
        if (thing != null) {
            boolean inRange = Math.abs(thing.getX() - this.creature.getX())
                    + Math.abs(thing.getY() - this.creature.getY()) <= 1;
            if (inRange) {
                // ... attack if we can.
                this.creature.attack(thing);
            } else {
                // ... otherwise move to the animals direction.
                if (x < thing.getX()) {
                    this.creature.move(EAST);
                } else if (x > thing.getX()) {
                    this.creature.move(WEST);
                } else if (y < thing.getY()) {
                    this.creature.move(SOUTH);
                } else {
                    this.creature.move(NORTH);
                }
            }
        } else {
            // If we do not see an animal, move to a random direction.
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
    }

    private double distance(int ax, int ay, int bx, int by) {
        int dx = ax - bx;
        int dy = ay - by;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String getName() {
        return "Bear";
    }

}
