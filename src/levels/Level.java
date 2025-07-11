package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Slime;
import main.Game;
import objects.GameContainer;
import objects.BatteryItems;
import objects.Die1;
import utilz.HelpMethods;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetSlimes;
import static utilz.HelpMethods.GetPlayerSpawn;


public class Level {
	
	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Slime> slimes;
	private ArrayList<BatteryItems> potions;
	private ArrayList<Die1> die1s;
	private ArrayList<GameContainer> containers;
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;
	
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		createPotions();
		createContainers();
		calcLvlOffsets();
		calcPlayerSpawn();
		createSpikes();
		
	}
	
	private void createSpikes() {
		die1s = HelpMethods.GetDie1s(img);
		
	}

	private void createContainers() {
		containers = HelpMethods.GetContainers(img);
	}

	private void createPotions() {
		potions = HelpMethods.GetPotions(img);		
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
		
	}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
		
	}

	private void createEnemies() {
		slimes = GetSlimes(img);
		
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
		
	}
	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}
public int getLvlOffset() {
	return maxLvlOffsetX;
}
	public ArrayList<Slime> getSlimes(){
		return slimes;
	}
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<BatteryItems> getPotions(){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}
	public ArrayList<Die1> getSpikes(){
		return die1s;
	}
}
