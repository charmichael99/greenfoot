import greenfoot.*;

import java.awt.Color;


// Displays a speech bubble above a creature.
public class SpeechBubble extends UX {

    private int FONT_SIZE = TheWorld.CELL_SIZE / 3;

    private String msg;
    private int duration;
    private Actor actor;
    private World world;

    public SpeechBubble(String msg, int duration, Actor a) {
        this.msg = msg;
        this.duration = duration;
        this.actor = a;
    }

    protected void addedToWorld(World w) {
        this.world = w;
        GreenfootImage img = new GreenfootImage(this.msg, FONT_SIZE, Color.BLACK, Color.WHITE);
        this.setImage(img);
        this.setLocation(this.actor.getX(), this.actor.getY() - 1);
    }

    public void act() {
        this.duration -= 1;
        if (this.duration < 0) {
            this.world.removeObject(this);
        }
    }
}
