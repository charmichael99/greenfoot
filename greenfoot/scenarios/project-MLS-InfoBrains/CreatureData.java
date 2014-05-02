import java.lang.Math;


// Data storage for the world to handle attribute values from the creatures.
public class CreatureData {

    private int energy;
    private int maxEnergy;
    private int calories;

    private int speed;

    private int maxViewRange;

    private int idleCost;
    private int moveCost;
    private int lookCost;

    private int attackValue;

    private boolean alive;
    private int startRound;
    
    public CreatureData setEnergy(int energy) {
        if (this.maxEnergy != 0) {
            this.energy = Math.min(this.maxEnergy, energy);
        } else {
            this.energy = energy;
        }
        return this;
    }

    public int getEnergy() { return this.energy; }

    public CreatureData setMaxEnergy(int energy) {
        this.maxEnergy = energy;
        return this;
    }

    public int getMaxEnergy() { return this.maxEnergy; }

    public CreatureData setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public int getCalories() { return this.calories; }

    public CreatureData setCalories(int calories) {
        this.calories = calories;
        return this;
    }

    public int getSpeed() { return this.speed; }

    public CreatureData setMaxViewRange(int range) {
        this.maxViewRange = range;
        return this;
    }

    public int getMaxViewRange() { return this.maxViewRange; }

    public CreatureData setIdleCost(int cost) {
        this.idleCost = cost;
        return this;
    }

    public int getIdleCost() { return this.idleCost; }

    public CreatureData setMoveCost(int cost) {
        this.moveCost = cost;
        return this;
    }

    public int getMoveCost() { return this.moveCost; }

    public CreatureData setLookCost(int cost) {
        this.lookCost = cost;
        return this;
    }

    public int getLookCost() { return this.lookCost; }

    public CreatureData setAttackValue(int value) {
        this.attackValue = value;
        return this;
    }

    public int getAttackValue() { return this.attackValue; }

    public CreatureData setAlive(boolean alive) {
        this.alive = alive;
        return this;
    }

    public boolean isAlive() { return this.alive; }

    public CreatureData setStartRound(int round) {
        this.startRound = round;
        return this;
    }

    public int getStartRound() { return this.startRound; }

}
