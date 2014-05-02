/**
 * Write a description of class swarm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class swarm extends JBrains 
{
    int round = 0;
    
    public void init(){
        initiate("ant3.png");
    }
    
    public void think(){
        round++;
        this.creature.say(round+"");
        explore(3);
        printMap();
        pause(2000);
    }
    
}
