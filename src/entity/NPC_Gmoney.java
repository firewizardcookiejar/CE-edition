package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Gmoney extends Entity {

    public NPC_Gmoney(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();

        //hit box parameters
        solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

    } public void getImage(){
		
		up1 = setup("/npc/gmoney_up_1",gp.tileSize, gp.tileSize);
		up2 = setup("/npc/gmoney_up_3",gp.tileSize, gp.tileSize);
        up3 = setup("/npc/gmoney_up_2",gp.tileSize, gp.tileSize);
		down1 = setup("/npc/gmoney_down_1",gp.tileSize, gp.tileSize);
		down2 = setup("/npc/gmoney_down_3",gp.tileSize, gp.tileSize);
	
		left1 = setup("/npc/gmoney_left_1",gp.tileSize, gp.tileSize);
		left2 = setup("/npc/gmoney_left_2",gp.tileSize, gp.tileSize);
		right1 = setup("/npc/gmoney_right_1",gp.tileSize, gp.tileSize);
		right2 = setup("/npc/gmoney_right_2",gp.tileSize, gp.tileSize);


		
    }
    public void setDialogue(){

        dialogues[0] = "Sup lil gup.";
        dialogues[1] = "I used to be an NBA player \nbut now I just play on my dad's street.";
        dialogues[2] = "Well later chode...";

    }
    public void setAction(){

        actionLockCounter ++;

        if(actionLockCounter == 90){
        Random random = new Random();
        int i = random.nextInt(100) + 1;

        if(i <= 25){
            direction = "up";
        }
        if(i > 25 && i <= 50){
            direction = "down";
        }
        if(i > 50 && i <= 75){
            direction = "left";
        }
        if(i > 75 && i <= 100){
            direction = "right";
        }
        actionLockCounter = 0;
    }
}
    public void speak(){
        //calls entity method
        //this one is for character specific actions on speak
        super.speak();

    }
}
