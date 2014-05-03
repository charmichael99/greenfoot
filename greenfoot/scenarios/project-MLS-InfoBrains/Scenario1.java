public class Scenario1 extends TheWorld {


    public Scenario1() {
        this.addWater();
        this.addPlants();
        
        int d = 4;
        this.addObject(new Rock(), randomPosition(), randomPosition());
        this.addObject(new Rock(), randomPosition(), randomPosition());
        this.addObject(new Rock(), randomPosition(), randomPosition());
        this.addObject(new Rock(), randomPosition(), randomPosition());
        
        this.addObject(new Animal(new IndianaJones()), 2,2);
        this.addObject(new Animal(new DummyBrain()), 2,HEIGHT-3);
    }
    
    private int randomPosition(){
        double x = ((Math.random() * 13) +1);
        
        return (int) Math.abs(x);
    }
}
