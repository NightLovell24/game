package com.archers.model;

import java.util.Date;

public class PlayerData {
	public enum State {
		STANDING, RUNNING, HIDING
	}

	public enum Facing {
		LEFT, RIGHT, DOWN, UP
	}

	public PlayerData(String nickname) {
		this.nickname = nickname;
	}

	public PlayerData() {

	}

	private int id;
	private Date date;
	private String nickname;
	private float x;
	private float y;
	private State currentState;
	private Facing currentFacing;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean downPressed;
	private float mouseAngleX;
	private float mouseAngleY;
	private boolean stopped;


	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public Facing getCurrentFacing() {
		return currentFacing;
	}

	public void setCurrentFacing(Facing currentFacing) {
		this.currentFacing = currentFacing;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	public float getMouseAngleX() {
		return mouseAngleX;
	}

	public void setMouseAngleX(float mouseAngleX) {
		this.mouseAngleX = mouseAngleX;
	}

	public float getMouseAngleY() {
		return mouseAngleY;
	}

	public void setMouseAngleY(float mouseAngleY) {
		this.mouseAngleY = mouseAngleY;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	@Override
	public String toString() {
		return "nickname : " + nickname + " x : " + x + " y: " + y + " currentState : " + currentState
				+ " currentFacing : " + currentFacing;
	}

}
