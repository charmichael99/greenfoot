/**
 * @author (Zickmantel, Reising) 
 * @version (1.0)
 * 
 * TODO:
 * - how about a half-life array where every type of object a half life is assigned (--> quicker changing possible)
 */
public class Map  
{
    public static int[][] field;    //field that saves explored objects
    public static int[][] halfLife; //field that saves when objects where explored
    
    //map-constants for possible objects
    public static int EMPTY = -2;
    public static int UNKNOWN = -1;
    public static int STONE = 0;
    public static int WATER = 1;
    public static int PLANT = 2;
    public static int ANIMAL = 3;
    
    //Constructer
    public Map(Creature c){
        //create field in correct dimension
        field = new int[c.world.getWidth()][c.world.getHeight()];
        //create half life field accordingly
        halfLife= new int[c.world.getWidth()][c.world.getHeight()];
        
         //set fog of war aka. mark all fields unknown
        for(int a=0;a<field.length;a++){            //iterate over x
            for(int b=0; b< field[0].length;b++){   //and y
                if(a==0|b==0|a==field.length-1|b==field[0].length-1){ //recognize water aka. border of map
                    //implement: at the border is water
                    field[a][b] = WATER;
                    halfLife[a][b] = Integer.MAX_VALUE;
                }else{
                    field[a][b] = UNKNOWN;   //mark everything except water unknown
                     halfLife[a][b] = 0;
                }
            }
        }
    }
    
    /*
     * UPDATE MAP WITH NEW INFORMATION
     * 1) delete outdated information
     * 2) save new information
     */
    public void update(Creature c, Thing[] things, int range){
         /*
          * before we can save the new data
          * we have to delete the current date in the view-range
          * else 'outdated' information will persist
          */
        int lookX, lookY;   //variable that defines where we are currently deleting
        
        for(int a=-range;a<=range;a++){         //iterate over the rectangle (-x,-y)(+x,+y)
            for(int b=-range;b<=range;b++){     //relative to creature who scanned
                
                //obtain current position to delete
                lookX = c.getX() + a;
                lookY = c.getY() + b;
                
                //mark all fields in range unknown aka. reset fog of war
                /*
                 * TO DO:
                 * SHOULDN'T IT ALSO CHECK IF lookX AND lookY ARE WITHIN 
                 * THE MAP ON THE right AND bottom SIDE?
                 */
                if(lookX>0 && lookY>0){ //check if we are within the map
                    //if(field[lookX][lookY]==-1) field[lookX][lookY] = -2; //IST DAS KUNST ODER KANN DAS WEG?
                    
                    field[lookX][lookY] = -2;   //set EMPTY
                    
                    /*
                     * TO DO:
                     * IS THAT A GOOD WAY TO CALCULATE THE HALF LIFE?
                     * MY IDEA WAS THAT INFORMATION NEARER TO THE CENTER IS MORE UNCERTAIN
                     * 
                     * TO CLARIFY:
                     * THIS IS THE HALF LIFE FOR 'EMPTY FIELD'. SO BASICALLY THE FOG creeps BACK ^^
                     */
                    
                    //reset half life
                    int dist = distance(a, b, c.world.getWidth()/2, c.world.getHeight()/2); //calculate distance to center
                    halfLife[lookX][lookY] = (dist/2) + 1;  //calculate halfLife in dependence on distance to center
                }
                
            }
        }
        
        /*
         * NOW WE CAN UPDATE THE NEW DATA
         */
        for(Thing t : things){
            int type = t.getType();
            field[t.getX()][t.getY()] = type;

            /*
             * TO DO:
             * VARY THE VALUES OF HALF LIFE FOR OBJECTS (mostly flowers and animals)
             */
            
            // create half-life table
            switch(type){
                //stone
                case 0:
                halfLife[t.getX()][t.getY()] = Integer.MAX_VALUE;   //maximum half life for stones
                break;
                
                //water
                case 1:
                halfLife[t.getX()][t.getY()] = Integer.MAX_VALUE;   //maximum half life for water
                break;
                
                case 2: //Plant
                halfLife[t.getX()][t.getY()] = 4;                   //4 rounds half life for plants               
                break;
                
                case 3: //Animal
                halfLife[t.getX()][t.getY()] = 1;                   //1 round half life for animals
                break;
            }
        }
        
    }
    
    /*
     * GETTER FUNCTIONS
     */
    public int getObject(int x, int y){
        return field[x][y];
    }
    
    public int getHalfLife(int x, int y){
        return halfLife[x][y];
    }
    
    /*
     * SETTER FUNCTION
     * 
     * TO DO:
     * what half life for 
     */
    public void set(int x, int y, int value){
        field[x][y] = value;
        halfLife[x][y] = 3;             //TO DO: !!!! NEEDS TO BE CHANGED
    }
    
    /*
     * GOAL: prevent outdated information from persisting in the map
     *       --> 'expiration distance/time' is substracted by one
     *       --> shall be called once a round
     */
    public void substractHalfLife(){
        //iterate over every expiration-field
        for(int a = 0; a<halfLife.length;a++){
            for(int b = 0;b<halfLife[0].length;b++){
               
                if(halfLife[a][b]>0){ //substract halfLife if expiration isn't reached
                    halfLife[a][b] = halfLife[a][b] - 1;
                }else{                //else revive fog of war
                    field[a][b] = UNKNOWN;
                }
            }
        }
    }
    
    /*
     * GOAL: visualize map
     *       --> two maps are created
     *       1) map of known objects
     *       2) their expiration round (countdown)
     * 
     * OBJECTS:
     * fog = x
     * empty = -
     * water = ?
     * animals&plants = 1-9
     * 
     * EXPIRATION:
     * - countdown to expiration round
     * - 9 equals a very large number
     */
    public void print(){
        System.out.print('\f');
        for(int b=0;b<field[0].length;b++){
            for(int a=0;a<field.length;a++){
                System.out.print(" ");
                //water
                if(field[a][b] == UNKNOWN){
                    System.out.print("x");              
                }else if(field[a][b] == EMPTY){
                    //didnt find anything
                    System.out.print("-");              
                }else{
                    System.out.print(field[a][b]);
                }
            }
            System.out.println("");
        }
        System.out.println("-------------------------------------------------");
        for(int b=0;b<field[0].length;b++){
            for(int a=0;a<field.length;a++){
                System.out.print(" ");
                int fieldHalfLife = halfLife[a][b];    
                if(fieldHalfLife<10)    System.out.print(fieldHalfLife);
                else  System.out.print("9");
            }
            System.out.println("");
        } 
        
        System.out.println("-------------------------------------------------");
    }
    
    /*
     * GOAL: calculate distance from one coordinate to another
     * 
     */
    public int distance(int meX, int meY, int x, int y){
        return Math.abs(x-meX)+Math.abs(y-meY);
    }
    
    /*
     * GOAL: calculate number of things of a 
     *       special type (defined by thingNumer) in range from coordinates
     *       
     * RETURNS: int
     */
    public int numberOfThings(int x, int y, int range, int ThingNumber){
        int sum = 0;

        //iterate over range
        for(int a=x-range;a<=x+range;a++){
            for(int b=y-range;b<=y+range; b++){
                /*
                 * CAREFUL CODE HERE CHANGED WITHOUT TESTING:
                 * PROBLEM: this.creature.world used --> map doesnt know the creature
                 * SOLUTION: dimensions of field variable
                 * 
                 * OLD CODE: if(a > 0 && b > 0 && a< this.creature.world.getWidth() && b< this.creature.world.getHeight()){
                 * NEW CODE: see below
                 */
                if(a > 0 && b > 0 && a< field.length && b< field[0].length){
                    if(field[a][b] == ThingNumber){
                        sum ++;
                    }
                }
            }
        }
        return sum;
    }
    
    /*
     * GOAL: find coordinates of nearest Plant
     * INPUT: animals coordinates (x,y) --> (this.creature.getX(). this.creature.getY())
     * RETURNS: 2D-int-array [x,y]
     *          null if there are no flowers
     */
    public int[] findNearestPlant(int x, int y){
        int minDistance = Integer.MAX_VALUE;    //set minimal distance to biggest value possible
        int minX = -1;                          //set coordinates
        int minY = -1;                          //to impossible value
        
        //iterate over all fields
        for(int a=0;a<field.length;a++){
            for(int b=0;b<field[0].length;b++){
                
                if(field[a][b] == PLANT){   
                    //calculate distance to flower 
                    if(distance(x,y,a,b)<minDistance){  //set new plant nearer if distance < smallest distance until then
                        minDistance = distance(x,y,a,b);
                        minX = a;
                        minY = b;
                    }
                }
            }
        }
        if(minX < 0) return null;

        return new int[] {minX,minY};
    }
}
