package com.jpmorgan.trading;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jpmorgan.model.CurrencyValue;
import com.jpmorgan.model.GenericStock;

public class Portfolio {
	private UUID id;
	private Date activationDate;
	private Date closeDate;

	// This list stores the orders associated with this portfolio
	private List<Order> orders;

	// This map stores the stocks owned along with the quantity owned; this
	// information could be calculated from the orders associated with this
	// portfolio but, for performance reasons, we could have this map
	// representing the current state of this portfolio
	private Map<GenericStock, Integer> stocks;

	public Portfolio() {
		id = UUID.randomUUID();
		activationDate = new Date();
		orders = new LinkedList<>();
		stocks = new HashMap<>();
	}

	public UUID getId() {
		return id;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void close() {
		closeDate = new Date();
	}

	public boolean isActive() {
		return closeDate == null;
	}

	private void trade(GenericStock stock, int quantity,
			TradeIndicator tradeIndicator, CurrencyValue price) {
		if (!isActive())
			throw new IllegalStateException(
					"Cannot trade on a closed portfolio");
		Integer quantityInPortfolio = stocks.get(stock);
		if (tradeIndicator == TradeIndicator.SELL) {
			if (quantityInPortfolio == null || quantityInPortfolio < quantity)
				throw new IllegalStateException("Quantity not available");
		}
		// The registrations of the new order and the update of the current
		// state of the portfolio must be executed, in a real case with a
		// database, within the same transaction
		Order order = new Order(stock, quantity, tradeIndicator, price);
		orders.add(order);
		stock.addOrder(order);
		if (quantityInPortfolio == null) // surely a BUY order
			stocks.put(stock, quantity);
		else {
			if (tradeIndicator == TradeIndicator.SELL)
				quantity = -1 * quantity;
			int newQuantity = quantity + quantityInPortfolio;
			if (newQuantity == 0)
				stocks.remove(stock);
			else
				stocks.put(stock, newQuantity);
		}
	}

	public void buy(GenericStock stock, int quantity, CurrencyValue price) {
		trade(stock, quantity, TradeIndicator.BUY, price);
	}

	public void sell(GenericStock stock, int quantity, CurrencyValue price) {
		trade(stock, quantity, TradeIndicator.SELL, price);
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Map<GenericStock, Integer> getStocks() {
		return stocks;
	}

	public String toString() {
		return String.format("[id=%s]", id);
	}
}