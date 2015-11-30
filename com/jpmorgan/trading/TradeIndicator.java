package com.jpmorgan.trading;

public enum TradeIndicator {
	BUY("B"), SELL("S");

	// This is the code that will be stored in the database for each trading
	// operation
	private String code;

	private TradeIndicator(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}