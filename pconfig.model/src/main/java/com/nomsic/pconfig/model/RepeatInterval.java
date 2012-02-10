package com.nomsic.pconfig.model;

public enum RepeatInterval {
	Daily("day"), Weekly("week"), Monthly("month");
	
	private String text;
	
	private RepeatInterval(String text) {
		this.text = text;
	}
	
	public String getText(int count) {
		return count > 1 ? text + "s" : text;
	}
}