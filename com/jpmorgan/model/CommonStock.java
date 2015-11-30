package com.jpmorgan.model;

import java.math.BigDecimal;

public class CommonStock extends GenericStock {
	public CommonStock(String symbol, CurrencyValue lastDividend,
			CurrencyValue parValue, CurrencyValue tickerPrice) {
		super(symbol, StockType.COMMON, lastDividend, parValue, tickerPrice);
	}

	public CommonStock(String symbol, BigDecimal lastDividend,
			BigDecimal parValue, BigDecimal tickerPrice) {
		this(symbol, new CurrencyValue(lastDividend), new CurrencyValue(
				parValue), new CurrencyValue(tickerPrice));
	}

	public CommonStock(String symbol, double lastDividend, double parValue,
			double tickerPrice) {
		this(symbol, new CurrencyValue(lastDividend), new CurrencyValue(
				parValue), new CurrencyValue(tickerPrice));
	}

	@Override
	public BigDecimal getDividendYield() {
		return getLastDividend().getValue().divide(getTickerPrice().getValue(),
				BigDecimal.ROUND_HALF_UP);
	}

	public String toString() {
		return String.format("[%s]", super.toString());
	}
}