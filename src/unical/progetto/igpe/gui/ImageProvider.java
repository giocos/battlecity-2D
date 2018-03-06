package unical.progetto.igpe.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageProvider {

	private static Image pause;
	
	private final static int tile = 35;
	private final static int mapsXY = 175;
	private final static int stageX = 90;
	private final static int stageY = 20;
		
	// load
	private static Image battleCity;
	private static Image loading;

	// settings
	private static Image level;
	private static Image sound;
	private static Image easy;
	private static Image normal;
	private static Image hard;

	// menu
	private static Image title;
	private static Image cursorR;
	private static Image cursorL;
	
	// statistics
	private static Image Bar;
	private static Image ptsArrow;
	
	// maps
	private static ArrayList<Image> maps1P = new ArrayList<>();
	private static ArrayList<Image> maps2P = new ArrayList<>();
	
	private static Image arrowRight;
	private static Image arrowLeft;
	private static Image arrowRightsmall;
	private static Image arrowLeftsmall;
	private static Image selectMap;
		
	// powerUps
	private static Image grenade;
	private static Image helmet;
	private static Image shovel;
	private static Image star;
	private static Image tank;
	private static Image timer;
	
	private static Image helmetx;
	private static Image shovelx;
	private static Image timerx;

	// player1
	private static Image player1A;
	private static Image player1B;
	private static Image player1A_s1;
	private static Image player1B_s1;
	private static Image player1A_s2;
	private static Image player1B_s2;
	private static Image player1A_s3;
	private static Image player1B_s3;
	
	// player2
	private static Image player2A;
	private static Image player2B;
	private static Image player2A_s1;
	private static Image player2B_s1;
	private static Image player2A_s2;
	private static Image player2B_s2;
	private static Image player2A_s3;
	private static Image player2B_s3;

	// others
	private static Image flag;
	private static Image rocket;
	private static Image flagDestroyed;
	private static Image finalPanelGame;
	private static Image shield1;
	private static Image shield2;
	private static Image plus;
	private static Image less;
	private static Image selection;
	private static Image trash;
	
	// wall
	private static Image waterA;
	private static Image waterB;
	private static Image brick;
	private static Image steel;
	private static Image ice;
	private static Image tree;

	// enemies
	private static Image armorA;
	private static Image armorB;
	private static Image basicA;
	private static Image basicB;
	private static Image fastA;
	private static Image fastB;
	private static Image powerA;
	private static Image powerB;
	private static Image armorPowerUpA;
	private static Image armorPowerUpB;
	private static Image basicPowerUpA;
	private static Image basicPowerUpB;
	private static Image fastPowerUpA;
	private static Image fastPowerUpB;
	private static Image powerPowerUpA;
	private static Image powerPowerUpB;
	
	// appear
	private static Image appear1;
	private static Image appear2;
	private static Image appear3;
	private static Image appear4;
	
	// rocket explosion
	private static Image rocketExplosion1;
	private static Image rocketExplosion2;
	private static Image rocketExplosion3;
	
	//enemies explosion
	private static Image bigExplosion1;
	private static Image bigExplosion2;
	private static Image bigExplosion3;
	private static Image bigExplosion4;
	private static Image bigExplosion5;

	// statistics
	private static Image points100;
	private static Image points200;
	private static Image points300;
	private static Image points400;
	private static Image points500;
	
	//construction panel
	private static Image background1P;
	private static Image background1Px;
	private static Image background2P;
	
	private static Image iconEnemy;
	private static Image iconFlag;
	private static Image locked;
	
	private static Image stageComplete;
	private static Image gameOver;
	private static Image lives;
	
	private static Image keyboard;
	
	static {
		try {
	
			maps1P.add((ImageIO.read(new File("resource/map1.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map2.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map3.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map4.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map5.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map6.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map7.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map8.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map9.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map10.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map11.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map12.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map13.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map14.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map15.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map16.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map17.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map18.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map19.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map20.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map21.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map22.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map23.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps1P.add((ImageIO.read(new File("resource/map24.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map1_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map2_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map3_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map4_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map5_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map6_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map7_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map8_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map9_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map10_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map11_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map12_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map13_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map14_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map15_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map16_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map17_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map18_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map19_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map20_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map21_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map22_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map23_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			maps2P.add((ImageIO.read(new File("resource/map24_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
//			maps1P.add((ImageIO.read(new File("resource/mapBonus.png")).getScaledInstance(getMapsXY(), getMapsXY(),
//					java.awt.Image.SCALE_SMOOTH)));
//			maps2P.add((ImageIO.read(new File("resource/mapBonus_Multi.png")).getScaledInstance(getMapsXY(), getMapsXY(),
//			java.awt.Image.SCALE_SMOOTH)));
			
			setGameOver(ImageIO.read(new File("resource/gameOver.png")));
			setStageComplete(ImageIO.read(new File("resource/stageComplete.png")));
			setLocked((ImageIO.read(new File("resource/locked.png")).getScaledInstance(getMapsXY(), getMapsXY(),
					java.awt.Image.SCALE_SMOOTH)));
			setBackground1P((ImageIO.read(new File("resource/player1A.png")).getScaledInstance(600, 600,
					java.awt.Image.SCALE_SMOOTH)));
			setBackground1Px((ImageIO.read(new File("resource/player1A.png")).getScaledInstance(750, 750,
					java.awt.Image.SCALE_SMOOTH)));
			setBackground2P((ImageIO.read(new File("resource/player2A.png")).getScaledInstance(750, 750,
					java.awt.Image.SCALE_SMOOTH)));
			setTitle(ImageIO.read(new File("resource/title.png")));
			setCursorRight((ImageIO.read(new File("resource/cursorRight.gif")).getScaledInstance(38, 40,
					java.awt.Image.SCALE_SMOOTH)));
			setCursorLeft((ImageIO.read(new File("resource/cursorLeft.gif")).getScaledInstance(38, 40,
					java.awt.Image.SCALE_SMOOTH)));
			setSelectMap((ImageIO.read(new File("resource/selectMap.png")).getScaledInstance(186, 186,
					java.awt.Image.SCALE_SMOOTH)));
			setLoading(ImageIO.read(new File("resource/loading.gif")));
			setEasy((ImageIO.read(new File("resource/easy.png")).getScaledInstance(54, 20,
					java.awt.Image.SCALE_SMOOTH)));
			setNormal((ImageIO.read(new File("resource/normal.png")).getScaledInstance(72, 20,
					java.awt.Image.SCALE_SMOOTH)));
			setHard((ImageIO.read(new File("resource/hard.png")).getScaledInstance(52, 20,
					java.awt.Image.SCALE_SMOOTH)));
			setSound((ImageIO.read(new File("resource/sound.png")).getScaledInstance(78, 24,
					java.awt.Image.SCALE_SMOOTH)));
			setBattleCity(ImageIO.read(new File("resource/battleCity.png")));
			setPause((ImageIO.read(new File("resource/pause.png")).getScaledInstance(140, 50,
					java.awt.Image.SCALE_SMOOTH)));
			setGrenade((ImageIO.read(new File("resource/grenade.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setHelmet((ImageIO.read(new File("resource/helmet.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			
			//nn cancellare
			setHelmetx((ImageIO.read(new File("resource/helmet.png")).getScaledInstance(50, 50,
					java.awt.Image.SCALE_SMOOTH)));
			setTimerx((ImageIO.read(new File("resource/timer.png")).getScaledInstance(50, 50,
					java.awt.Image.SCALE_SMOOTH)));
			setShovelx((ImageIO.read(new File("resource/shovel.png")).getScaledInstance(50, 50,
					java.awt.Image.SCALE_SMOOTH)));
			
			setKeyboard((ImageIO.read(new File("resource/keyboard.png"))));
			
			setShovel((ImageIO.read(new File("resource/shovel.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setStar((ImageIO.read(new File("resource/star.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setTimer((ImageIO.read(new File("resource/timer.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setTank((ImageIO.read(new File("resource/tank.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1A((ImageIO.read(new File("resource/player1A.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1B((ImageIO.read(new File("resource/player1B.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1A_s1((ImageIO.read(new File("resource/player1A_s1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1B_s1((ImageIO.read(new File("resource/player1B_s1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1A_s2((ImageIO.read(new File("resource/player1A_s2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1B_s2((ImageIO.read(new File("resource/player1B_s2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1A_s3((ImageIO.read(new File("resource/player1A_s3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer1B_s3((ImageIO.read(new File("resource/player1B_s3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2A((ImageIO.read(new File("resource/player2A.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2B((ImageIO.read(new File("resource/player2B.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2A_s1((ImageIO.read(new File("resource/player2A_s1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2B_s1((ImageIO.read(new File("resource/player2B_s1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2A_s2((ImageIO.read(new File("resource/player2A_s2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2B_s2((ImageIO.read(new File("resource/player2B_s2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2A_s3((ImageIO.read(new File("resource/player2A_s3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlayer2B_s3((ImageIO.read(new File("resource/player2B_s3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setWaterA((ImageIO.read(new File("resource/waterA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setWaterB((ImageIO.read(new File("resource/waterB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setIce((ImageIO.read(new File("resource/ice.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBrick((ImageIO.read(new File("resource/brick.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setSteel((ImageIO.read(new File("resource/steel.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setTree((ImageIO.read(new File("resource/tree.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFlag((ImageIO.read(new File("resource/flag.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFlag_destroyed((ImageIO.read(new File("resource/flagDestroyed.png")).getScaledInstance(getTile(),
					getTile(), java.awt.Image.SCALE_SMOOTH)));
			setRocket((ImageIO.read(new File("resource/rocket.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setArmorA((ImageIO.read(new File("resource/armorA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPowerA((ImageIO.read(new File("resource/powerA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBasicA((ImageIO.read(new File("resource/basicA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFastA((ImageIO.read(new File("resource/fastA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setArmorB((ImageIO.read(new File("resource/armorB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPowerB((ImageIO.read(new File("resource/powerB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBasicB((ImageIO.read(new File("resource/basicB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFastB((ImageIO.read(new File("resource/fastB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setAppear1((ImageIO.read(new File("resource/appear1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setAppear2((ImageIO.read(new File("resource/appear2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setAppear3((ImageIO.read(new File("resource/appear3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setAppear4((ImageIO.read(new File("resource/appear4.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setRocketExplosion1((ImageIO.read(new File("resource/rocketExplosion1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setRocketExplosion2((ImageIO.read(new File("resource/rocketExplosion2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setRocketExplosion3((ImageIO.read(new File("resource/rocketExplosion3.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setShield1((ImageIO.read(new File("resource/shield1.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setShield2((ImageIO.read(new File("resource/shield2.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setArmorPowerUpA((ImageIO.read(new File("resource/armorPowerUpA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setArmorPowerUpB((ImageIO.read(new File("resource/armorPowerUpB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFastPowerUpA((ImageIO.read(new File("resource/fastPowerUpA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setFastPowerUpB((ImageIO.read(new File("resource/fastPowerUpB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBasicPowerUpA((ImageIO.read(new File("resource/basicPowerUpA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBasicPowerUpB((ImageIO.read(new File("resource/basicPowerUpB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPowerPowerUpA((ImageIO.read(new File("resource/powerPowerUpA.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPowerPowerUpB((ImageIO.read(new File("resource/powerPowerUpB.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setBigExplosion1((ImageIO.read(new File("resource/bigExplosion1.png")).getScaledInstance(getTile()*2, getTile()*2,
					java.awt.Image.SCALE_SMOOTH)));
			setBigExplosion2((ImageIO.read(new File("resource/bigExplosion2.png")).getScaledInstance(getTile()*2, getTile()*2,
					java.awt.Image.SCALE_SMOOTH)));
			setBigExplosion3((ImageIO.read(new File("resource/bigExplosion3.png")).getScaledInstance(getTile()*2, getTile()*2,
					java.awt.Image.SCALE_SMOOTH)));
			setBigExplosion4((ImageIO.read(new File("resource/bigExplosion4.png")).getScaledInstance(getTile()*2, getTile()*2,
					java.awt.Image.SCALE_SMOOTH)));
			setBigExplosion5((ImageIO.read(new File("resource/bigExplosion5.png")).getScaledInstance(getTile()*2, getTile()*2,
					java.awt.Image.SCALE_SMOOTH)));
			setPoints100((ImageIO.read(new File("resource/points100.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPoints200((ImageIO.read(new File("resource/points200.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPoints300((ImageIO.read(new File("resource/points300.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPoints400((ImageIO.read(new File("resource/points400.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPoints500((ImageIO.read(new File("resource/points500.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setPlus((ImageIO.read(new File("resource/plus.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setLess((ImageIO.read(new File("resource/less.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setTrash((ImageIO.read(new File("resource/trash.png")).getScaledInstance(getTile(), getTile(),
					java.awt.Image.SCALE_SMOOTH)));
			setSelection(ImageIO.read(new File("resource/selection.png")));

			setPtsArrow((ImageIO.read(new File("resource/ptsArrow.png")).getScaledInstance(40, 30,
					java.awt.Image.SCALE_SMOOTH)));
			setBar((ImageIO.read(new File("resource/bar.png")).getScaledInstance(190, 15,
				java.awt.Image.SCALE_SMOOTH)));
			setArrowLeft(ImageIO.read(new File("resource/arrowLeft.png")).getScaledInstance(45, 55,
					java.awt.Image.SCALE_SMOOTH));
			setArrowRight(ImageIO.read(new File("resource/arrowRight.png")).getScaledInstance(45, 55,
					java.awt.Image.SCALE_SMOOTH));
			setArrowLeftsmall(ImageIO.read(new File("resource/arrowLeft.png")).getScaledInstance(20, 30,
					java.awt.Image.SCALE_SMOOTH));
			setArrowRightsmall(ImageIO.read(new File("resource/arrowRight.png")).getScaledInstance(20, 30,
					java.awt.Image.SCALE_SMOOTH));
			setIconEnemy((ImageIO.read(new File("resource/iconenemy.png")).getScaledInstance(35, 35,
					java.awt.Image.SCALE_SMOOTH)));
			
			setIconFlag((ImageIO.read(new File("resource/iconflag.png")).getScaledInstance(35, 35,
					java.awt.Image.SCALE_SMOOTH)));
			
			setLives((ImageIO.read(new File("resource/lives.png")).getScaledInstance(20, 20,
					java.awt.Image.SCALE_SMOOTH)));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void setPlus(Image plus) {
		ImageProvider.plus = plus;
	}
	
	public static void setSelectMap(Image selectMap) {
		ImageProvider.selectMap = selectMap;
	}
	
	public static Image getSelectMap() {
		return selectMap;
	}

	public static Image getPlus() {
		return plus;
	}

	public static void setLess(Image less) {
		ImageProvider.less = less;
	}

	public static Image getLess() {
		return less;
	}
	
	public static int getTile() {
		return tile;
	}
	
	public static int getStageX() {
		return stageX;
	}
	
	public static int getStageY() {
		return stageY;
	}
	
	public static int getMapsXY() {
		return mapsXY;
	}
	
	public static Image getPlayer1A() {
		return player1A;
	}

	public static void setPlayer1A(Image player1A) {
		ImageProvider.player1A = player1A;
	}

	public static Image getPlayer1B() {
		return player1B;
	}

	public static void setPlayer1B(Image player1B) {
		ImageProvider.player1B = player1B;
	}

	public static Image getGrenade() {
		return grenade;
	}

	public static void setGrenade(Image grenade) {
		ImageProvider.grenade = grenade;
	}

	public static Image getHelmet() {
		return helmet;
	}

	public static void setHelmet(Image helmet) {
		ImageProvider.helmet = helmet;
	}

	public static Image getShovel() {
		return shovel;
	}

	public static void setShovel(Image shovel) {
		ImageProvider.shovel = shovel;
	}
	
	public static Image getHelmetx() {
		return helmetx;
	}

	public static void setHelmetx(Image helmetx) {
		ImageProvider.helmetx = helmetx;
	}
	
	public static void setTimerx(Image timerx) {
		ImageProvider.timerx = timerx;
	}
	
	public static Image getTimerx() {
		return timerx;
	}
	
	public static void setShovelx(Image shovelx) {
		ImageProvider.shovelx = shovelx;
	}
	
	public static Image getShovelx() {
		return shovelx;
	}

	public static Image getStar() {
		return star;
	}

	public static void setStar(Image star) {
		ImageProvider.star = star;
	}

	public static Image getTank() {
		return tank;
	}

	public static void setTank(Image tank) {
		ImageProvider.tank = tank;
	}

	public static Image getTimer() {
		return timer;
	}

	public static void setTimer(Image timer) {
		ImageProvider.timer = timer;
	}

	public static Image getTitle() {
		return title;
	}

	public static void setTitle(Image title) {
		ImageProvider.title = title;
	}
	
	public static Image getIce() {
		return ice;
	}

	public static void setIce(Image ice) {
		ImageProvider.ice = ice;
	}

	public static Image getSteel() {
		return steel;
	}

	public static void setSteel(Image steel) {
		ImageProvider.steel = steel;
	}

	public static Image getBrick() {
		return brick;
	}

	public static void setBrick(Image brick) {
		ImageProvider.brick = brick;
	}

	public static Image getWaterA() {
		return waterA;
	}

	public static void setWaterA(Image waterA) {
		ImageProvider.waterA = waterA;
	}

	public static Image getWaterB() {
		return waterB;
	}

	public static void setWaterB(Image waterB) {
		ImageProvider.waterB = waterB;
	}

	public static Image getTree() {
		return tree;
	}

	public static void setTree(Image tree) {
		ImageProvider.tree = tree;
	}

	public static Image getFlag() {
		return flag;
	}

	public static void setFlag(Image flag) {
		ImageProvider.flag = flag;
	}

	public static Image getRocket() {
		return rocket;
	}

	public static void setRocket(Image rocket) {
		ImageProvider.rocket = rocket;
	}

	public static Image getArmorA() {
		return armorA;
	}

	public static void setArmorA(Image armorA) {
		ImageProvider.armorA = armorA;
	}

	public static Image getArmorB() {
		return armorB;
	}

	public static void setArmorB(Image armorB) {
		ImageProvider.armorB = armorB;
	}

	public static Image getBasicA() {
		return basicA;
	}

	public static void setBasicA(Image basicA) {
		ImageProvider.basicA = basicA;
	}

	public static Image getBasicB() {
		return basicB;
	}

	public static void setBasicB(Image basicB) {
		ImageProvider.basicB = basicB;
	}

	public static Image getFastA() {
		return fastA;
	}

	public static void setFastA(Image fastA) {
		ImageProvider.fastA = fastA;
	}

	public static Image getFastB() {
		return fastB;
	}

	public static void setFastB(Image fastB) {
		ImageProvider.fastB = fastB;
	}

	public static Image getPowerA() {
		return powerA;
	}

	public static void setPowerA(Image powerA) {
		ImageProvider.powerA = powerA;
	}

	public static Image getPowerB() {
		return powerB;
	}

	public static void setPowerB(Image powerB) {
		ImageProvider.powerB = powerB;
	}

	public static Image getGameOver() {
		return gameOver;
	}

	public static void setGameOver(Image gameOver) {
		ImageProvider.gameOver = gameOver;
	}

	public static Image getLoading() {
		return loading;
	}

	public static void setLoading(Image loading) {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		loading = toolkit.getImage("resource/loading.gif");
		ImageProvider.loading = loading;
	}

	public static Image getBattleCity() {
		return battleCity;
	}

	public static void setBattleCity(Image battleCity) {
		ImageProvider.battleCity = battleCity;
	}

	public static Image getAppear1() {
		return appear1;
	}

	public static void setAppear1(Image appear1) {
		ImageProvider.appear1 = appear1;
	}

	public static Image getAppear2() {
		return appear2;
	}

	public static void setAppear2(Image appear2) {
		ImageProvider.appear2 = appear2;
	}

	public static Image getAppear3() {
		return appear3;
	}

	public static void setAppear3(Image appear3) {
		ImageProvider.appear3 = appear3;
	}

	public static Image getAppear4() {
		return appear4;
	}

	public static void setAppear4(Image appear4) {
		ImageProvider.appear4 = appear4;
	}

	public static Image getFlag_destroyed() {
		return flagDestroyed;
	}

	public static void setFlag_destroyed(Image flagDestroyed) {
		ImageProvider.flagDestroyed = flagDestroyed;
	}

	public static Image getLevel() {
		return level;
	}

	public static void setLevel(Image level) {
		ImageProvider.level = level;
	}

	public static Image getSound() {
		return sound;
	}

	public static void setSound(Image sound) {
		ImageProvider.sound = sound;
	}

	public static Image getEasy() {
		return easy;
	}

	public static void setEasy(Image easy) {
		ImageProvider.easy = easy;
	}

	public static Image getNormal() {
		return normal;
	}

	public static void setNormal(Image normal) {
		ImageProvider.normal = normal;
	}

	public static Image getHard() {
		return hard;
	}

	public static void setHard(Image hard) {
		ImageProvider.hard = hard;
	}

	public static void setArrowLeft(Image arrowBack) {
		ImageProvider.arrowLeft = arrowBack;
	}
	
	public static Image getArrowLeft() {
		return arrowLeft;
	}
	
	public static void setArrowRight(Image arrow) {
		ImageProvider.arrowRight = arrow;
	}
	
	public static Image getArrowRight() {
		return arrowRight;
	}

	public static Image getCursorLeft() {
		return cursorL;
	}

	public static void setCursorLeft(Image cursorL) {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		cursorL = toolkit.getImage("resource/cursorLeft.gif");
		ImageProvider.cursorL = cursorL;
	}
	
	public static Image getCursorRight() {
		return cursorR;
	}

	public static void setCursorRight(Image cursorR) {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		cursorR = toolkit.getImage("resource/cursorRight.gif");
		ImageProvider.cursorR = cursorR;
	}

	public static Image getRocketExplosion1() {
		return rocketExplosion1;
	}

	public static void setRocketExplosion1(Image rocketExplosion1) {
		ImageProvider.rocketExplosion1 = rocketExplosion1;
	}

	public static Image getRocketExplosion2() {
		return rocketExplosion2;
	}

	public static void setRocketExplosion2(Image rocketExplosion2) {
		ImageProvider.rocketExplosion2 = rocketExplosion2;
	}

	public static Image getRocketExplosion3() {
		return rocketExplosion3;
	}

	public static void setRocketExplosion3(Image rocketExplosion3) {
		ImageProvider.rocketExplosion3 = rocketExplosion3;
	}

	public static Image getShield1() {
		return shield1;
	}

	public static void setShield1(Image shield1) {
		ImageProvider.shield1 = shield1;
	}

	public static Image getShield2() {
		return shield2;
	}

	public static void setShield2(Image shield2) {
		ImageProvider.shield2 = shield2;
	}
	
	public static Image getArmorPowerUpA() {
		return armorPowerUpA;
	}

	public static void setArmorPowerUpA(Image armorPowerUpA) {
		ImageProvider.armorPowerUpA = armorPowerUpA;
	}

	public static Image getArmorPowerUpB() {
		return armorPowerUpB;
	}

	public static void setArmorPowerUpB(Image armorPowerUpB) {
		ImageProvider.armorPowerUpB = armorPowerUpB;
	}

	public static Image getBasicPowerUpA() {
		return basicPowerUpA;
	}

	public static void setBasicPowerUpA(Image basicPowerUpA) {
		ImageProvider.basicPowerUpA = basicPowerUpA;
	}

	public static Image getBasicPowerUpB() {
		return basicPowerUpB;
	}

	public static void setBasicPowerUpB(Image basicPowerUpB) {
		ImageProvider.basicPowerUpB = basicPowerUpB;
	}

	public static Image getFastPowerUpA() {
		return fastPowerUpA;
	}

	public static void setFastPowerUpA(Image fastPowerUpA) {
		ImageProvider.fastPowerUpA = fastPowerUpA;
	}

	public static Image getFastPowerUpB() {
		return fastPowerUpB;
	}

	public static void setFastPowerUpB(Image fastPowerUpB) {
		ImageProvider.fastPowerUpB = fastPowerUpB;
	}

	public static Image getPowerPowerUpA() {
		return powerPowerUpA;
	}

	public static void setPowerPowerUpA(Image powerPowerUpA) {
		ImageProvider.powerPowerUpA = powerPowerUpA;
	}

	public static Image getPowerPowerUpB() {
		return powerPowerUpB;
	}

	public static void setPowerPowerUpB(Image powerPowerUpB) {
		ImageProvider.powerPowerUpB = powerPowerUpB;
	}
	
	public static Image getPlayer1A_s1() {
		return player1A_s1;
	}

	public static void setPlayer1A_s1(Image player1a_s1) {
		player1A_s1 = player1a_s1;
	}

	public static Image getPlayer1B_s1() {
		return player1B_s1;
	}

	public static void setPlayer1B_s1(Image player1b_s1) {
		player1B_s1 = player1b_s1;
	}

	public static Image getPlayer1A_s2() {
		return player1A_s2;
	}

	public static void setPlayer1A_s2(Image player1a_s2) {
		player1A_s2 = player1a_s2;
	}

	public static Image getPlayer1B_s2() {
		return player1B_s2;
	}

	public static void setPlayer1B_s2(Image player1b_s2) {
		player1B_s2 = player1b_s2;
	}

	public static Image getPlayer1A_s3() {
		return player1A_s3;
	}

	public static void setPlayer1A_s3(Image player1a_s3) {
		player1A_s3 = player1a_s3;
	}

	public static Image getPlayer1B_s3() {
		return player1B_s3;
	}

	public static void setPlayer1B_s3(Image player1b_s3) {
		player1B_s3 = player1b_s3;
	}
	
	public static Image getBigExplosion1() {
		return bigExplosion1;
	}

	public static void setBigExplosion1(Image bigExplosion1) {
		ImageProvider.bigExplosion1 = bigExplosion1;
	}

	public static Image getBigExplosion2() {
		return bigExplosion2;
	}

	public static void setBigExplosion2(Image bigExplosion2) {
		ImageProvider.bigExplosion2 = bigExplosion2;
	}

	public static Image getBigExplosion3() {
		return bigExplosion3;
	}

	public static void setBigExplosion3(Image bigExplosion3) {
		ImageProvider.bigExplosion3 = bigExplosion3;
	}

	public static Image getBigExplosion4() {
		return bigExplosion4;
	}

	public static void setBigExplosion4(Image bigExplosion4) {
		ImageProvider.bigExplosion4 = bigExplosion4;
	}

	public static Image getBigExplosion5() {
		return bigExplosion5;
	}

	public static void setBigExplosion5(Image bigExplosion5) {
		ImageProvider.bigExplosion5 = bigExplosion5;
	}

	public static Image getSelection() {
		return selection;
	}

	public static void setSelection(Image selection) {
		ImageProvider.selection = selection;
	}

	public static Image getBar() {
		return Bar;
	}

	public static void setBar(Image totalBar) {
		ImageProvider.Bar = totalBar;
	}

	public static Image getPtsArrow() {
		return ptsArrow;
	}

	public static void setPtsArrow(Image ptsArrow) {
		ImageProvider.ptsArrow = ptsArrow;
	}

	public static Image getPoints100() {
		return points100;
	}

	public static void setPoints100(Image points100) {
		ImageProvider.points100 = points100;
	}

	public static Image getPoints200() {
		return points200;
	}

	public static void setPoints200(Image points200) {
		ImageProvider.points200 = points200;
	}

	public static Image getPoints300() {
		return points300;
	}

	public static void setPoints300(Image points300) {
		ImageProvider.points300 = points300;
	}

	public static Image getPoints400() {
		return points400;
	}

	public static void setPoints400(Image points400) {
		ImageProvider.points400 = points400;
	}

	public static Image getPoints500() {
		return points500;
	}

	public static void setPoints500(Image points500) {
		ImageProvider.points500 = points500;
	}

	public static Image getFinalPanelGame() {
		return finalPanelGame;
	}

	public static void setFinalPanelGame(Image finalPanelGame) {
		ImageProvider.finalPanelGame = finalPanelGame;
	}

	public static Image getTrash() {
		return trash;
	}

	public static void setTrash(Image trash) {
		ImageProvider.trash = trash;
	}

	public static Image getBackground2P() {
		return background2P;
	}

	public static void setBackground2P(Image background2P) {
		ImageProvider.background2P = background2P;
	}
	
	public static Image getBackground1P() {
		return background1P;
	}

	public static void setBackground1P(Image background1P) {
		ImageProvider.background1P = background1P;
	}
	
	public static Image getBackground1Px() {
		return background1Px;
	}

	public static void setBackground1Px(Image background1Px) {
		ImageProvider.background1Px = background1Px;
	}

	public static ArrayList<Image> getMapsP2() {
		return maps2P;
	}

	public static void setMaps2P(ArrayList<Image> maps2P) {
		ImageProvider.maps2P = maps2P;
	}
	
	public static ArrayList<Image> getMapsP1() {
		return maps1P;
	}

	public static void setMaps1P(ArrayList<Image> maps1P) {
		ImageProvider.maps1P = maps1P;
	}

	public static Image getPause() {
		return pause;
	}

	public static void setPause(Image pause) {
		ImageProvider.pause = pause;
	}

	public static Image getIconEnemy() {
		return iconEnemy;
	}

	public static void setIconEnemy(Image iconEnemy) {
		ImageProvider.iconEnemy = iconEnemy;
	}

	public static Image getIconFlag() {
		return iconFlag;
	}

	public static void setIconFlag(Image iconFlag) {
		ImageProvider.iconFlag = iconFlag;
	}
	
	public static Image getPlayer2A() {
		return player2A;
	}
	
	public static void setPlayer2A(Image player2a) {
		player2A = player2a;
	}

	public static Image getPlayer2B() {
		return player2B;
	}

	public static void setPlayer2B(Image player2b) {
		player2B = player2b;
	}

	public static Image getPlayer2A_s1() {
		return player2A_s1;
	}

	public static void setPlayer2A_s1(Image player2a_s1) {
		player2A_s1 = player2a_s1;
	}

	public static Image getPlayer2B_s1() {
		return player2B_s1;
	}

	public static void setPlayer2B_s1(Image player2b_s1) {
		player2B_s1 = player2b_s1;
	}

	public static Image getPlayer2A_s2() {
		return player2A_s2;
	}

	public static void setPlayer2A_s2(Image player2a_s2) {
		player2A_s2 = player2a_s2;
	}

	public static Image getPlayer2B_s2() {
		return player2B_s2;
	}

	public static void setPlayer2B_s2(Image player2b_s2) {
		player2B_s2 = player2b_s2;
	}

	public static Image getPlayer2A_s3() {
		return player2A_s3;
	}

	public static void setPlayer2A_s3(Image player2a_s3) {
		player2A_s3 = player2a_s3;
	}

	public static Image getPlayer2B_s3() {
		return player2B_s3;
	}

	public static void setPlayer2B_s3(Image player2b_s3) {
		player2B_s3 = player2b_s3;
	}

	public static Image getLocked() {
		return locked;
	}

	public static void setLocked(Image locked) {
		ImageProvider.locked = locked;
	}

	public static Image getStageComplete() {
		return stageComplete;
	}

	public static void setStageComplete(Image stageComplete) {
		ImageProvider.stageComplete = stageComplete;
	}
	
	public static Image getLives() {
		return lives;
	}

	public static void setLives(Image lives) {
		ImageProvider.lives = lives;
	}

	public static Image getArrowRightsmall() {
		return arrowRightsmall;
	}

	public static void setArrowRightsmall(Image arrowRightsmall) {
		ImageProvider.arrowRightsmall = arrowRightsmall;
	}

	public static Image getArrowLeftsmall() {
		return arrowLeftsmall;
	}

	public static void setArrowLeftsmall(Image arrowLeftsmall) {
		ImageProvider.arrowLeftsmall = arrowLeftsmall;
	}

	public static Image getKeyboard() {
		return keyboard;
	}

	public static void setKeyboard(Image keyboard) {
		ImageProvider.keyboard = keyboard;
	}

}