package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{
	
	
	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int idleCounter = 0;
	
	

	public Player(GamePanel gp, KeyHandler keyH)	{
		
		super(gp);
		this.keyH = keyH;

		screenX = gp.screenWidth/2 - (gp.tileSize/2); //draws character in center
		screenY = gp.screenHeight/2 - (gp.tileSize/2);

		//hitbox parameters
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

		attackArea.width = 36;
		attackArea.height = 36;

		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		
		
	}
	public void setDefaultValues()		{
		
		worldX = gp.tileSize * 8;
		worldY = gp.tileSize * 23;
		speed = 4;
		direction = "down";

		//PLAYER STATUS
		maxLife = 6;
		life = maxLife;
		
	}
	public void getPlayerImage()	{
		
	    up1 = setup("/player/nasus_up1",gp.tileSize, gp.tileSize);
		up2 = setup("/player/nasus_up2",gp.tileSize, gp.tileSize);	
		up3 = setup("/player/nasus_up3",gp.tileSize, gp.tileSize);
		down1 = setup("/player/nasus_down1",gp.tileSize, gp.tileSize);
		down2 = setup("/player/nasus_down2",gp.tileSize, gp.tileSize);
		down3 = setup("/player/nasus_down3",gp.tileSize, gp.tileSize);
		left1 = setup("/player/nasus_left1",gp.tileSize, gp.tileSize);
		left2 = setup("/player/nasus_left2",gp.tileSize, gp.tileSize);
		right1 = setup("/player/nasus_right1",gp.tileSize, gp.tileSize);
		right2 = setup("/player/nasus_right2",gp.tileSize, gp.tileSize);
	
	}

	public void getPlayerAttackImage()	{

		attackUp1 = setup("/player/nasus_attack_up1", gp.tileSize, gp.tileSize*2);
		attackUp2 = setup("/player/nasus_attack_up2", gp.tileSize, gp.tileSize*2);
		attackDown1 = setup("/player/nasus_attack_down1",gp.tileSize, gp.tileSize*2);
		attackDown2 = setup("/player/nasus_attack_down2", gp.tileSize, gp.tileSize*2);
		attackLeft1 = setup("/player/nasus_attack_left1",gp.tileSize*2, gp.tileSize);
		attackLeft2 = setup("/player/nasus_attack_left2",gp.tileSize*2, gp.tileSize);
		attackRight1 = setup("/player/nasus_attack_right1",gp.tileSize*2, gp.tileSize);
		attackRight2 = setup("/player/nasus_attack_right2",gp.tileSize*2, gp.tileSize);
	}
	
	public void update()	{

			if(attacking == true){
				attacking();
			}
			
			else if(keyH.upPressed == true || keyH.downPressed == true || //idle animation currently none 
					keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true)	{
				if(keyH.upPressed == true) {
					direction = "up";
				}
				else if(keyH.downPressed == true) {
					direction = "down";
				}
				else if(keyH.leftPressed == true) {
					direction = "left";
				}
				else if(keyH.rightPressed == true) {
					direction = "right";
				}

				//tile collssion
				collisionOn = false;
				gp.cChecker.checkTile(this);

				//check  obj collision
				int objIndex = gp.cChecker.checkObject(this, true);
				pickUpObject(objIndex);

				//check npc collision
				int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
				interactNPC(npcIndex);

				//check monster collision
				int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
				contactMonster(monsterIndex);


				//check event
				gp.eHandler.checkEvent();
 
				//IF false CAN move
				if(collisionOn == false && keyH.enterPressed == false){
					
					switch(direction){
						case "up":
						worldY -= speed;
							break;

						case "down":
						worldY += speed;
							break;

						case "left":
						worldX -= speed;
							break;

						case "right":
							worldX += speed; 
							break;
							
					}
				}
				gp.keyH.enterPressed = false;
				
				spriteCounter++;  //how fast the animation is for player sprite
			
				if(spriteCounter > 10)	{ 

					if(spriteNum == 1)	{
						spriteNum = 2;
					}
					else if(spriteNum == 2)	{
						spriteNum = 1;
					}
					spriteCounter = 0;
				}		
		}

			else{
				idleCounter++;

				if(idleCounter == 90){
					spriteNum = 2;
					idleCounter = 0;

				}
				
			}

			//Invincibility timer
			if(invincible == true){
				invincibleCounter++;
				if(invincibleCounter > 60){
					invincible = false;
					invincibleCounter = 0;
				}
			}		
		}
	public void attacking(){
			
			spriteCounter++;

			if(spriteCounter <= 5){
				spriteNum = 1;
			}
			if(spriteCounter > 5 && spriteCounter <= 25){
				spriteNum = 2;

				// Save current worldx,worly, solidarea
				int currentWorldX = worldX;
				int currentWorldY = worldY;
				int solidAreaWidth = solidArea.width;
				int solidAreaHeight = solidArea.height;

				//adjust players worldx/y for attackarea
				switch(direction){
				case "up": worldY -= attackArea.height; break;
				case "down": worldY += attackArea.height; break;
				case "left": worldX -= attackArea.width; break;
				case "right": worldX += attackArea.width; break;
				}
				// attackArea becomes solidArea
				solidArea.width = attackArea.width;
				solidArea.height = attackArea.height;
				// Check monster collision with the new worldX/Y and solid area
				int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
				damageMonster(monsterIndex);

				//Restores original ddata
				worldX = currentWorldX;
				worldY = currentWorldY;
				solidArea.width = solidAreaWidth;
				solidArea.height = solidAreaHeight;


			}
			if(spriteCounter > 25){
				spriteNum = 1;
				spriteCounter = 0;
				attacking = false;
			}
		}
	public void pickUpObject(int i){

			if(i != 727){
			}
		}
	public void interactNPC(int i){

			if(gp.keyH.enterPressed == true){
				if(i != 727){

					gp.gameState = gp.dialogueState;
					gp.npc[i].speak();					
			}
			else{
					attacking = true;
					gp.playSE(8);	
					}
			}	
		}
	public void contactMonster(int i){
			
			if(i !=727){

				if(invincible == false){
					gp.playSE(7);
				life -= 1;
				invincible = true;
				}
				
			}

		}
	public void damageMonster(int i ){
		if(i != 727){

			if(gp.monster[i].invincible == false){

				gp.playSE(7);
				gp.monster[i].life -=1;
				gp.monster[i].invincible = true;

				if(gp.monster[i].life <= 0 ){
					gp.monster[i].dying = true;
					gp.playSE(9);
				}
			}
		}
	}

	public void draw(Graphics2D g2)	{
			
//			g2.setColor(Color.yellow);
			
//			g2.fillRect(x, y, gp.tileSize, gp.tileSize);
			
			BufferedImage image = null;
			int tempScreenX = screenX;
			int tempScreenY = screenY;
			
			switch(direction)	{
			case "up":
				if(attacking == false){
					if(spriteNum == 1){image = up1;}
				if(spriteNum == 2){image = up3;}
				if(spriteNum == 3){	image = up2;}
				}
				if(attacking == true){
					tempScreenY = screenY - gp.tileSize;
					if(spriteNum == 1){image = attackUp1;}
				if(spriteNum == 2){image = attackUp2;}
				if(spriteNum == 3){	image = attackUp2;}
				}		
				break;
			case "down":
				if(attacking == false){
					if(spriteNum == 1){image = down1;}
				if(spriteNum == 2){image = down3;}
				if(spriteNum == 3){	image = down2;}
				}
				if(attacking == true){
					if(spriteNum == 1){image = attackDown1;}
				if(spriteNum == 2){image = attackDown2;}
				if(spriteNum == 3){	image = attackDown2;}
				}		
				break;
			case "left":
				if(attacking == false){
					if(spriteNum == 1){image = left1;}
				if(spriteNum == 2){image = left2;}
				if(spriteNum == 3){	image = left2;}
				}
				if(attacking == true){
					tempScreenX = screenX - gp.tileSize;
					if(spriteNum == 1){image = attackLeft1;}
				if(spriteNum == 2){image = attackLeft2;}
				if(spriteNum == 3){	image = attackLeft2;}
				}		
				break;
			case "right":
				if(attacking == false){
					if(spriteNum == 1){image = right1;}
				if(spriteNum == 2){image = right2;}
				if(spriteNum == 3){	image = right2;}
				}
				if(attacking == true){
					if(spriteNum == 1){image = attackRight1;}
				if(spriteNum == 2){image = attackRight2;}
				if(spriteNum == 3){	image = attackRight2;}
				}		
				break;
			}
			if(invincible == true){
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			}
			g2.drawImage(image,  tempScreenX,  tempScreenY, null);
			//Reset transparency effect
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			g2.setColor(Color.blue);	//used to show player hitbox uncomment to see :)
			g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, + solidArea.height);

			g2.setFont(new Font("Arial", Font.PLAIN, 26));
			g2.setColor(Color.white);
			g2.drawString("Invincible:" + invincibleCounter, 10, 400);
			
		}
		
}
