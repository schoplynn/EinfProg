/*
 * Author: Christian Bier
 * Date: 21.11.2016
 * Copyright (c) 2016 Christian Bier, All Rights Reserved.
 * 
 * Dieses Programm wurde im Rahmen der Hausaufgabe Nr. 4 der Vorlesung
 * Einfuehrung in die Programmierung der Universitaet Kassel, Prof.
 * Albert Zuendorf, erstellt und im Rahmen der Hausaufgabe Nr. 5 in
 * Eclipse uebertragen und nach Moeglichkeiten an die Java Code
 * Conventions angepasst.
 */
package de.uniks.einfgprog.ha5;

import processing.core.PApplet;

public class Ha5 extends PApplet {

	public static void main(String[] args) {
		PApplet.main("de.uniks.einfgprog.ha5.Ha5");
	}

	public static int PADDLE_1_SIZE_X = 20;
	public static int PADDLE_1_SIZE_Y = 100;
	public static int PADDLE_2_SIZE_Y = 100;
	public static int PADDLE_2_SIZE_X = 20;
	public static int BALL_SIZE = 30;
	public static int WALL_DEPTH = 20;
	public static int COLOR_LINES = 0; // Farbe der Linien und der Scores
	public static int COLOR_BALL = 0; // Farbe des Balls
	int scoreOne = 0;
	int scoreTwo = 0;
	int controlTime = 0; // Steuerung der Anzeigedauer der Controlelemente
	int brickPosX = 0;
	int brickPosY = 0;
	int brickSizeX = 0;
	int brickSizeY = 0;
	int brickHitCount[];
	float paddle1PosX = 0;
	float paddle1PosY = 0;
	float paddle2PosX = 0;
	float paddle2PosY = 0;
	float paddleSpeed = 10;
	float ballPosX = 0;
	float ballPosY = 0;
	double ballXSpeed = 5;
	double ballXSpeedStart = 0;
	double ballYSpeed = 0;
	boolean ballOut = false;
	boolean ballOutRight = false;
	boolean ballDir = false; // Aufzeichnung der Ballrichtung
	boolean brickOut = false;

	public void settings() {
		size(1200, 700);
	}

	public void setup() {
		paddle1PosX = 10;
		paddle1PosY = (height - 2 * WALL_DEPTH) / 2 - (PADDLE_1_SIZE_Y / 2);
		paddle2PosX = (width - PADDLE_2_SIZE_X) - paddle1PosX;
		paddle2PosY = (height - 2 * WALL_DEPTH) / 2 - (PADDLE_1_SIZE_Y / 2);
		ballPosX = (width / 2) + 100;
		ballPosY = (height - 2 * WALL_DEPTH) / 2;
		ballXSpeedStart = ballXSpeed;
		brickSizeX = 20;
		brickSizeY = 80;
		brickPosX = (width / 2) - (brickSizeX / 2);
		brickHitCount = new int[5];
	}

	public void draw() {
		background(255);
		drawWallTop();
		drawWallBottom();
		drawPaddle1();
		drawPaddle2();
		drawMiddleStroke();
		drawScoreLeft();
		drawScoreRight();
		showControlButtons();
		drawBricks();
		drawBall();
	}

	public void drawBricks() {

		/*
		 * Diese Methode zeichnet die vertikalen Steine im Spielfeld.
		 */

		brickPosY = WALL_DEPTH + 30;
		int brickCount = 0;

		while ((brickPosY + brickSizeY) < height) {

			if ((brickPosX + brickHitCount[brickCount]) < 90) {
				brickHitCount[brickCount] = 0;
				ballDir = false;
			} else if ((brickPosX + brickHitCount[brickCount]) > 1090) {
				brickHitCount[brickCount] = 0;
				ballDir = true;
			}
			rect(brickPosX + brickHitCount[brickCount], brickPosY, brickSizeX, brickSizeY);
			brickCount++;
			brickPosY += brickSizeY + 50;
		}
	}

	public void drawBall() {

		/* 
		 * Diese Methode zeichnet den Ball und ist ausserdem
		 * fuer die Kollisionsabfrage mit Waenden, Paddles
		 * verantwortlich. Auch die Ueberpruefung ob sich
		 * der Ball im Aus befindet wird hier durchgefuehrt.
		 */

		if (ballOut == true) {
			ballPosX = width / 2 + 100;
			ballPosY = (height - 2 * WALL_DEPTH) / 2;
			ballOut = false;
			ballDir = false;
			ballXSpeed = ballXSpeedStart;

			/* 
			 * Umkehr der Ballrichtung um der Start-Richtungs-Monotonie
			 * entgegen zu wirken.
			 */

			if (ballOutRight == true) {
				ballXSpeed = -1 * ballXSpeed;
				ballOutRight = false;
				ballDir = true;
				ballPosX = width / 2 - 100;
			}
			ballYSpeed = 0;
		}

		/* 
		 * Der folgende Block behandelt die Kollisionsabfrage mit
		 * Paddles, Waenden und Bricks.
		 */

		if ((ballPosX + BALL_SIZE / 2) >= paddle2PosX) {
			if ((ballPosY + BALL_SIZE / 2) >= paddle2PosY
					&& (ballPosY - BALL_SIZE / 2) <= (paddle2PosY + PADDLE_2_SIZE_Y)) {
				if (keyPressed && key == 'o' || keyPressed && key == 'l') {
					ballXSpeed = -1.25 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - PADDLE_2_SIZE_Y / 2) * 0.05;
					ballDir = true;
					if (ballXSpeed > (ballXSpeedStart * 2)
							|| ballXSpeed < (-1 * (ballXSpeedStart * 2))) {
						ballXSpeed = ballXSpeedStart * 2;
					}
				} else {
					ballXSpeed = -1 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - PADDLE_2_SIZE_Y / 2) * 0.07;
					ballDir = true;
				}
			} else if (((ballPosY) < paddle2PosY
					&& (ballPosX - BALL_SIZE / 2) > (paddle2PosX + PADDLE_2_SIZE_X))
					|| ((ballPosY) > (paddle2PosY + PADDLE_2_SIZE_Y)
							&& (ballPosX - BALL_SIZE / 2) > (paddle2PosX + PADDLE_2_SIZE_X))) {
				ballOut = true;
				ballOutRight = true;
				scoreOne++;
			}
		} else if ((ballPosX - BALL_SIZE / 2) <= paddle1PosX + PADDLE_1_SIZE_X) {
			if ((ballPosY + BALL_SIZE / 2) >= paddle1PosY
					&& (ballPosY - BALL_SIZE / 2) <= (paddle1PosY + PADDLE_1_SIZE_Y)) {
				if (keyPressed && key == 'w' || keyPressed && key == 's') {
					ballXSpeed = -1.25 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle2PosY - PADDLE_2_SIZE_Y / 2) * 0.05;
					ballDir = false;
					if (ballXSpeed > (ballXSpeedStart * 2)
							|| ballXSpeed < (-1 * (ballXSpeedStart * 2))) {
						ballXSpeed = ballXSpeedStart * 2;
					}
				} else {
					ballXSpeed = -1 * ballXSpeed;
					ballYSpeed = (ballPosY - paddle1PosY - PADDLE_1_SIZE_Y / 2) * 0.07;
					ballDir = false;
				}
			} else if ((ballPosY < paddle1PosY && (ballPosX + BALL_SIZE / 2) < paddle1PosX)
					|| (ballPosY > (paddle1PosY + PADDLE_1_SIZE_Y)
							&& (ballPosX + BALL_SIZE / 2) < (paddle1PosX))) {
				ballOut = true;
				scoreTwo++;
			}
		}
		if (ballPosY - BALL_SIZE / 2 <= WALL_DEPTH) {
			ballYSpeed = ballYSpeed * -1;
		}
		if (ballPosY + BALL_SIZE / 2 >= (height - WALL_DEPTH)) {
			ballYSpeed = ballYSpeed * -1;
		}
		areBricksHit();
		fill(COLOR_BALL);
		ellipse(ballPosX, ballPosY, BALL_SIZE, BALL_SIZE);
		ballPosX += ballXSpeed;
		ballPosY += ballYSpeed;
	}

	public void areBricksHit() {

		/* 
		 * Diese Methode ueberprueft ob der Ball mit den
		 * vertikalen Steinen im Spielfeld kollidiert ist.
		 */

		int brickNo = 0;
		int yPos = WALL_DEPTH + 30;
		while (brickNo < 5) {
			if (ballPosY > yPos && ballPosY < (yPos + 80)) {

				/* 
				 * Abfrage: Beruehrt der Ball den Brick und aus welcher Richtung
				 * kommt er? Die Trefferzone des Balls wurde bewusst mittig
				 * gehalten da dieser sonst nur schwer zwischen den Luecken
				 * der Bricks dieser sonst nur schwer zwischen den Luecken der Bricks
				 */

				if (ballDir == false && ballPosX - (BALL_SIZE / 2)
						< (brickPosX + brickHitCount[brickNo] + brickSizeX)
						&& ballPosX + (BALL_SIZE / 2) > brickPosX + brickHitCount[brickNo]) {
					ballXSpeed = -ballXSpeed;
					brickHitCount[brickNo] += 10;
				}

				/*
				 *  Abfrage: Beruehrt der Ball den Brick und aus welcher Richtung
				 *  kommt er? Die Trefferzone des Balls wurde bewusst mittig
				 *  gehalten da dieser sonst nur schwer zwischen den Luecken der
				 *  Bricks hindurchkommt.
				 */	

				else if (ballDir == true
						&& ballPosX - (BALL_SIZE / 2) < (brickPosX + brickHitCount[brickNo] + brickSizeX)
						&& ballPosX + (BALL_SIZE / 2) > brickPosX + brickHitCount[brickNo]) {
					ballXSpeed = -ballXSpeed;
					brickHitCount[brickNo] -= 10;
				}
			}
			yPos += 130;
			brickNo++;
		}
	}


	public void drawPaddle1() {

		/*
		 * Diese Methode zeichnet das Paddle Nr. 1
		 * (Paddle auf der linken Seite) und ist
		 * ebefalls fuer die Bewegung des Paddles
		 * verantwortlich.
		 */

		if (keyPressed && (key == 'w')) {
			if (paddle1PosY <= WALL_DEPTH) {
				paddle1PosY = paddle1PosY;
			} else {
				paddle1PosY = paddle1PosY - paddleSpeed;
			}
		} else if (keyPressed && (key == 's')) {
			if ((paddle1PosY + PADDLE_1_SIZE_Y) >= (height - WALL_DEPTH)) {
				paddle1PosY = paddle1PosY;
			} else {
				paddle1PosY = paddle1PosY + paddleSpeed;
			}
		}
		fill(COLOR_LINES);
		rect(paddle1PosX, paddle1PosY, PADDLE_1_SIZE_X, PADDLE_1_SIZE_Y, 20, 20, 20, 20);
	}


	public void drawPaddle2() {

		/*
		 * Diese Methode zeichnet das Paddle Nr. 2
		 * (Paddle auf der rechten Seite) und ist
		 * ebefalls fuer die Bewegung des Paddles
		 * verantwortlich.
		 */

		if (keyPressed && (key == 'o')) {
			if (paddle2PosY <= WALL_DEPTH) {
				paddle2PosY = paddle2PosY;
			} else {
				paddle2PosY = paddle2PosY - paddleSpeed;
			}
		} else if (keyPressed && (key == 'l')) {
			if ((paddle2PosY + PADDLE_2_SIZE_Y) >= (height - WALL_DEPTH)) {
				paddle2PosY = paddle2PosY;
			} else {
				paddle2PosY = paddle2PosY + paddleSpeed;
			}
		}
		fill(COLOR_LINES);
		int PADDLE_2_SIZE_X = 20;
		rect(paddle2PosX, paddle2PosY, PADDLE_2_SIZE_X, PADDLE_2_SIZE_Y, 20, 20, 20, 20);
	}


	public void drawScoreLeft() {

		/*
		 * Diese Methode zeigt den Score auf der linken
		 * Seite des Bildschirms an.
		 */

		textSize(20);
		fill(COLOR_LINES);
		text(scoreOne, paddle1PosX + PADDLE_1_SIZE_X + 5, WALL_DEPTH + 30);
	}


	public void drawScoreRight() {

		/*
		 * Diese Methode zeigt den Score auf der rechten
		 * Seite des Bildschirms an.
		 */

		textSize(20);
		fill(COLOR_LINES);
		text(scoreTwo, paddle2PosX - 15, WALL_DEPTH + 30);
	}


	public void drawMiddleStroke() {

		/*
		 * Diese Mehode zeichnet den vertikalen Strich
		 * in der Mitte des Spielfelds.
		 */

		int middleStrokeSizeX = 5;
		fill(COLOR_LINES);
		rect((width / 2 - middleStrokeSizeX / 2), 0, middleStrokeSizeX, height);
	}

	public void drawWallTop() {

		/*
		 * Diese Methode zeichnet den horizontalen
		 * Strich am oberen Rand des Spielfelds.
		 */

		noStroke();
		fill(COLOR_LINES);
		rect(0, 0, width, WALL_DEPTH);
	}


	public void drawWallBottom() {

		/*
		 * Diese Methode zeichnet den horizontalen
		 * Strich am unteren Rand des Spielfelds.
		 */

		noStroke();
		fill(COLOR_LINES);
		rect(0, (height - WALL_DEPTH), width, WALL_DEPTH);
	}


	public void showControlButtons() {

		/*
		 * Diese Methode zeigt die zur Steuerung festgelegten
		 * Tasten fuer ca. 20 Sekunden im unteren Bereich des
		 * Spielfelds an.
		 */

		if (controlTime < 1200) {
			textSize(20);
			fill(COLOR_LINES);
			text("Steuerung links: W + S", paddle1PosX + PADDLE_1_SIZE_X + 5, height - WALL_DEPTH - 30);
			text("Steuerung rechts: O + L", paddle2PosX - 250, height - WALL_DEPTH - 30);
			controlTime++;
		}
	}
}
