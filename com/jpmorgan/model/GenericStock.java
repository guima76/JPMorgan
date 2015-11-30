package com.jpmorgan.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import com.jpmorgan.trading.Order;

public abstract class GenericStock {
	private String symbol;
	private StockType type;

	// A dividend could be, for example, 0.058 pounds per share (5.8 pennies per
	// share)
	private CurrencyValue lastDividend;
	private CurrencyValue parValue;

	private CurrencyValue tickerPrice;

	// This list stores the orders associated with this stock; an order
	// container should be used to manage orders from different instances
	// representing the same stock
	private List<Order> orders;

	GenericStock(String symbol, StockType type, CurrencyValue lastDividend,
			CurrencyValue parValue, CurrencyValue tickerPrice) {
		if (symbol == null || symbol.length() == 0)
			throw new IllegalArgumentException(
					"Stock symbol can be neither null nor an empty string");
		if (type == null)
			throw new IllegalArgumentException("Stock type cannot be null");
		// Last dividend can be null for example if the company of the stock has
		// never given dividends
		if (lastDividend != null
				& lastDividend.getValue().compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException(
					"Stock cannot have a negative last dividend");
		if (parValue == null)
			throw new IllegalArgumentException(
					"Stock type cannot have a null par value");
		// If necessary, we also could check for a parValue > 0
		if (tickerPrice == null)
			throw new IllegalArgumentException(
					"The ticker price cannot be null");
		// I supposed here that the current price of a share cannot be <= 0
		if (tickerPrice.getValue().compareTo(BigDecimal.ZERO) <= 0)
			throw new IllegalArgumentException(
					"The ticker price can be neither zero nor less than zero");
		this.symbol = symbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.parValue = parValue;
		this.tickerPrice = tickerPrice;
		orders = new LinkedList<>();
	}

	GenericStock(String symbol, StockType type, BigDecimal lastDividend,
			BigDecimal parValue, BigDecimal tickerPrice) {
		this(symbol, type, new CurrencyValue(lastDividend), new CurrencyValue(
				parValue), new CurrencyValue(tickerPrice));
	}

	GenericStock(String symbol, StockType type, double lastDividend,
			double parValue, BigDecimal tickerPrice) {
		this(symbol, type, new CurrencyValue(lastDividend), new CurrencyValue(
				lastDividend), new CurrencyValue(tickerPrice));
	}

	public String getSymbol() {
		return symbol;
	}

	public StockType getType() {
		return type;
	}

	public CurrencyValue getLastDividend() {
		return lastDividend;
	}

	public CurrencyValue getParValue() {
		return parValue;
	}

	public CurrencyValue getTickerPrice() {
		return tickerPrice;
	}

	public abstract BigDecimal getDividendYield();

	public BigDecimal getPERatio() {
		if (lastDividend == null
				|| lastDividend.getValue().compareTo(BigDecimal.ZERO) == 0)
			throw new IllegalStateException(
					"Cannot calculate P/E ratio, last dividend is zero or null");
		return tickerPrice.getValue().divide(lastDividend.getValue(),
				BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getStockPrice(int minutes) {
		if (minutes < 0)
			throw new IllegalArgumentException(
					"Cannot calculate the stock price for a negative period");
		if (orders.size() == 0)
			throw new IllegalStateException(
					"No orders for this stock: cannot calculate stock price");
		Date now = new Date();
		int quantity = 0;
		BigDecimal orderValues = BigDecimal.ZERO;
		ListIterator<Order> iterator = orders.listIterator(orders.size());
		while (iterator.hasPrevious()) {
			Order order = iterator.previous();
			if (TimeUnit.MILLISECONDS.toMinutes(now.getTime()
					- order.getTimestamp().getTime()) <= minutes) {
				orderValues = orderValues.add(order.getOrderValue());
				quantity += order.getQuantity();
			} else
				break;
		}
		return orderValues.divide(new BigDecimal(quantity));
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public String toString() {
		return String.format(
				"symbol=%s; type=%s; lastDividend=%s; parValue=%s", symbol,
				type, lastDividend, parValue);
	}

	// In a portfolio, two instances of stocks must be considered equals if
	// their symbols and types are equals, independently, for example, from
	// the last dividend (an instance, for example, could refer to the year 2014
	// and the other one to the year 2015
	public int hashCode() {
		return 7 * symbol.hashCode() + type.hashCode();
	}

	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null || other.getClass() != this.getClass())
			return false;
		GenericStock genericStock = (GenericStock) other;
		return symbol.equals(genericStock.symbol)
				&& type.equals(genericStock.type);
	}
}