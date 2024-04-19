package com.Breakbreak;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Gameplay extends JPanel implements KeyListener,ActionListener{
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay =8;
	
	private int playerX = 310;
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballxdir = -1;
	private int ballydir = -2;
	

	
	
	private MapGenerator map;
	private Clip backgroundSound;
	 public Gameplay() {
	        map = new MapGenerator(3, 7);
	        addKeyListener(this);
	        setFocusable(true);
	        setFocusTraversalKeysEnabled(false);
	        timer = new Timer(delay, this);
	        timer.start();

	        // Load the background sound
	        try {
	            File soundFile = new File("D:\\files\\New folder\\Energetic Rock.wav");
	            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	            backgroundSound = AudioSystem.getClip();
	            backgroundSound.open(audioInputStream);
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
	            e.printStackTrace();
	        }

	        // Start playing the background sound
	        if (backgroundSound != null) {
	            backgroundSound.loop(Clip.LOOP_CONTINUOUSLY);
	        }
	    }

		

	
	
	public void paint(Graphics g) {
		// background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692,592 );
		
		// drawing map
		map.draw((Graphics2D)g);
		
		// border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3,592 );
		g.fillRect(0, 0, 692,3 );
		g.fillRect(691, 0, 3,592 );
		
		//scores 
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		// the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8 );
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20 );
		
		if(totalBricks<=0) {
			play =false;
			ballxdir = 0;
			ballydir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Won: ", 260, 300);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 230, 350);
		
		}
		
		if(ballposY>570) {
			play =false;
			ballxdir = 0;
			ballydir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over, Score: ", 190, 300);
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 230, 350);
		
		}
		g.dispose();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    timer.start();

	    if (play) {
	        // Check collision with paddle
	        if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
	            ballydir = -ballydir;
	        }

	        // Check collision with bricks
	        A: for (int i = 0; i < map.map.length; i++) {
	            for (int j = 0; j < map.map[0].length; j++) {
	                if (map.map[i][j] > 0) {
	                    int brickX = j * map.brickWidth + 80;
	                    int brickY = i * map.brickHeight + 50;
	                    int brickWidth = map.brickWidth;
	                    int brickHeight = map.brickHeight;

	                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
	                    Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

	                    if (ballRect.intersects(brickRect)) {
	                        map.setBrickvalue(0, i, j);
	                        totalBricks--;
	                        score += 5;

	                        if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
	                            ballxdir = -ballxdir;
	                        } else {
	                            ballydir = -ballydir;
	                        }

	                        break A;
	                    }
	                }
	            }
	        }

	        ballposX +=2* ballxdir;
	        ballposY += 2*ballydir;
	        if (ballposX < 0) {
	            ballxdir = -ballxdir;
	        }
	        if (ballposY < 0) {
	            ballydir = -ballydir;
	        }
	        if (ballposX > 670) {
	            ballxdir = -ballxdir;
	        }
	    }
	    repaint();
	}

		
	

	@Override
	public void keyTyped(KeyEvent e) {}	
	@Override
	public void keyReleased(KeyEvent e) {}


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			}else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			}else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play=true;
				ballposX = 120;
				ballposY = 350;
				ballxdir = -1;
				ballydir = -2;
				playerX = 310;
				score= 0;
				totalBricks =21;
				map =new MapGenerator(3,7);
				
				repaint();
			}
		}
		
	}
public void moveRight() {
	play = true;
	if (playerX + 20 <= 592) { // Adjusted boundary to prevent going out of border
        playerX += 20;
    }
}
public void moveLeft() {
	play = true;
	if (playerX - 20 >= 0) { // Adjusted boundary to prevent going out of border
        playerX -= 20;
    }
}
public void stopBackgroundSound() {
    if (backgroundSound != null) {
        backgroundSound.stop();
    }
}

}
