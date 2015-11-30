package com.jpmorgan.trading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jpmorgan.model.GenericStock;

public class GbceAllShare {
	private List<GenericStock> stocks;

	public GbceAllShare() {
		stocks = new LinkedList<GenericStock>();
	}

	public void addStock(GenericStock stock) {
		stocks.add(stock);
	}

	public BigDecimal getIndex() {
		List<BigDecimal> prices = new ArrayList<>();
		for (GenericStock stock : stocks)
			prices.add(stock.getTickerPrice().getValue());
		return Utility.getGeometricMean(prices);
	}
}