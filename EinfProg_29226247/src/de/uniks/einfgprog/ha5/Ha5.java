/*
 * %W% %E% Christian Bier
 * Copyright (c) 2016 Christian Bier, All Rights Reserved.
 */
package de.uniks.einfgprog.ha5;

import processing.core.PApplet;

/**
 * Dieses Programm wurde im Rahmen der Hausaufgabe Nr. 4 der Vorlesung
 * Einfuehrung in die Programmierung der Universitaet Kassel, Prof.
 * Albert Zuendorf, erstellt. 
 */

public class Ha5 extends PApplet {
	public static void main(String[] args) {
		PApplet.main("de.uniks.einfgprog.ha5.Ha5");

	}

	int wallDepth = 20;
	float paddle1PosX = 0;
	float paddle1PosY = 0;
	int paddle1SizeX = 20;
	float paddle1SizeY = 100;
	float paddle2PosX = 0;
	float paddle2PosY = 0;
	float paddle2SizeY = 100;
	int paddle2SizeX = 20;
	float paddleSpeed = 10;
	int ballSize = 30;
	float ballPosX = 0;
	float ballPosY = 0;
	double ballXSpeed = 5;
	double ballXSpeedStart = 0;
	double ballYSpeed = 0;
	boolean ballOut = false;
	boolean right = false;
	int scoreOne = 0, scoreTwo = 0;
	int colorLines = 0;
	int colorBall = 0;
	int controlTime = 0;
	int brickPosX = 0;
	int brickPosY = 0;
	int brickSizeX = 0;
	int brickSizeY = 0;
	int brickHitCount[];
	boolean ballDir = false;
	boolean brickOut = false;

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		paddle1PosX = 10;
		paddle1PosY = (height - 2 * wallDepth) / 2 - (paddle1SizeY / 2);
		paddle2PosX = (width - paddle2SizeX) - paddle1PosX;
		paddle2PosY = (height - 2 * wallDepth) / 2 - (paddle1SizeY / 2);
		ballPosX = (width / 2) + 100;
		ballPosY = (height - 2 * wallDepth) / 2;
		ballXSpeedStart = ballXSpeed;
		brickSizeX = 20;
		brickSizeY = 80;
		brickPosX = (width / 2) - (brickSizeX / 2);
		brickHitCount = new int[5];
	}

	public void draw() {
		background(255);
		wallTop();
		wallBottom();
		paddle1();
		paddle2();
		middleStroke();
		scoreLeft();
		scoreRight();
		showControlButtons();
		bricks();
		ball();
	}

	void bricks() {
		brickPosY = wallDepth + 30;
		int brickCount = 0;

		while ((brickPosY + brickSizeY) < height) {
			// Ist die xPos eines Bricks kleiner als 90 wird er wieder in die
			// Mitte zurueckgesetzt
			if ((brickPosX + brickHitCount[brickCount]) < 90) {
				brickHitCount[brickCount] = 0;
				ballDir = false;
			}
			// Ist die xPos eines Bricks groesser als 1090 wird er wieder in die
			// Mitte zurueckgesetzt
			else if ((brickPosX + brickHitCount[brickCount]) > 1090) {
				brickHitCount[brickCount] = 0;
				ballDir = true;
			}
			// Zeichnen der Bricks
			rect(brickPosX + brickHitCount[brickCount], brickPosY, brickSizeX, brickSizeY);
			brickCount++;
			brickPosY += brickSizeY + 50;
		}
	}

	void ball() {

		/* Abfrage: Ist der Ball aus dem Spielfeld geflogen?
		 * Wenn ja, wird er in der Mitte wieder eingesetzt
		 */

		if (ballOut == true) {
			ballPosX = width / 2 + 100;
			ballPosY = (height - 2 * wallDepth) / 2;
			ballOut = false;
			ballDir = false;
			// Zuruecksetzen der extra Geschwindigkeit auf den Startwert

			ballXSpeed = ballXSpeedStart;
			/* Umkehr der Ballrichtung um der Start-Richtungs-Monotonie
			 * entgegen zu wirken
			 */

			if (right == true) {
				ballXSpeed = -1 * ballXSpeed;
				right = false;
				ballDir = true;
				ballPosX = width / 2 - 100;
			}

			/* Zuruecksetzen des Y-Speeds um den Ball wieder gerade starten zu
			 * lassen
			 */
			ballYSpeed = 0;
		}

		// Abfrage: Ist der Ball mit paddle2 kollidiert?
		if ((ballPosX + ballSize / 2) >= paddle2PosX) {
			if ((ballPosY + ballSize / 2) >= paddle2PosY &&
					(ballPosY - ballSize / 2) <= (paddle2PosY + paddle2SizeY)) {
				/* Pruefen ob eine Taste zum bewegen des Paddles gedrueckt ist
				 * um einen Spin mit zu geben
				 */
				
				if (keyPressed && key == 'o' || keyPressed && key == 'l') {

					ballXSpeed = -1.25 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - paddle2SizeY / 2) * 0.05;
					ballDir = true;

					/* Begrenzen der maximalen Geschwindigkeitszunahme in
					 * beiden Richtungen auf
					 * das doppelte der Startgeschwindigkeit
					 *
					 */
					
					if (ballXSpeed > (ballXSpeedStart * 2) || ballXSpeed < (-1 * (ballXSpeedStart * 2))) {
						ballXSpeed = ballXSpeedStart * 2;
					}
				} else {
					ballXSpeed = -1 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - paddle2SizeY / 2) * 0.07;
					ballDir = true;
				}
			} else if (((ballPosY) < paddle2PosY && (ballPosX - ballSize / 2) > (paddle2PosX + paddle2SizeX))
					|| ((ballPosY) > (paddle2PosY + paddle2SizeY)
							&& (ballPosX - ballSize / 2) > (paddle2PosX + paddle2SizeX))) {
				ballOut = true;
				right = true;
				scoreOne++;
			}
		}
		// Abfrage: Ist der Ball mit paddle1 kollidiert?
		
		else if ((ballPosX - ballSize / 2) <= paddle1PosX + paddle1SizeX) {
			if ((ballPosY + ballSize / 2) >= paddle1PosY &&
					(ballPosY - ballSize / 2) <= (paddle1PosY + paddle1SizeY)) {
				
				/* Pruefen ob eine Taste zum bewegen des Paddles gedrueckt ist
				 * um einen Spin mit zu geben
				 */
				
				if (keyPressed && key == 'w' || keyPressed && key == 's') {
					ballXSpeed = -1.25 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - paddle2SizeY / 2) * 0.05;
					ballDir = false;
					/* Begrenzen der maximalen Geschwindigkeitszunahme in beiden
					 * Richtungen auf das doppelte der Startgeschwindigkeit
					 */

					if (ballXSpeed > (ballXSpeedStart * 2) || ballXSpeed < (-1 * (ballXSpeedStart * 2))) {
						ballXSpeed = ballXSpeedStart * 2;
					}
				} else {
					ballXSpeed = -1 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle1PosY - paddle1SizeY / 2) * 0.07;
					ballDir = false;
				}
			} else if ((ballPosY < paddle1PosY && (ballPosX + ballSize / 2) < paddle1PosX)
					|| (ballPosY > (paddle1PosY + paddle1SizeY) && (ballPosX + ballSize / 2) < (paddle1PosX))) {
				ballOut = true;
				scoreTwo++;
			}
		}

		// Abfrage: Beruehrt der Ball die obere Wand?
		
		if (ballPosY - ballSize / 2 <= wallDepth) {
			ballYSpeed = ballYSpeed * -1;
		}

		// Abfrage: Beruehrt der Ball die untere Wand?
		if (ballPosY + ballSize / 2 >= (height - wallDepth)) {
			ballYSpeed = ballYSpeed * -1;
		}

		// Abfrage: Trifft der Ball einen der Bricks?
		bricksHit();

		// Zeichnen des Balls
		fill(colorBall);
		ellipse(ballPosX, ballPosY, ballSize, ballSize);

		// Hochzaehlen der Ballposition X und Y
		ballPosX += ballXSpeed;
		ballPosY += ballYSpeed;
	}

	void bricksHit() {
		int brickNo = 0;
		int yPos = wallDepth + 30;
		while (brickNo < 5) {
			if (ballPosY > yPos && ballPosY < (yPos + 80)) {
				/* Abfrage: Beruehrt der Ball den Brick und aus welcher Richtung
				 * kommt er? Die Trefferzone des Balls wurde bewusst mittig
				 * gehalten da dieser sonst nur schwer zwischen den Luecken
				 * der Bricks dieser sonst nur schwer zwischen den Luecken der Bricks
				 */

				if (ballDir == false && ballPosX - (ballSize / 2) < (brickPosX + brickHitCount[brickNo] + brickSizeX)
						&& ballPosX + (ballSize / 2) > brickPosX + brickHitCount[brickNo]) {
					ballXSpeed = -ballXSpeed;
					// Versetzen der Brick X-Position um 10 Pixel nach rechts
					brickHitCount[brickNo] += 10;
				}
				// Abfrage: Beruehrt der Ball den Brick und aus welcher Richtung
				// kommt er?
				// Die Trefferzone des Balls wurde bewusst mittig gehalten da
				// dieser sonst nur schwer zwischen den Luecken der Bricks
				// hindurchkommt
				else if (ballDir == true
						&& ballPosX - (ballSize / 2) < (brickPosX + brickHitCount[brickNo] + brickSizeX)
						&& ballPosX + (ballSize / 2) > brickPosX + brickHitCount[brickNo]) {
					ballXSpeed = -ballXSpeed;
					// Versetzen der Brick X-Position um 10 Pixel nach links
					brickHitCount[brickNo] -= 10;
				}
			}
			yPos += 130;
			brickNo++;
		}
	}

	void paddle1() {
		if (keyPressed && key == 'w') {
			// Pruefen ob das Paddle1 die obere Wand beruehrt
			if (paddle1PosY <= wallDepth) {
				paddle1PosY = paddle1PosY;
			} else {
				paddle1PosY = paddle1PosY - paddleSpeed;
			}
		} else if (keyPressed && key == 's') {
			// �Pruefen ob Paddle1 die untere Wand beruehrt
			if ((paddle1PosY + paddle1SizeY) >= (height - wallDepth)) {
				paddle1PosY = paddle1PosY;
			} else {
				paddle1PosY = paddle1PosY + paddleSpeed;
			}
		}
		fill(colorLines);
		rect(paddle1PosX, paddle1PosY, paddle1SizeX, paddle1SizeY, 20, 20, 20, 20);
	}

	void paddle2() {

		if (keyPressed && key == 'o') {
			// �Pruefen ob Paddle2 die obere Wand beruehrt
			if (paddle2PosY <= wallDepth) {
				paddle2PosY = paddle2PosY;
			} else {
				paddle2PosY = paddle2PosY - paddleSpeed;
			}
		} else if (keyPressed && key == 'l') {
			// �Pruefen ob Paddle2 die untere Wand beruehrt
			if ((paddle2PosY + paddle2SizeY) >= (height - wallDepth)) {
				paddle2PosY = paddle2PosY;
			} else {
				paddle2PosY = paddle2PosY + paddleSpeed;
			}
		}
		fill(colorLines);
		int paddle2SizeX = 20;
		rect(paddle2PosX, paddle2PosY, paddle2SizeX, paddle2SizeY, 20, 20, 20, 20);
	}

	void scoreLeft() {
		textSize(20);
		fill(colorLines);
		text(scoreOne, paddle1PosX + paddle1SizeX + 5, wallDepth + 30);
	}

	void scoreRight() {
		textSize(20);
		fill(colorLines);
		text(scoreTwo, paddle2PosX - 15, wallDepth + 30);
	}

	void middleStroke() {
		int middleStrokeSizeX = 5;
		fill(colorLines);
		rect((width / 2 - middleStrokeSizeX / 2), 0, middleStrokeSizeX, height);
	}

	void wallTop() {
		noStroke();
		fill(colorLines);
		rect(0, 0, width, wallDepth);
	}

	void wallBottom() {
		noStroke();
		fill(colorLines);
		rect(0, (height - wallDepth), width, wallDepth);
	}

	void showControlButtons() {
		// �Anzeige der Tastaturbelegung im Spielfeld fuer ca. 20 Sekunden
		// �mit einer Framerate von ca. 60
		if (controlTime < 1200) {
			textSize(20);
			fill(colorLines);
			text("Steuerung links: W + S", paddle1PosX + paddle1SizeX + 5, height - wallDepth - 30);
			text("Steuerung rechts: O + L", paddle2PosX - 250, height - wallDepth - 30);
			controlTime++;
		}
	}
}
