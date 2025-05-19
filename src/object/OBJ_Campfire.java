package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Campfire extends Entity{

    int spriteCounter = 0;
	int spriteNum = 1;
    
            
        public OBJ_Campfire(GamePanel gp){
            super(gp);
		//hitbox parameters
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 33;
		solidArea.height = 33;
        name = "Campfire";
        down1 = setup("/objects/firepit_1");
		down2 = setup("/objects/firepit_2");
		down3 = setup("/objects/firepit_3");
		down4 = setup("/objects/firepit_4");

        collision = true;
        
    }
	
}
