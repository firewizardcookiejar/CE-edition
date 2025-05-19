package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Mask extends Entity{
        
        public OBJ_Mask(GamePanel gp){
            super(gp);

        name = "Mask";
        down1 = setup("/objects/OBJ_Mask");
        collision = true;
    }
}
