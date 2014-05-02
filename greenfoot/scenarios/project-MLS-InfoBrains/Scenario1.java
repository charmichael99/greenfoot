public class Scenario1 extends TheWorld {


    public Scenario1() {
        this.addWater();
        this.addPlants();

        int d = 4;
        this.addObject(new Rock(), d, d);
        this.addObject(new Rock(), d, HEIGHT - d - 1);
        this.addObject(new Rock(), WIDTH - d - 1, d);
        this.addObject(new Rock(), WIDTH - d - 1, HEIGHT - d - 1);
        
        this.addObject(new Animal(new MarioBrain()), 2,2);
        this.addObject(new Animal(new DummyBrain()), 2,HEIGHT-3);
    }
}
