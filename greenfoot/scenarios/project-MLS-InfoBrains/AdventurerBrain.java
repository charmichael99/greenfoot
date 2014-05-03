/**
 * @author (Zickmantel, Reising) 
 * @version (1.0)
 * 
 * Great-Grandfather-Class:
 * - teaches walking to coordinates
 * - teaches walking to center
 * - teaches exploring
 * - and saving of information
 */
public class AdventurerBrain extends Brain 
{
    
    public static int round = 0;
    public static Map map;
    
    //Costs
    int maxView, viewCost;
    
    /*
     * TO DO:
     * - only call intiate if first instance
     * - remember wich round
     * - call half-life function of map once a round
     */
    
    public void initiate(String icon){
        this.creature.setCreatureImage(icon);  //Set Image
        
        //Get costs for exploring
        maxView = this.creature.getMaxViewRange();
        viewCost = this.creature.getLookCost();
        
        //clear terminal
        System.out.print('\f'); 
       
        //create map
        if(map==null){  //but only if no other instance has; fix for: amnesia at child birth
            map = new Map(this.creature);
        }
        
        //Visualisation --> delete in final version
        map.print();
        pause(1000);
    }
    
    /*
     * GOAL: explore the surroundings in range
     */
    public void explore(int range){
      
        Thing[] things = this.creature.lookAround(range);   //explore surroundings
        map.update(this.creature, things, range);   //save explorations
        
        //Visualisation --> delete in final version
        map.print();
    }
    
    /*
     * GOAL: move towards coordinates
     * ORDER: east-west-south-north
     * RETURNS: true if walking was succesful, else false (f.ex. position == coordinates)
     * 
     * TO DO:
     * check if moving is possible!
     */
    public boolean moveTowards(int x, int y){
         int distX = x - this.creature.getX();
         int distY = y - this.creature.getY();
         
         if(distX != 0 && distX > 0){
             this.creature.move(EAST);
             return true;
         }else if(distX != 0 && distX < 0){
             this.creature.move(WEST);
             return true;
         }
         
         if(distY != 0 && distY > 0){
             this.creature.move(SOUTH);
             return true;
         }else if(distY != 0 && distY < 0){
             this.creature.move(NORTH);
             return true;
         }
         
         return false;
     
    }
    
    /*
     * GOAL: move towards the middle
     * RETURN: true if walking was succesful, else false (f.ex. position == coordinates)
     */
    public boolean moveToMiddle(){
        int middleX = this.creature.world.getWidth()/2;
        int middleY = this.creature.world.getHeight()/2;
        
        return moveTowards(middleX, middleY);
    }
    
    /*
     * NOT SURE WHAT THIS FUNCTION DOES. 
     * WHY WOULD THE CLASS BE OF INTEREST?
     */
     public String getName () {
        return this.getClass().toString();
    }
    
    /*
     * PAUSE FUNCTION
     * --> delete in final version
     */
    public void pause(int i){
        try{
            Thread.sleep(i);
        }catch(Exception e){}
    }
}
