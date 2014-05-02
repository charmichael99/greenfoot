import greenfoot.*;

import java.awt.Color;


// Displays a log message at the top of the world.
public class Log extends UX {
    private int FONT_SIZE = 24;

    private String msg;

    public Log(String msg) {
        this.msg = msg;
    }

    protected void addedToWorld(World w) {
        GreenfootImage img = new GreenfootImage(this.msg, FONT_SIZE, Color.BLACK, Color.WHITE);
        this.setImage(img);
        this.setLocation(TheWorld.WIDTH / 2, 0);
    }
}
