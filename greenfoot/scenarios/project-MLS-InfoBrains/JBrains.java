/**
 * This is the JBrain Class
 * You will find a brain that has the ability to
 * - move
 * - see something
 * - remember what s/he saw
 * 
 * @author (Jan Philipp Reising) 
 * 
 * @version (v2.0)
 * Die Karte entält Zeit-Informationen. Halbwertszeit von Pflanzen etc.
 * Steine: unendlich
 * Wasser: unendlich
 * Pflanzen: 2 || 3
 * Tiere: 1
 * leere Felder: schwierig; außen weniger, als innen
 * 
 * @version (v1.2)
 * Karte die Informationen über das Terrain enthalten
 * Wasser: 1
 * Steine: 0
 * Pflanzen: 2
 * Tiere: 3
 * leere Felder: -2
 * unbekannt: -1
 * 
 */
import greenfoot.*;

public class JBrains extends Brain 
{
 
    /*
     * It would have been nice to save the thing-object. There are certain flaws to that. For example
     * that there is no "empty"-thing element. Instead I'll use an multiple-int-array. 
     * Not optimal - i know!
     */
    //private Thing[][] field;
    public static int[][] field;
    public static int[][] halfLife;
    public static int turn = 0;
    public static int EMPTY = -2;
    public static int UNKNOWN = -1;
    public static int STONE = 0;
    public static int WATER = 1;
    public static int PLANT = 2;
    public static int ANIMAL = 3;
    
    public int maxView, viewCost;
    /*
     * 1: water
     * -1: unknown
     * -2: known but nothing
     */
    
    // constructor for initialization code.
    public void initiate (String image) {
        this.creature.setCreatureImage(image);
        maxView = this.creature.getMaxViewRange();
        viewCost = this.creature.getLookCost();
        //clear terminal
        System.out.print('\f'); 
        
        /*
         * Set map to remember encountered things
         */
        field = new int[this.creature.world.getWidth()][this.creature.world.getHeight()];
        halfLife= new int[this.creature.world.getWidth()][this.creature.world.getHeight()];
        
        //set fog of war
        for(int a=0;a<field.length;a++){
            for(int b=0; b< field[0].length;b++){
                if(a==0|b==0|a==field.length-1|b==field[0].length-1){
                    //implement: at the border is water
                    field[a][b] = WATER;
                    halfLife[a][b] = Integer.MAX_VALUE;
                }else{
                    field[a][b] = UNKNOWN;   //unkown
                     halfLife[a][b] = 0;
                }
            }
        }
        printMap();
        pause(1000);
    }
    
    public void substractHalfLife(){
        for(int a = 0; a<halfLife.length;a++){
            for(int b = 0;b<halfLife[0].length;b++){
                //substract halfLife
                if(halfLife[a][b]>0){
                    halfLife[a][b] = halfLife[a][b] - 1;
                }else{
                    field[a][b] = UNKNOWN;
                }
            }
        }
    }
    
    public void explore(int i){
        //clear everything in my view
        int lookX, lookY;
        for(int a=-i;a<=i;a++){
            for(int b=-i;b<=i;b++){
                lookX = this.creature.getX() + a;
                lookY = this.creature.getY() + b;
                if(lookX>0 && lookY>0){
                    //if(field[lookX][lookY]==-1) field[lookX][lookY] = -2;
                    field[lookX][lookY] = -2;
                    int dist = distance(a, b, this.creature.world.getWidth()/2, this.creature.world.getHeight()/2);
                    halfLife[lookX][lookY] = (dist/2) + 1;
                }
            }
        }
        //save things i explored
        Thing[] things = this.creature.lookAround(i);
        for(Thing t : things){
            int type = t.getType();
            field[t.getX()][t.getY()] = type;

            // create half-life table
            switch(type){
                //stone
                case 0:
                halfLife[t.getX()][t.getY()] = Integer.MAX_VALUE;
                break;
                
                //water
                case 1:
                halfLife[t.getX()][t.getY()] = Integer.MAX_VALUE;
                break;
                
                case 2: //Plant
                halfLife[t.getX()][t.getY()] = 4;
                break;
                
                case 3: //Animal
                halfLife[t.getX()][t.getY()] = 1;
                break;
            }
        }
        printMap();
    }
    
    public void moveToMiddle(){
        int middleX = this.creature.world.getWidth()/2;
        int middleY = this.creature.world.getHeight()/2;
        
        moveTowards(middleX, middleY);
    }
    
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
    
    public String getName () {
        return this.getClass().toString();
    }
    
    public void printMap(){
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
    
    public int distance(int meX, int meY, int x, int y){
        if(meX == 0) meX = this.creature.getX();
        if(meY == 0) meY = this.creature.getY();
        
        return Math.abs(x-meX)+Math.abs(y-meY);
    }

    public int numberOfThings(int x, int y, int range, int ThingNumber){
        int sum = 0;
        if(x == 0) x = this.creature.getX();
        if(y == 0) y = this.creature.getY();

        for(int a=x-range;a<=x+range;a++){
            for(int b=y-range;b<=y+range; b++){
                if(a > 0 && b > 0 && a< this.creature.world.getWidth() && b< this.creature.world.getHeight()){
                    if(field[a][b] == ThingNumber){
                        sum ++;
                    }
                }
            }
        }
        return sum;
    }
    
    /*
     * x && y == 0 --> nearest Plant to my current position
     */
    public int[] findNearestPlant(int x, int y){
        int minDistance = Integer.MAX_VALUE;
        int minX = -1;
        int minY = -1;
        for(int a=0;a<field.length;a++){
            for(int b=0;b<field[0].length;b++){
                if(field[a][b] == PLANT){
                    //calculate distance to flower 
                    if(distance(x,y,a,b)<minDistance){
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
    
    public void pause(int i){
        try{
            Thread.sleep(i);
        }catch(Exception e){}
    }
    
}
