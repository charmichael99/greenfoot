import greenfoot.GreenfootImage;
import greenfoot.World;

import java.awt.Color;
import java.awt.Font;
import java.util.List;


// Displays the ranking at the end of the game.
public class Ranking extends UX {

    private float FONT_SIZE = TheWorld.CELL_SIZE / 2;
    private int WIDTH;
    private int HEIGHT;

    private List<T<Integer, Integer, Creature>> ranking;

    public Ranking(List<T<Integer, Integer, Creature>> ranking) {
        this.ranking = ranking;
    }

    @Override
    protected void addedToWorld(World world) {
        this.WIDTH = (int) (0.75 * (TheWorld.WIDTH * TheWorld.CELL_SIZE));
        this.HEIGHT = (int) (0.75 * (TheWorld.HEIGHT * TheWorld.CELL_SIZE));
        this.show();
    }

    private void show() {
        // Create background.
        GreenfootImage image = new GreenfootImage(this.WIDTH, this.HEIGHT);

        image.setColor(new Color(0, 0, 0, 50));
        image.fillRect(0, 0, this.WIDTH, this.HEIGHT);
        image.setColor(new Color(255, 255, 255, 150));
        image.fillRect(5, 5, this.WIDTH - 10, this.HEIGHT - 10);
        
        // Set large font for the caption.
        Font font = image.getFont();
        font = font.deriveFont(FONT_SIZE);
        image.setFont(font);
        image.setColor(Color.BLACK);

        // Display caption
        String s = "Ranking";
        int x = (this.WIDTH / 2) - (s.length() * (int) this.FONT_SIZE / 4);
        image.drawString(s, x, TheWorld.CELL_SIZE);

        // Set smaller font for the list.
        font = font.deriveFont((float) TheWorld.CELL_SIZE / 3);
        image.setFont(font);
        image.setColor(Color.RED);
        // Display list.
        for (int i = 1; i <= this.ranking.size(); i++) {
            T<Integer, Integer, Creature> t = this.ranking.get(i - 1);
            String name = t.c.getName();
            if (name.length() > 20) {
                name = name.substring(0, 20) + "...";
            }
            s = "Rank " + i + ": \"" + name + "\" survived " + t.b + " rounds.";
            image.drawString(s, TheWorld.CELL_SIZE, (i + 1) * TheWorld.CELL_SIZE);
        }

        this.setImage(image);
    }
}
