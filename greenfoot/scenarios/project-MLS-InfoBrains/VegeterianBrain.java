/**
 * Write a description of class VegetarianBrain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VegeterianBrain extends JBrains 
{
    public void init(){
        initiate("frog.png");
    }
    
    public void think(){
        this.creature.say(this.creature.getEnergy()+"");
        
        //if you could only eat 2 more plants
        //and there are more than 4 plants around
        if(this.creature.getEnergy() > this.creature.getMaxEnergy()-(40) && numberOfThings(0,0,1,2)>3){
            this.creature.say("It's a boy");
            this.creature.giveBirth();
        }
        
        //check enviroment
        explore(1);
        //printMap(); //tell me what you found
         
         
        //check for plants
        int[] nearestPlant = findNearestPlant(0,0);
        
        if(nearestPlant!=null){
            //System.out.println(knownPlants[0]+":"+knownPlants[1]);
            //System.out.println(distance(knownPlants[0],knownPlants[1]));
            //pause(5000);
        }
        
        if(nearestPlant==null){
            moveToMiddle();
            return;
        }else if(distance(0,0,nearestPlant[0],nearestPlant[1])==0){
            this.creature.eat();
            //Remember that you ate the plant
            field[nearestPlant[0]][nearestPlant[1]] = -2;
            return;
        }else{
            moveTowards(nearestPlant[0],nearestPlant[1]);
            return;
        }
       
        
        
    
    }
}
