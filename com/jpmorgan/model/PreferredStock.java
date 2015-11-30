package com.jpmorgan.model;

import java.math.BigDecimal;

public class PreferredStock extends GenericStock {
	private int fixedDividend;

	public PreferredStock(String symbol, CurrencyValue lastDividend,
			int fixedDividend, CurrencyValue parValue, CurrencyValue tickerPrice) {
		super(symbol, StockType.PREFERRED, lastDividend, parValue, tickerPrice);
		this.fixedDividend = fixedDividend;
	}

	public PreferredStock(String symbol, BigDecimal lastDividend,
			int fixedDividend, BigDecimal parValue, BigDecimal tickerPrice) {
		this(symbol, new CurrencyValue(lastDividend), fixedDividend,
				new CurrencyValue(parValue), new CurrencyValue(tickerPrice));
	}

	public PreferredStock(String symbol, double lastDividend,
			int fixedDividend, double parValue, double tickerPrice) {
		this(symbol, new CurrencyValue(lastDividend), fixedDividend,
				new CurrencyValue(parValue), new CurrencyValue(tickerPrice));
	}

	public int getFixedDividend() {
		return fixedDividend;
	}

	@Override
	public BigDecimal getDividendYield() {
		return getParValue().getValue().multiply(new BigDecimal(fixedDividend))
				.divide(getTickerPrice().getValue(), BigDecimal.ROUND_HALF_UP)
				.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
	}

	public String toString() {
		return String.format("[%s; fixedDividend=%s%%]", super.toString(),
				fixedDividend);
	}
}