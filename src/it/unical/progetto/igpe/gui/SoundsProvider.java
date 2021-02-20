package it.unical.progetto.igpe.gui;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundsProvider {

	private static AudioInputStream audio;
	private static FloatControl gainControl;
	private static FloatControl gainControlLower;
	private static FloatControl gainControlMedium;
	
	private static File stageStart;
	private static File gameOver;
	private static File bulletShot;
	private static File powerUpPick;
	private static File powerUpAppear;
	private static File bulletHit1;
	private static File bulletHit2;
	private static File explosion1;
	private static File explosion2;
	private static File score;
	private static File pause;
	private static File move;
	private static File stop;
	private static File tankHit;
	private static File stageComplete;
	private static File canGo;
	private static File onlineStart;
	private static File onlineEnd;
	
	private static Clip stageCompleteClip;
	private static Clip stopClip;
	private static Clip moveClip;
	private static Clip pauseClip;
	private static Clip scoreClip;
	private static Clip explosion1Clip;
	private static Clip explosion2Clip;
	private static Clip bulletHit1Clip;
	private static Clip bulletHit2Clip;
	private static Clip powerUpAppearClip;
	private static Clip powerUpPickClip;
	private static Clip bulletShotClip;
	private static Clip gameOverClip;
	public static Clip stageStartClip; 
	private static Clip tankHitClip;
	private static Clip canGoClip;
	private static Clip onlineStartClip;
	private static Clip onlineEndClip;
	
	
	static {
		onlineEnd = new File("sounds/onlineEnd.wav");
		onlineStart = new File("sounds/onlineStart.wav");
		stageComplete = new File("sounds/stageComplete.wav");
		stageStart = new File("sounds/stageStart.wav");
		gameOver = new File("sounds/gameOver.wav");
		bulletShot = new File("sounds/bulletShot.wav");
		powerUpPick = new File("sounds/powerUpPick.wav");
		powerUpAppear = new File("sounds/powerUpAppear.wav");
		bulletHit1= new File("sounds/bulletHit1.wav");
		bulletHit2 = new File("sounds/bulletHit2.wav");
		explosion1 = new File("sounds/explosion1.wav");
		explosion2= new File("sounds/explosion2.wav");
		score = new File("sounds/score.wav");
		pause = new File("sounds/pause.wav");
		move = new File("sounds/tankMove.wav");
		stop = new File("sounds/tankStop.wav");
		tankHit = new File("sounds/tankHit.wav");
		canGo = new File("sounds/bulletHit2.wav");
	}
	
	static {
		try {
			audio =  AudioSystem.getAudioInputStream(onlineEnd);
			onlineEndClip = AudioSystem.getClip();
			onlineEndClip.open(audio);

			audio =  AudioSystem.getAudioInputStream(onlineStart);
			onlineStartClip = AudioSystem.getClip();
			onlineStartClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(canGo);
			canGoClip = AudioSystem.getClip();
			canGoClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(stageComplete);
			stageCompleteClip = AudioSystem.getClip();
			stageCompleteClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(tankHit);
			tankHitClip = AudioSystem.getClip();
			tankHitClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(stop);
			stopClip = AudioSystem.getClip();
			stopClip.open(audio);
		
			audio =  AudioSystem.getAudioInputStream(move);
			moveClip = AudioSystem.getClip();
			moveClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(pause);
			pauseClip = AudioSystem.getClip();
			pauseClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(score);
			scoreClip = AudioSystem.getClip();
			scoreClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(explosion1);
			explosion1Clip = AudioSystem.getClip();
			explosion1Clip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(explosion2);
			explosion2Clip = AudioSystem.getClip();
			explosion2Clip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(bulletHit1);
			bulletHit1Clip = AudioSystem.getClip();
			bulletHit1Clip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(bulletHit2);
			bulletHit2Clip = AudioSystem.getClip();
			bulletHit2Clip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(powerUpAppear);
			powerUpAppearClip = AudioSystem.getClip();
			powerUpAppearClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(powerUpPick);
			powerUpPickClip = AudioSystem.getClip();
			powerUpPickClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(bulletShot);
			bulletShotClip = AudioSystem.getClip();
			bulletShotClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(gameOver);
			gameOverClip = AudioSystem.getClip();
			gameOverClip.open(audio);
			
			audio =  AudioSystem.getAudioInputStream(stageStart);
			stageStartClip = AudioSystem.getClip();
			stageStartClip.open(audio);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setGain(Clip clip) {
		gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-SettingsPanel.soundValue);
	}
	
	public static void setGainLower(Clip clip) {
		gainControlLower = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControlLower.setValue(-(SettingsPanel.soundValue + 10));
	}
	
	public static void setGainMedium(Clip clip) {
		gainControlMedium = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControlMedium.setValue(-(SettingsPanel.soundValue + 5));
	}

	public static void playStop() {
		moveClip.stop();
		setGainLower(stopClip);
		stopClip.start();
		stopClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void cancelStop() {
		stopClip.stop();
	}
	
	public static void playMove() {
		stopClip.stop();
		setGainLower(moveClip);
		moveClip.start();
		moveClip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void cancelMove() {
		moveClip.stop();
	}
	
	public static void playHitForCanGo() {
		canGoClip.setFramePosition(0);
		setGainLower(canGoClip);
		canGoClip.start();
	}
	
	public static void playOnlineEnd() {
		onlineEndClip.setFramePosition(0);
		setGainLower(onlineEndClip);
		onlineEndClip.start();
	}
	
	public static void playOnlineStart() {
		onlineStartClip.setFramePosition(0);
		setGainLower(onlineStartClip);
		onlineStartClip.start();
	}
	
	public static void playPause() {
		pauseClip.setFramePosition(0);
		setGain(pauseClip);
		pauseClip.start();
	}
	
	public static void playScore() {
		scoreClip.setFramePosition(0);
		setGain(scoreClip);
		scoreClip.start();
	}
	
	public static void playTankHit() {
		tankHitClip.setFramePosition(0);
		setGain(tankHitClip);
		tankHitClip.start();
	}
	
	public static void playExplosion1() {
		explosion1Clip.setFramePosition(0);
		setGain(explosion1Clip);
		explosion1Clip.start();
	}
	
	public static void playExplosion2() {
		explosion2Clip.setFramePosition(0);
		setGain(explosion2Clip);
		explosion2Clip.start();
	}
	
	public static void playBulletHit1() {
		bulletHit1Clip.setFramePosition(0);
		setGainMedium(bulletHit1Clip);
		bulletHit1Clip.start();
	}
	
	public static void playBulletHit2() {
		bulletHit2Clip.setFramePosition(0);
		setGainMedium(bulletHit2Clip);
		bulletHit2Clip.start();
	}
	
	public static void playPowerUpAppear() {
		powerUpAppearClip.setFramePosition(0);
		setGain(powerUpAppearClip);
		powerUpAppearClip.start();
	}
	
	public static void playPowerUpPick(){
		powerUpPickClip.setFramePosition(0);
		setGain(powerUpAppearClip);
		powerUpPickClip.start();
	}
	
	public static void playBulletShot() {
		bulletShotClip.setFramePosition(0);
		setGain(bulletShotClip);
		bulletShotClip.start();
	}
	
	public static void playGameOver() {
		gameOverClip.setFramePosition(0);
		setGain(gameOverClip);
		gameOverClip.start();
	}

	public static void playStageStart(){
		stageStartClip.setFramePosition(0);
		setGain(stageStartClip);
		stageStartClip.start();
	}
	
	public static void playStageComplete(){
		stageCompleteClip.setFramePosition(0);
		setGain(stageCompleteClip);
		stageCompleteClip.start();
	}
}
