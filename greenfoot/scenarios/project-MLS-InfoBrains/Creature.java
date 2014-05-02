import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


public class Creature extends Actor {

    // Default values
    protected int ENERGY = 200;
    protected int MAX_ENERGY = 200;
    protected int CALORIES = 20;
    protected int SPEED = 4;
    protected int MAX_VIEW_RANGE = 3;
    protected int IDLE_COST = 4;
    protected int MOVE_COST = 5;
    protected int LOOK_COST = 5;
    protected int ATTACK_VALUE = 20;
    protected boolean ALIVE = true;

    // Attributes
    protected Brain brain;
    protected TheWorld world;
    protected Visibility vis;
    protected SpeechBubble bubble;
    protected CreatureData data;

    protected int ident = -1;
    protected boolean canAct;

    public Creature(Brain brain) {
        this.brain = brain;
    }

    // Initialize Creature data when it is added to the world.
    protected void addedToWorld(World world) {
        // Initialize creature attributes with default values.
        this.world = (TheWorld) world;
        this.world.initCreature(this);
        this.data = this.world.data.get(this);

        this.vis = new Visibility(this, this.world);

        // Create reference from Brain to Creature.
        if (this.brain != null) {
            // If ident is not set, the creature was not created with the
            // giveBirth() method and we get a new identifier.
            if(this.ident < 0) {
                this.ident = this.world.getNewId();
            }
            this.brain.setCreature(this);
            this.brain.init();
        }

        // Resize background image.
        GreenfootImage bg = this.getImage();
        bg.scale(TheWorld.CELL_SIZE - 15, TheWorld.CELL_SIZE - 15);
        this.setImage(bg);

        this.paint();
    }

    // Replacement for the act() method.
    public void doSomething() {
        this.canAct = true;

        if (this.brain != null) {
            try {
                this.brain.think();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // The following Creature methods are all described in the reference
    // section of the user documentation.


    // -------- Actions ---------

    // For every action method we need to check if no other action was called
    // in this turn ("canAct"), and if the creature has not died before in
    // this turn ("getEnergy() > 0").

    public void eat() {
        if (this.data.getEnergy() > 0 && this.canAct) {
            for (Creature c : (List<Creature>) this.world.getObjectsAt(this.getX(), this.getY(), Creature.class)) {
                if(c instanceof Plant || c.getEnergy() <= 0) {
                    this.world.addEnergy(this, c.getCalories());

                    // Remove the other Creature and its data.
                    this.world.removeObject(c);
                    this.world.data.remove(c);
                }
            }
        }

        // Disallow other actions in this turn.
        this.canAct = false;
    }

    // --------

    public void move(int direction) {
        if (this.data.getEnergy() > 0 && this.canAct && this.canMove(direction)) {
            // Move the creature.
            if (direction == Brain.EAST) {
                super.setLocation(this.getX() + 1, this.getY());
            } else if (direction == Brain.WEST) {
                super.setLocation(this.getX() - 1, this.getY());
            } else if (direction == Brain.NORTH) {
                super.setLocation(this.getX(), this.getY() - 1);
            } else if (direction == Brain.SOUTH) {
                super.setLocation(this.getX(), this.getY() + 1);
            }

            // Move the creatures speech bubble.
            if (this.bubble != null) {
                this.bubble.setLocation(this.getX(), this.getY() - 1);
            }
        }

        // Always subtract energy and disallow other actions in this turn.
        this.world.subtractEnergy(this, this.data.getMoveCost());
        this.canAct = false;

        // Pause a moment so that the movement is better visible from the GUI.
        Greenfoot.delay(1);
    }

    // Check if we can move one step to "direction" or if the cell is blocked.
    private boolean canMove(int direction) {
        int x = this.getX();
        int y = this.getY();

        if (direction == Brain.EAST) {
            x += 1;
        } else if (direction == Brain.WEST) {
            x -= 1;
        } else if (direction == Brain.NORTH) {
            y -= 1;
        } else if (direction == Brain.SOUTH) {
            y += 1;
        }

        boolean outOfWorld = x < 0 || y < 0 || x >= this.world.getWidth() || y >= this.world.getHeight();
        boolean blocked = !this.world.getObjectsAt(x, y, Wall.class).isEmpty();

        return !outOfWorld && !blocked;
    }

    // --------

    public void attack(Thing t) {
        if (this.data.getEnergy() > 0 && this.canAct) {
            Actor actor = null;

            // Get the private Creature attribute of the Thing object.
            // Security settings of the JVM may forbid this, but the default
            // settings of the SecurityManager do allow this.
            try {
                Field f = t.getClass().getDeclaredField("actor");
                f.setAccessible(true);
                actor = (Actor) f.get(t);
                f.setAccessible(false);
            } catch (Exception e) {
                System.err.println("Attacking does not work!");
                e.printStackTrace();
            }

            // Check if the the thing object represents a creature.
            Creature attacked = null;
            try {
                attacked = (Creature) actor;
            } catch (Exception e) {
            }

            // If its a creature, attack it.
            if (attacked != null) {
                boolean inRange = Math.abs(t.getX() - this.getX()) + Math.abs(t.getY() - this.getY()) <= 1;
                if (inRange) {
                    this.world.addObject(new Damage(), attacked.getX(), attacked.getY());
                    this.world.subtractEnergy(attacked, this.data.getAttackValue());
                }
            }
        }

        // Disallow other actions in this turn.
        this.canAct = false;
    }

    // --------

    public void giveBirth() {
        List<Integer[]> freeCells = this.freeCellsAround();
        if(this.canAct && !freeCells.isEmpty() && this.data.getEnergy() > 0) {
            try {
                // Create a new creature instance with the same identifier
                Constructor<? extends Brain> brainCtr = this.brain.getClass().getConstructor();
                Constructor<? extends Creature> creatureCtr = this.getClass().getConstructor(Brain.class);
                Creature c = creatureCtr.newInstance(brainCtr.newInstance());
                c.setIdent(this.ident);

                // Set values for new creature
                Integer[] pos = freeCells.get(Greenfoot.getRandomNumber(freeCells.size()));
                this.world.addObject(c, pos[0], pos[1]);
                CreatureData that = this.world.data.get(c);
                int e = this.data.getEnergy() / 2;
                this.data.setEnergy(e);
                that.setEnergy(e);
                that.setStartRound(this.data.getStartRound());

                this.paint();
                c.paint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Disallow other actions in this turn.
        this.canAct = false;
    }

    // Return all surrounding cells without walls.
    private List<Integer[]> freeCellsAround() {
        List<Integer[]> cells = new LinkedList<Integer[]>();

        int x = this.getX();
        int y = this.getY();
        for(int i=-1; i<=1; i++) {
            for(int j=-1; j<=1; j++) {
                if(i != 0 || j != 0) {
                    if(this.world.getObjectsAt(x + i, y + j, Wall.class).isEmpty()) {
                        cells.add(new Integer[] {x + i, y + j});
                    }
                }
            }
        }

        return cells;
    }

    // --------- Info methods ---------

    // We need to check if the creature is still alive for the lookAround()
    // method because it causes a visible effect, but we do not need to do
    // this for the other info methods.

    public Thing[] lookAround(int range) {
        if (this.data.getEnergy() <= 0) {
            return new Thing[0];
        }
/*
 * changed
 */
        // Validate the range parameter.
        range = Math.max(0, range);
		if (range > 0){
        	this.world.subtractEnergy(this, (range - 1 ) * this.data.getLookCost());
		}
        int allowedRange = Math.min(range, this.data.getMaxViewRange());

        // Display view animation.
        this.vis.display(allowedRange);

        // Get all actors within the given range.
        List<Actor> r = this.world.getObjectsAt(this.getX(), this.getY(), Actor.class);
        r.addAll(super.getNeighbours(allowedRange, true, Actor.class));

        // Return only a subset of all actors.
        return this.filterVisible(r);
    }

    // Only return Walls and Creatures from the list of actors.
    private Thing[] filterVisible(List<Actor> actors) {
        List<Thing> things = new LinkedList<Thing>();

        for (Actor a : actors) {
            if (a != this && (a instanceof Wall || a instanceof Creature)) {
                things.add(new Thing(a));
            }
        }

        return things.toArray(new Thing[0]);
    }

    // ----------

    public int getWorldWidth() { return this.world.getWidth(); }

    public int getWorldHeight() { return this.world.getHeight(); }

    public int getEnergy() { return this.data.getEnergy(); }

    public int getMaxEnergy() { return this.data.getMaxEnergy(); }

    public int getCalories() { return this.data.getCalories(); }
    
    public int getMaxViewRange() { return this.data.getMaxViewRange(); }

    public int getIdleCost() { return this.data.getIdleCost(); }

    public int getMoveCost() { return this.data.getMoveCost(); }

    public int getLookCost() { return this.data.getLookCost(); }

    public int getSpeed() { return this.data.getSpeed(); }

    public int getAttackValue() { return this.data.getAttackValue(); }

    // --------- Other methods --------

    public void say(String msg) {
        // Remove old bubble (if it exists).
        if (this.bubble != null) {
            try {
                this.world.removeObject(this.bubble);
            } catch (Exception e) {
            }
        }
        // Display new bubble.
        this.bubble = new SpeechBubble(msg, 2, this);
        this.world.addObject(this.bubble, 0, 0);
        Greenfoot.delay(1);
    }

    // --------

    public void setCreatureImage(String path) {
        try {
            this.setImage(new GreenfootImage(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // -------- Methods not used directly by animals --------

    public String getName() {
        if (this.brain != null) {
            return this.brain.getName();
        }
        return "Unknown creature.";
    }

    public CreatureData getDefaultValues() {
        return new CreatureData().setMaxEnergy(MAX_ENERGY)
                                 .setEnergy(ENERGY)
                                 .setCalories(CALORIES)
                                 .setSpeed(SPEED)
                                 .setMaxViewRange(MAX_VIEW_RANGE)
                                 .setIdleCost(IDLE_COST)
                                 .setMoveCost(MOVE_COST)
                                 .setLookCost(LOOK_COST)
                                 .setAttackValue(ATTACK_VALUE)
                                 .setAlive(ALIVE);
    }

    public void paint() {
        GreenfootImage img = this.getImage();

        // Draw bar to display energy directly on the creatures image.

        int ENERGY_WIDTH = 5;

        int energy = Math.max(0, this.world.data.get(this).getEnergy());
        int maxEnergy = this.world.data.get(this).getMaxEnergy();
        double ratio = (double) energy / (double) maxEnergy;
        int h = img.getHeight();
        int energyHeight = (int) ((double) h * ratio);

        img.setColor(new Color(255, 0, 0, 255));
        img.fillRect(0, 0, ENERGY_WIDTH, h);
        img.setColor(new Color(0, 0, 0, 255));
        img.fillRect(0, h - energyHeight, ENERGY_WIDTH, h);

        this.setImage(img);
    }

    public Creature setIdent(int id) {
        this.ident = id;
        return this;
    }

    public int getIdent() { return this.ident; }


    // Override all other methods to do nothing to prevent "cheating".

    @Override
    public void act() {}

    @Override
    protected List<Actor> getIntersectingObjects(Class cls) { return null; }

    @Override
    protected List<Actor> getNeighbours(int distance, boolean diagonal, Class cls) { return null; }

    @Override
    protected List<Actor> getObjectsAtOffset(int dx, int dy, Class cls) { return null; }

    @Override
    protected List<Actor> getObjectsInRange(int radius, Class cls) { return null; }

    @Override
    protected Actor getOneIntersectingObject(Class cls) { return null; }

    @Override
    protected Actor getOneObjectAtOffset(int dx, int dy, Class cls) { return null; }

    @Override
    public World getWorld() { return null; }

    @Override
    protected boolean intersects(Actor other) { return false; }

    @Override
    public void setLocation(int x, int y) {}

    @Override
    public int getRotation() { return 0; }

    @Override
    public void setRotation(int rotation) {}

    @Override
    public void turn(int amount) {}
}
