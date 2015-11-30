package com.jpmorgan.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

// Wrapper class of BigDecimal in order to have a unified mode of manage and printing values as currency.
// The number of the decimal digits could be managed depending on the value; for example,
// 0 < price <= 0.2 --> 4 decimal digits are required
// 0.2 < price <= 2 --> 3 decimal digits are required
// 2 < price --> 2 decimal digits are required
public class CurrencyValue {
	private BigDecimal value;

	public CurrencyValue(BigDecimal value) {
		// I don't use the original BigDecimal object passed to set the
		// attribute because I need to set the scale but I don't want to modify
		// the original object
		this(value.doubleValue());
	}

	public CurrencyValue(double value) {
		this.value = new BigDecimal(value)
				.setScale(5, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getValue() {
		return value;
	}

	// If necessary, this toString() method could be modified in order to print
	// more than 2 decimal digits, as necessary
	public String toString() {
		return NumberFormat.getCurrencyInstance().format(value);
	}
}