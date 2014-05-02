public class Scenario2 extends TheWorld {

    public Scenario2() {
        this.addWater();
        this.addPlants();

        int d = 4;
        this.addObject(new Rock(), d, d);
        this.addObject(new Rock(), d, HEIGHT - d - 1);
        this.addObject(new Rock(), WIDTH - d - 1, d);
        this.addObject(new Rock(), WIDTH - d - 1, HEIGHT - d - 1);

        this.addObject(new Bear(new BearBrain()), 2, HEIGHT / 2);
        this.addObject(new Bear(new BearBrain()), WIDTH - 3, HEIGHT / 2);
    }
}
