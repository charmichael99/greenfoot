import greenfoot.*;

import java.awt.Color;


// Display an animation to visualize the lookAround(range) creature method.
public class Visibility extends UX {

    private int STEPS = 5;  // Animation steps.

    private Creature creature;
    private World world;

    public Visibility(Creature c, World w) {
        this.creature = c;
        this.world = w;
    }

    // Run the animation.
    public void display(int radius) {
        this.world.addObject(this, this.creature.getX(), this.creature.getY());
        double tmp = (2 * radius + 1) * TheWorld.CELL_SIZE;
        double oneStep = tmp / this.STEPS;

        GreenfootImage image = new GreenfootImage(100, 100);
        image.setColor(new Color(255, 255, 255, 50));
        image.fillRect(0, 0, 100, 100);

        for (int step = 1; step <= STEPS; step++) {
            int x = Math.max(1, (int) (oneStep * step));
            image.scale(x, x);
            this.setImage(image);
            Greenfoot.delay(1);
        }

        image.clear();
        this.world.removeObject(this);
    }
}
