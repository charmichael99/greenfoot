import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class swarmScenario here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class swarmScenario extends TheWorld
{

    public swarmScenario(){
        this.addWater();
        this.addPlants();
        
        this.addObject(new Animal(new swarm()), 2,2);
        this.addObject(new Animal(new swarm()), 10,10);
        this.addObject(new Animal(new swarm()), 10,2);
        
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
