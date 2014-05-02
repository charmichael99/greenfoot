import java.awt.Color;

import greenfoot.*;


// Animation for damage done by attacks.
public class Damage extends UX {

    private int STEPS = 5;
    private double SIZE = 1.5 * TheWorld.CELL_SIZE;

    protected void addedToWorld(World w) {
        GreenfootImage img = new GreenfootImage(1, 1);
        img.setColor(Color.YELLOW);
        img.fill();
        img.setTransparency(100);

        double oneStep = this.SIZE / this.STEPS;

        for (int step = 1; step <= STEPS; step++) {
            int x = Math.max(1,(int) (oneStep * step));
            img.scale(x, x);
            this.setImage(img);
            Greenfoot.delay(1);
        }

        img.clear();
        w.removeObject(this);
    }

}
