package com.jack.pathtracer;

public class ProgressBar {

	private static final int BAR_SIZE = 20;
	
	private int value = 0;
	private final int max;
	private final String label;
	
	public ProgressBar(String label, int max) {
		this.max = max;
		this.label = label;
		this.repaint();
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		if(value < 0) this.value = 0;
		if(value > this.max) this.value = this.max;
		if(this.value != value) {
			this.value = value;
			this.repaint();
		}
	}
	
	public void increment() {
		this.setValue(this.getValue() + 1);
	}
	
	private void repaint() {
		System.out.print("[");
		int steps = (int)((float)this.value * BAR_SIZE / this.max);
		for(int i = 0; i < BAR_SIZE; i++) {
			System.out.print(i <= steps ? "\u2588" : "-");
		}
		float prog = (float)this.value * 100f / this.max;
		System.out.print("] " + this.value + "/" + this.max + " " + this.label + ": " + String.format("%.2f", prog) + "%     \r");
	}
	
	public void end() {
		System.out.println();
	}
	
}