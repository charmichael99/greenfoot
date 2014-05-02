public class Bear extends Creature {

    public Bear(Brain brain) {
        super(brain);

        this.ENERGY = 300;
        this.MAX_ENERGY = 300;
        this.CALORIES = 100;
        this.SPEED = 1;
        this.MAX_VIEW_RANGE = 4;
        this.IDLE_COST = 0;
        this.MOVE_COST = 0;
        this.LOOK_COST = 0;
        this.ATTACK_VALUE = 50;
    }
}
