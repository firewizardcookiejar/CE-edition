package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

		GamePanel gp;
		public BufferedImage up1, up2, up3, down1, down2, down3, down4, left1, left2, right1, right2;
		public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
		public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
		public int solidAreaDefaultX, solidAreaDefaultY;
		public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
		String dialogues[] = new String[20];
		public BufferedImage image, image2, image3, image4;
    	public boolean collision = false;


		// STATE		
		public int worldX, worldY;
		public boolean invincible = false;
		public boolean collisionOn = false;
		public String direction = "down";
		public int spriteNum = 1; 
		int dialogueIndex = 0;
		public boolean attacking = false;
		public boolean alive = true;
		public boolean dying = false;


		//COUNTER 
		public int spriteCounter = 0;
		public int actionLockCounter = 0;
		public int invincibleCounter;
		public int dyingCounter = 0;

		// CHARACTER STATUS
		public int maxLife;
		public int life;
		public int type; // 0 = player, 1 = npc, 2 = monster
		public int speed; 
    	public String name;


		public Entity(GamePanel gp){
			this.gp = gp;
		}
		public void setAction(){

			

		}
		public void speak(){
			 
		if (dialogueIndex >= dialogues.length || dialogues[dialogueIndex] == null){
    		dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
        case "up":
            direction = "down";
            break;
        case "down":
            direction = "up";
            break;
        case "left":
            direction = "right";
            break;
        case "right":
            direction = "left";
            break;
        }

		}
		public void update(){
			setAction();

			collisionOn = false;
			gp.cChecker.checkTile(this);
			gp.cChecker.checkObject(this, false);
			gp.cChecker.checkEntity(this, gp.npc);
			gp.cChecker.checkEntity(this, gp.monster);
			gp.cChecker.checkPlayer(this);
			boolean contactPlayer = gp.cChecker.checkPlayer(this);


			if(this.type == 2 && contactPlayer == true){
				if(gp.player.invincible == false){
					//do damage
					gp.playSE(7);
					gp.player.life -= 1;
					gp.player.invincible = true;
				}
			}
			//if false player can move
			if(collisionOn == false){
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
			if(invincible == true){
				invincibleCounter++;
				if(invincibleCounter > 40){
					invincible = false;
					invincibleCounter = 0;
				}
			}

		}
		public void draw(Graphics2D g2){

			BufferedImage image = null;
			int screenX = worldX - gp.player.worldX + gp.player.screenX; 
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

			switch(direction)	{
			case "up":
				if(spriteNum == 1){image = up1;}
				if(spriteNum == 2){image = up2;}
				break;
			case "down":
				if(spriteNum == 1){image = down1;}
				if(spriteNum == 2){image = down2;}
				break;
			case "left":
				if(spriteNum == 1){image = left1;}
				if(spriteNum == 2){image = left2;}		
				break;
			case "right":
				if(spriteNum == 1){image = right1;}
				if(spriteNum == 2){image = right2;}
				break;
			}
			if(invincible == true){
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			}
			if(dying == true){
				dyingAnimation(g2);
			}
                     g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

					 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					 g2.setColor(Color.blue);
					 g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, + solidArea.height);
                     
                }
		}
		public void dyingAnimation(Graphics2D g2){
			
			dyingCounter++;

			int i = 5;

			if(dyingCounter <= i){changeAlpha(g2, 0f);}
			if(dyingCounter > i && dyingCounter <= i*2){changeAlpha(g2, 1f);}
			if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2, 0f);}
			if(dyingCounter > i*3 && dyingCounter <= i*4){changeAlpha(g2, 1f);}
			if(dyingCounter > i*4 && dyingCounter <= i*5){changeAlpha(g2, 0f);}
			if(dyingCounter > i*5 && dyingCounter <= i*6){changeAlpha(g2, 1f);}
			if(dyingCounter > i*6 && dyingCounter <= i*7){changeAlpha(g2, 0f);}
			if(dyingCounter > i*7 && dyingCounter <= i*8){changeAlpha(g2, 1f);}
			if(dyingCounter > i*8){
				dying = false;
				alive = false;
			}
			
			}
		
		public void changeAlpha(Graphics2D g2, float alphaValue){
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));

		}
		public BufferedImage setup(String imagePath, int width, int height){

		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream("" + imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);

		}catch(IOException e){
			e.printStackTrace();

		}
		return image;
	}


		

		
}
