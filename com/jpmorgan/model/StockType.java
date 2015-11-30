package com.jpmorgan.model;

// Represents the type of a stock
enum StockType {
	COMMON("COM"), PREFERRED("PRE");

	// This is the code that will be stored in the database for each stock
	private String code;

	private StockType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}