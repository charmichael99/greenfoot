import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


// Displays the map and runs the game from the act() method.
public class TheWorld extends World {

    // Attributes to define the (displayed) size of the world.
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    //public static final int CELL_SIZE = 60;
    public static final int CELL_SIZE = 40;

    // Log to display (debug) information.
    private Log log;
    private boolean showLog = false;

    // Attributes to manage creatures.
    public HashMap<Creature, CreatureData> data = new HashMap<Creature, CreatureData>();
    private int creatureId = 0;

    // Attributes for the ranking.
    private HashMap<Integer, T<Integer, Integer, Creature>> ranks = new HashMap<Integer, T<Integer, Integer, Creature>>();
    private int rank = 0;
    private int round = 0;

    public TheWorld() {
        super(WIDTH, HEIGHT, CELL_SIZE);

        GreenfootImage bg = new GreenfootImage("sand.png");
        bg.scale(CELL_SIZE, CELL_SIZE);
        this.setBackground(bg);

        this.setPaintOrder(Ranking.class,
                           Log.class,
                           Animal.class,
                           Damage.class,
                           SpeechBubble.class,
                           Bear.class,
                           Mouse.class,
                           Plant.class,
                           Wall.class,
                           Visibility.class);
        this.setActOrder(World.class);
    }

    // Generate identifiers for creatures, so that we can group animals
    // created with the giveBirth() method.
    public int getNewId() {
        this.creatureId += 1;
        return this.creatureId;
    }

    // Create and save a new CreatureData instance with default values.
    public void initCreature(Creature c) {
        if (this.data.get(c) == null) {
            CreatureData d = c.getDefaultValues();
            d.setStartRound(this.round);
            this.data.put(c, d);
        }
    }

    // Mainloop of the game.
    public void act() {
        this.round += 1;

        List<Creature> creatures = this.getObjects(Creature.class);

        // Subtract idle energy for each alive creatures, reduce calorie count
        // for dead creatures and remove them if none is left.
        for (Creature c : creatures) {
            CreatureData d = this.data.get(c);
            if (d.isAlive()) {
                this.subtractEnergy(c, d.getIdleCost());
            } else {
                d.setCalories(d.getCalories() - 5);
                if (d.getCalories() <= 0) {
                    this.removeObject(c);
                }
            }
            c.paint();
        }

        // Stop the game and display the ranking if no animal is left alive.
        boolean stop = true;
        Set<Integer> idents = new HashSet<Integer>();
        for (Animal a : (List<Animal>) this.getObjects(Animal.class)) {
            if (this.data.get(a).isAlive()) {
                stop = false;
                idents.add(a.getIdent());
            }
        }
        if (stop || idents.size() == 1) {
            for(Animal a : (List<Animal>) this.getObjects(Animal.class)) {
                if(this.data.get(a).isAlive()) {
                    int survived = this.round - this.data.get(a).getStartRound();
                    this.ranks.put(a.getIdent(), new T<Integer, Integer, Creature>(this.rank, survived, a));
                    this.rank += 1;
                }
            }

            List<T<Integer, Integer, Creature>> r = new LinkedList<T<Integer, Integer, Creature>>();
            for(T<Integer, Integer, Creature> t : this.ranks.values()) {
                r.add(t);
            }
            this.sortByRounds(r);
            this.addObject(new Ranking(r), this.getWidth() / 2, this.getHeight() / 2);
            Greenfoot.stop();
        }

        // Add a plant if there is no other plant left.
        if (this.getObjects(Plant.class).isEmpty()) {
            this.addObject(new Plant(null), this.getWidth() / 2, this.getHeight() / 2);
        }

        // Manage turns of the creatures. The loop is run with increasing speed
        // value, until no creature with a greater speed exists.
        int speed = 1;
        boolean turns = true;
        this.sortBySpeed(creatures);
        while (turns) {
            turns = false;
            for (Creature c : creatures) {
                CreatureData d = data.get(c);
                // d may be null if the creature died or is a plant that was eaten.
                if (d != null && d.isAlive() && d.getSpeed() >= speed) {
                    turns = true;

                    // Display debug information for this turn?
                    if (this.showLog) {
                        this.log = new Log(c.getName() + " is thinking.");
                        this.addObject(this.log, 0, 0);
                    }

                    // Allow creature to act.
                    c.doSomething();

                    // Hide debug information.
                    if (this.log != null) {
                        this.removeObject(this.log);
                    }
                }
            }
            speed += 1;
        }
    }

    // Always use one of the following two methods to change the energy value
    // of a creature. They handle all events that may be caused by the change.

    public int addEnergy(Creature c, int amount) {
        // Increase energy.
        CreatureData d = this.data.get(c);
        d.setEnergy(d.getEnergy() + amount);

        // Repaint energy bar.
        c.paint();

        // Return new energy value.
        return d.getEnergy();
    }

    public int subtractEnergy(Creature c, int amount) {
        // Subtract energy.
        CreatureData d = this.data.get(c);
        d.setEnergy(d.getEnergy() - amount);

        // Is the animal dead?
        if (d.isAlive() && d.getEnergy() <= 0) {
            d.setAlive(false);

            // Add an entry to the ranking for animals.
            if(c instanceof Animal) {
                int survived = this.round - d.getStartRound();
                this.ranks.put(c.getIdent(), new T<Integer, Integer, Creature>(this.rank, survived, c));
                this.rank += 1;
            }

            // Visualize that the creature is dead.
            GreenfootImage img = c.getImage();
            img.setColor(new Color(0, 0, 0, 50));
            img.fillRect(0, 0, img.getWidth(), img.getHeight());
            c.setImage(img);
        }

        // Repaint the energy bar.
        c.paint();

        // Return new energy value.
        return d.getEnergy();
    }

    // Toggle visibility of the debug log.
    public void showLog() { this.showLog = true; }
    public void hideLog() { this.showLog = false; }

    // Sort creatures by speed to define the order of their turns.
    private void sortBySpeed(List<Creature> creatures) {
        Collections.sort(creatures, new Comparator<Creature>() {
            public int compare(Creature a, Creature b) {
                return data.get(b).getSpeed() - data.get(a).getSpeed();
            }
        });
    }

    // Sort ranking entries by number of survived rounds.
    private void sortByRounds(List<T<Integer, Integer, Creature>> ranking) {
        Collections.sort(ranking, new Comparator<T<Integer, Integer, Creature>>() {
            public int compare(T<Integer, Integer, Creature> x, T<Integer, Integer, Creature> y) {
                return y.a - x.a;
            }
        });
    }


    // The following methods add all objects for the corresponding scenario.

    protected void addWater() {
        for (int i = 0; i < WIDTH; i++) {
            this.addObject(new Water(), i, 0);
            this.addObject(new Water(), i, HEIGHT - 1);
            this.addObject(new Water(), 0, i);
            this.addObject(new Water(), WIDTH - 1, i);
        }
    }

    protected void addPlants() {
        int cx = WIDTH / 2;
        int cy = HEIGHT / 2;
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                this.addObject(new Plant(null), x, y);
            }
        }
    }
}
