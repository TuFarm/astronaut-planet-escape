package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;

import static utilz.Constants.ObjectConstants.*;
import gamestate.Playing;
import levels.Level;
import utilz.LoadSave;

public class ObjectManager {
	
	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage spikeImg;
	private ArrayList<BatteryItems> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Die1> die1s;


	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
		
	}
	public void checkSpikesTouched(Player p) {
		for(Die1 s : die1s)
			if(s.getHitbox().intersects(p.getHitbox()))
				p.kill();
	}
	
	
	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for(BatteryItems p :potions)
		if(p.isActive()) {
			if(hitbox.intersects(p.getHitbox())) {
				p.setActive(false);
			applyEffectToPlayer(p);
		}
	}
}
	public void applyEffectToPlayer(BatteryItems p) {
		if(p.getObjType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		else
			playing.getPlayer().changePower(BLUE_POTION_VALUE);

		
	}
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		
		for(GameContainer gc : containers)
			if(gc.isActive() && !gc.doAnimation 	) {
				if(gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if(gc.getObjType() == BARREL)
						type = 1;
					potions.add(new BatteryItems((int)(gc.getHitbox().x + gc.getHitbox().width / 2), (int)(gc.getHitbox().y - gc.getHitbox().height/2), type));
					return;
				}
				
				
			}
		
	}
	
	public void loadObject(Level newLevel) {
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		die1s = newLevel.getSpikes();
		
	}


	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.BATTERY_ATLAS);
		potionImgs = new BufferedImage[2][7];
		
		for(int j = 0; j < potionImgs.length; j++)
			for(int i =0; i< potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(12*i, 16*j, 12, 16);
		
		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		
		for(int j = 0; j < containerImgs.length; j++)
			for(int i =0; i< containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(40*i, 30*j, 40, 30);
		
		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
	}
	
	public void update() {
		for(BatteryItems p : potions)
			if(p.isActive())
				p.update();
		for(GameContainer gc : containers)
			if(gc.isActive())
				gc.update();
	}
	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawTrap(g, xLvlOffset);

	}


	private void drawTrap(Graphics g, int xLvlOffset) {
		for(Die1 s : die1s)
			g.drawImage(spikeImg, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()),DIE1_WIDTH, DIE1_HEIGHT, null);
	}



	private void drawContainers(Graphics g, int xLvlOffset) {
		for(GameContainer gc : containers)
			if(gc.isActive()) {
				int type = 0;
				if(gc.getObjType() == BARREL)
					type = 1;
				
					
					
					
					
					g.drawImage(containerImgs[type][gc.getAniIndex()], 
							(int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int)(gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
				}
					
			}


	private void drawPotions(Graphics g, int xLvlOffset) {
		for(BatteryItems p : potions)
			if(p.isActive()) {
				int type = 0;
				if(p.getObjType() == RED_POTION)
					type = 1;
				g.drawImage(potionImgs[type][p.getAniIndex()], (int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int)(p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);
					
			}
				
				
	}
	
	public void resetAllObjects() {
		
		//System.out.println("Array size: " + potions.size() + " + " + containers.size() );
		loadObject(playing.getLevelManager().getCurrentLevel());
		
		for(BatteryItems p : potions)
			p.reset();
			
			for(GameContainer gc : containers)
				gc.reset();
		//	System.out.println("Array size after: " + potions.size() + " + " + containers.size() );
	}

		
	}

	
	


