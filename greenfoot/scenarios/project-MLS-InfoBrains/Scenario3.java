import greenfoot.*;


public class Scenario3 extends TheWorld {

    public Scenario3() {
        this.addWater();
        this.addPlants();

        //this.addObject(new Mouse(new MouseBrain()), 2, HEIGHT / 2 - 1);
        //this.addObject(new Mouse(new MouseBrain()), 2, HEIGHT / 2);
        //this.addObject(new Mouse(new MouseBrain()), WIDTH - 3, HEIGHT / 2);
        //this.addObject(new Mouse(new MouseBrain()), WIDTH - 3, HEIGHT / 2 - 1);
        
        this.addObject(new Animal(new MarioBrain()), 2,2);
        //this.addObject(new Animal(new VegeterianBrain()), WIDTH-3,HEIGHT-3);
        this.addObject(new Animal(new DummyBrain()), 2,HEIGHT-3);
        
        int x = 0, y = 0;
        for (int i = 0; i < 5; i++) {
            while (!this.getObjectsAt(x, y, null).isEmpty()) {
                x = Greenfoot.getRandomNumber(WIDTH);
                y = Greenfoot.getRandomNumber(HEIGHT);
            }
            this.addObject(new Rock(), x, y);
        }
    }
}
