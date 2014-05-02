import greenfoot.*;


public class Wall extends Actor {

    @Override
    protected void addedToWorld(World world) {
        GreenfootImage bg = this.getImage();
        
        bg.scale(TheWorld.CELL_SIZE, TheWorld.CELL_SIZE);
        this.setImage(bg);
        
        //SHOW X AND Y 
        bg.drawString(this.getX()+":"+this.getY(), this.getX()+3, this.getY()+10);
    }
}
