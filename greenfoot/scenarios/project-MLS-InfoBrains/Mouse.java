

public class Mouse extends Creature {
    public Mouse(Brain b) {
        super(b);

        this.ENERGY = 20;
        this.MAX_ENERGY = 20;
        this.CALORIES = 20;
        this.SPEED = 4;
        this.MAX_VIEW_RANGE = 3;
        this.LOOK_COST = 0;
        this.MOVE_COST = 1;
        this.IDLE_COST = 0;
        this.ATTACK_VALUE = 10;
    }
}
