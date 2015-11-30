package com.jpmorgan.trading;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import com.jpmorgan.model.CurrencyValue;
import com.jpmorgan.model.GenericStock;

public class Order {
	private GenericStock stock;
	private Date timestamp;
	private int quantity;
	private TradeIndicator tradeIndicator;
	private CurrencyValue singleStockPrice;

	public Order(GenericStock stock, int quantity,
			TradeIndicator tradeIndicator, CurrencyValue singleStockPrice) {
		if (stock == null)
			throw new IllegalArgumentException(
					"An order must be associated to a stock");
		if (quantity <= 0)
			throw new IllegalArgumentException(
					"Cannot build an order with a quantity <= 0");
		if (tradeIndicator == null)
			throw new IllegalArgumentException(
					"A Buy/Sell type must be specified");
		if (singleStockPrice == null
				|| singleStockPrice.getValue().compareTo(BigDecimal.ZERO) <= 0)
			throw new IllegalArgumentException("A >= 0 price must be specified");
		this.stock = stock;
		this.timestamp = new Date();
		this.quantity = quantity;
		this.tradeIndicator = tradeIndicator;
		this.singleStockPrice = singleStockPrice;
	}

	public GenericStock getStock() {
		return stock;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int getQuantity() {
		return quantity;
	}

	public TradeIndicator getTradeIndicator() {
		return tradeIndicator;
	}

	public CurrencyValue getSingleStockPrice() {
		return singleStockPrice;
	}

	public BigDecimal getOrderValue() {
		return singleStockPrice.getValue().multiply(new BigDecimal(quantity));
	}

	public String toString() {
		return String
				.format("[stock=%s; timestamp=%s; quantity=%d; BUY/SELL=%s; singleStockPrice=%s]",
						stock,
						DateFormat.getDateTimeInstance(DateFormat.FULL,
								DateFormat.FULL).format(timestamp), quantity,
						tradeIndicator, singleStockPrice);
	}
}