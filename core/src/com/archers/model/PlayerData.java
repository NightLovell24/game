package com.archers.model;

import java.util.Date;

public class PlayerData {

	private enum State {
		STANDING, RUNNING, HIDING
	}

	private enum Facing {
		LEFT, RIGHT, DOWN, UP
	}

	public PlayerData(String nickname) {
		this.nickname = nickname;
	}

	public PlayerData() {

	}

	private Date date;
	private String nickname;
	private float x;
	private float y;
	private State currentState;
	private Facing currentFacing;

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

	@Override
	public String toString() {
		return "nickname : " + nickname + " x : " + x + " y: " + y + " currentState : " + currentState
				+ " currentFacing : " + currentFacing;
	}

}
