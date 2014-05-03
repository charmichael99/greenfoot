/**
 * Write a description of class IndianaJones here.
 * 
 * @author (Zickmantel, Reising) 
 * @version (-1.0)
 * abgespeckte MarioClass
 */
public class IndianaJones extends AdventurerBrain 
{
    public void init(){
        initiate("indiana.png");
    }
    
    public void think(){
    //System.out.println("\f");
    
        //changing expiration date of information
        map.substractHalfLife();
        
        //explore current position (no costs)
        explore(0);
    
        /*
         * EATING
         * - flowers 
         * 
         * TO DO:
         * - dead animals
         */
        if(map.getObject(this.creature.getX(),this.creature.getY())==2){
            map.set(this.creature.getX(),this.creature.getY(),-2);
            this.creature.eat();
            this.creature.say("This should be in a museum!");
            
            //leave think method
            return;
        }
        
        /*
         * EXPLORING
         */
        
        explore(2);
        
        /*
         * MOVING
         */
        //evaluating moving
        int bestDirection = scoreMoving();
       
        //move
        if(bestDirection!= 4){                          //4 is coded as his current position
            this.creature.move(bestDirection);
        }else{
            this.creature.say("I like it here!");       //his current position is better than the rest
        }
        
        
        return;
    }
   
    /*
     * GOAL: calculate score for moving in each of the 4 directions and staying in place 
     */
    public int scoreMoving(){
        
        //creatures coordinates
        int actX = this.creature.getX();
        int actY = this.creature.getY();
        
        //matrix to save scores
        int[] scores = new int[5];
        
        //calculate scores and save them
        scores[4] = positionScoring(actX,actY);  //myPosition
        scores[NORTH] = positionScoring(actX,actY-1);  //NORTH
        scores[SOUTH] = positionScoring(actX,actY+1);  //SOUTH
        scores[WEST] = positionScoring(actX-1,actY);  //WEST
        scores[EAST] = positionScoring(actX+1,actY);  //EAST
        
        /*
        System.out.println("NORTH: "+scores[NORTH]);
        System.out.println("SOUTH: "+scores[SOUTH]);
        System.out.println("WEST: "+scores[WEST]);
        System.out.println("EAST: "+scores[EAST]);
        */
       
        //calculate Maximum score
        int max = 0;
        int maxPos = 0;
        for(int i=0;i<scores.length;i++){
            if(scores[i] > max){
                max = scores[i];
                maxPos = i;
            }
        }
            
            return maxPos;
    }
    
    /*
     * GOAL: calculate how good a position ist
     * 
     * TODO:
     * - VARY VALUES
     */
    public int positionScoring(int x, int y){

        int score = 0;
        int pos = map.getObject(x,y);  //new position
        
        //VALUE CONSTANTS
        int distanceToCenterValue = 10;         //middle
        
        int distanceToPlantNEAR = 50;           //food
        int distanceToPlantMIDDLE = 10;         //food
        int distanceToPlantFAR = 5;             //food
        
        int plantsInProximityBonus = 5;         //food density
        
        int fogOfWarPenalty = 50;               //fog penalty
        int stoneOrWaterPenalty = 1000;         //stone and water penalty

        /*
         * DISTANCE TO CENTER
         */
        //distance to center from new position and current position
        int distanceToCenter = map.distance(x,y,this.creature.world.getWidth()/2, this.creature.world.getHeight()/2);
        int myDistanceToCenter = map.distance(this.creature.getX(), this.creature.getY(),this.creature.world.getWidth()/2, this.creature.world.getHeight()/2);
        
        //calculate score: add constant if field is nearer
        if(myDistanceToCenter > distanceToCenter){
           score += distanceToCenterValue;
        }
        
        
        /*
         * DISTANCE TO FOOD
         * (flower || dead animal)
         */
        
        //calculate distance to nearest plant
        int[] nearestPlant = map.findNearestPlant(x,y);
        int dist = Integer.MAX_VALUE;
        if(nearestPlant != null){   //exclude if there is no plant nearby
            dist = map.distance(x,y,nearestPlant[0], nearestPlant[1]);
        }
        
        //score depending on the distance
        //in 3 categories: near, middle, far
        if(dist==0){
            score += distanceToPlantNEAR;
        }else if(dist<2){
            score += distanceToPlantMIDDLE;
        }else if(dist<3){
            score +=distanceToPlantFAR;
        }else{}
        
        
        /*
         * FOOD DENSITY
         */
        //number of flowers in near proximity numberOfThings(positionX, positionY, range, type)
        int noOfPlants = map.numberOfThings(x,y,1,2);
        //bonus for each plant in proximity
        score += noOfPlants * plantsInProximityBonus;
        
        /*
         * DISTANCE TO YOUR KIND
         */
        
        /*
         * DISTANCE TO DANGER
         */
        
        /*
         * FOG OF WAR STILL ON
         */
        if(pos == -1){
            score -= fogOfWarPenalty;
        }
        
        /*
         * OCCUPIED BY SOMETHINH
         * 
         * TO DO: 
         * - OTHER ANIMALS
         */
        if(pos == 0){ //a stone is very bad
            score -= stoneOrWaterPenalty;
        }else if(pos == 1){
            score -= stoneOrWaterPenalty;  //water is bad
        }
        
        /*
         * COSTS FOR MOVING
         * 
         * not very interesting at that stage but 
         * necessary when other scorings are added
         */
        score -= this.creature.getMoveCost();
        
        return score;
    }
}
