import greenfoot.*;

import java.lang.Math;


public class Plant extends Creature {

    private int chance = 15;  // percent
    private int radius = 1;

    public Plant(Brain brain) {
        super(brain);

        this.ENERGY = 30;
        this.MAX_ENERGY = 30;
        this.CALORIES = 30;

        this.SPEED = 4;

        this.IDLE_COST = 0;
    }

    public void doSomething() {
        // With a certain chance, grow another plant.
        if (Greenfoot.getRandomNumber(100) < chance) {
            int dx = this.radius - Greenfoot.getRandomNumber(2 * this.radius + 1);
            int dy = this.radius - Greenfoot.getRandomNumber(2 * this.radius + 1);

            int px = Math.min(Math.max(0, this.getX() + dx), this.world.getWidth() - 1);
            int py = Math.min(Math.max(0, this.getY() + dy), this.world.getHeight() - 1);

            if (this.world.getObjectsAt(px, py, Actor.class).isEmpty()) {
                this.world.addObject(new Plant(null), px, py);
            }
        }
    }

    // Override the paint method because we do not want to display the energy
    // bar for plants.
    public void paint() {
    }

}
