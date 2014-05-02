/**
 * Write a description of class DummyBrain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DummyBrain extends Brain 
{
    // instance variables - replace the example below with your own

    /**
     * Constructor for objects of class DummyBrain
     */
    public void init()
    {
        this.creature.setCreatureImage("tux.png");
        this.creature.say("I'm just stand'n here loosing: "+this.creature.getIdleCost());
    }
    
    public void think(){
     this.creature.say(this.creature.getEnergy()+"");
    }
    
}
