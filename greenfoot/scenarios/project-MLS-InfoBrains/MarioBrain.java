/**
 * Write a description of class MarioBrain here.
 * 
 * @author (Jan Philipp Reising) 
 * @version (v1.0)
 */
public class MarioBrain extends JBrains 
{
    int energy;
    public void init(){
        initiate("mario.png");
        energy = this.creature.getEnergy();
    }
    
    public void think(){
    //System.out.println("\f");
    this.creature.say(this.creature.getEnergy()+"");
    
        substractHalfLife();
        explore(0);
    
        if(field[this.creature.getX()][this.creature.getY()]==2){
            field[this.creature.getX()][this.creature.getY()] = -2;
            this.creature.eat();
            this.creature.say("yam, yam");
            return;
        }
        
        
        //evaluating exploring
        int bestRange = scoreExploring();
        explore(bestRange);
        printMap();
        
        //evaluating giving birth
        int bestBirth = scoreBirth();
        
        //evaluating moving
        int bestDirection = scoreMoving();
        
        //move or birth
        if(bestBirth > bestDirection){
            this.creature.giveBirth();
        }else{
            if(bestDirection!= 4)   this.creature.move(bestDirection);
        }
        
        
        
        return;
    }
    
    public int scoreBirth(){
        int score = 0;
        
        //number of flowers in close proximity
        int flowersClose = numberOfThings(this.creature.getX(), this.creature.getY(), 1, PLANT);
        score = score + flowersClose * 6;
        
        //number of known flowers overall
        int flowersOverall = numberOfThings(this.creature.getX(), this.creature.getY(), field.length/2, PLANT);
        //score = score + flowersOverall *3;
        
        //number of enemys
        int enemies = numberOfThings(this.creature.getX(), this.creature.getY(), 2, ANIMAL);
        score = score - enemies * 3;
        
        //health
        double howHealthy = 1 - this.creature.getEnergy() / this.creature.getMaxEnergy();
        score = score - (int)(howHealthy * 80);
        
        this.creature.say("flowersClose * 10: "+flowersClose * 10+"-- flowersOverall *5:"+flowersOverall *5);
        
        return score;
        
    }
    
    public int scoreExploring(){
        //idea: -value that you need to spend + value for each new field
        
        int valueOfAFogField = 2;   //The value you get when you uncover a new field
        int extraCosts = 0;         //configure how valuable it is to you
        
        //check for each range
        int result = 0, resultRange = 0;
        int fogOfWarFields, costs, fogOfWarBonus, balance;
        for(int i=maxView;i>=0;i--){
            fogOfWarFields = 0;
            
            fogOfWarFields = numberOfThings(this.creature.getX(), this.creature.getY(), i, -1);
            fogOfWarBonus = fogOfWarFields * valueOfAFogField;
            costs = (viewCost+extraCosts) * i;
            
            balance =  fogOfWarBonus - costs;
            
            //how many plants do you see
            int flowersClose = numberOfThings(this.creature.getX(), this.creature.getY(), 1, PLANT);
            balance = balance - flowersClose * 6;
            
            //if more bonus than cost and better than range before
            if(result < balance){
                result = balance;
                resultRange = i;
            }
        }
        System.out.println("I decided to view "+resultRange+" fields at a cost of "+result);
        return resultRange;
    }
    
    public int scoreMoving(){
        
        int actX = this.creature.getX();
        int actY = this.creature.getY();
        
        int[] scores = new int[5];
        scores[4] = positionScoring(actX,actY);  //myPosition
        scores[NORTH] = positionScoring(actX,actY-1);  //NORTH
        scores[SOUTH] = positionScoring(actX,actY+1);  //SOUTH
        scores[WEST] = positionScoring(actX-1,actY);  //WEST
        scores[EAST] = positionScoring(actX+1,actY);  //EAST
        
       // System.out.println("myPosition: "+scores[4]);
       //System.out.println("north: "+scores[NORTH]);
       // System.out.println("south: "+scores[SOUTH]);
       // System.out.println("west: "+scores[WEST]);
       // System.out.println("east: "+scores[EAST]);
        
        //Maximum score
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
     * calculation how good the position is
     */
    public int positionScoring(int x, int y){

        //calculation of score
        int score = 0;
        int pos = field[x][y];

        //distance to center
        int distanceToCenter = distance(x,y,this.creature.world.getWidth()/2, this.creature.world.getHeight()/2);
        int myDistanceToCenter = distance(0,0,this.creature.world.getWidth()/2, this.creature.world.getHeight()/2);
        //System.out.println("myDistance: "+myDistanceToCenter+"newDistance: "+distanceToCenter);
        if(myDistanceToCenter > distanceToCenter){
           score += 10;    //is this a good value?
        }
        //distance to food (flower or dead animal)
        int[] nearestPlant = findNearestPlant(x,y);
        int dist = Integer.MAX_VALUE;
        if(nearestPlant != null){   //exclude that there is no plant nearby
            dist = distance(x,y,nearestPlant[0], nearestPlant[1]);
        }
        if(dist==0){
            score += 50;
        }else if(dist<2){
            score += 10;
        }else if(dist<3){
            score +=5;
        }else{}
        
        //number of flowers in near proximity
        int noOfPlants = numberOfThings(x,y,1,2);
        score += noOfPlants * 5;    //is this a good value?
        
        //distance to your kind
        
        //distance to danger
        
        //fog of war still on
        if(pos == -1){
            score -= 50;    // is this a good value?
        }
        
        //occupied by s.th.
        if(pos == 0){ //a stone is very bad
            score -= 1000;
        }else if(pos == 1){
            score -= 1000;  //water is bad
        }
        
        //subtract value for moving
        score -= this.creature.getMoveCost();
        
        return score;
    }
}
