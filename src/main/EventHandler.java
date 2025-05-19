package main;

import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp;
   EventRect eventRect[][];

   int previousEventX, previousEventY;
   boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow){
        eventRect[col][row] = new EventRect();
        eventRect[col][row].x = 23;
        eventRect[col][row].y = 23;
        eventRect[col][row].width = 2;
        eventRect[col][row].height = 2;
        eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
        eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

        col++;
        if(col == gp.maxWorldCol){
            col = 0;
            row ++;
        }
        }    
    }

    public void checkEvent(){

            //Check if player is > 1 tile away previous event
            int xDistance = Math.abs(gp.player.worldX - previousEventX);
            int yDistance = Math.abs(gp.player.worldY - previousEventY);
            int distance = Math.max(xDistance, yDistance);
            if(distance > gp.tileSize){
                canTouchEvent = true;
            }
            if(canTouchEvent == true){
            if(hit(8,15,"any") == true){
                // event happens
                damagePit(8, 15,gp.dialogueState);
            }
            if(hit(8,25,"any") == true || hit(9,25,"left") == true || hit(8, 26,"up") == true || hit(7,25,"right")){
                healingPool(8, 24,gp.dialogueState);
            }
            if(hit(8,18,"up") == true){
                // event happens
                teleport(8, 18,gp.dialogueState);
                System.out.println("working");
            }    
            }
           
    }
    public boolean hit(int col, int row, String reqDirection){
        boolean hit = false;

        //finds event and player location
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;

        //do thing if rectangles intersect, || can be used if you want player to face event or any direction works
        if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
                
            }
        }

        //reset data after checking for intersect
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;


        return hit;
    }
    public void teleport(int col, int row, int gameState){
        
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Teleport!";
        gp.player.worldX = gp.tileSize*8;
        gp.player.worldY = gp.tileSize*27;
        eventRect[col][row].eventDone = true;
    }
    public void damagePit(int col, int row, int gameState){
        
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit!";
        gp.player.life -= 1;
        eventRect[col][row].eventDone = true;
        System.out.println(gp.player.life);
        canTouchEvent = false;
    }
    public void healingPool(int col, int row, int gameState){
        if(gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You warm yourself by the fire";
            gp.player.life = gp.player.maxLife;
            gp.keyH.enterPressed = false;
           eventRect[8][24].eventDone = true;
           eventRect[9][25].eventDone = true;
           eventRect[8][26].eventDone = true;
           eventRect[7][25].eventDone = true;
        } 
        
    }
}
