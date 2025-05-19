package main;

import entity.NPC_Gmoney;
import monster.MON_BlueSlime;
import object.OBJ_Campfire;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Mask;

public class AssetSetter {

    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Campfire(gp);
        gp.obj[0].worldX = gp.tileSize * 8;
        gp.obj[0].worldY = gp.tileSize * 25;
    }
    public void setNPC(){

        gp.npc[0] = new NPC_Gmoney(gp);
        gp.npc[0].worldX = gp.tileSize * 8;
        gp.npc[0].worldY = gp.tileSize * 15;

    }
    public void setMonster(){

        gp.monster[0] = new MON_BlueSlime(gp);
        gp.monster[0].worldX = gp.tileSize * 24;
        gp.monster[0].worldY = gp.tileSize * 25;

        gp.monster[1] = new MON_BlueSlime(gp);
        gp.monster[1].worldX = gp.tileSize * 30;
        gp.monster[1].worldY = gp.tileSize * 24;


    }
}
