package com.jpmorgan.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.jpmorgan.model.CommonStock;
import com.jpmorgan.model.CurrencyValue;
import com.jpmorgan.model.GenericStock;
import com.jpmorgan.model.PreferredStock;
import com.jpmorgan.trading.GbceAllShare;
import com.jpmorgan.trading.Order;
import com.jpmorgan.trading.Portfolio;
import com.jpmorgan.trading.Utility;

// For the purpose of this exercise, I have ignored:
// - all the concurrency-related problems (the use of thread-safe collection classes and synchronization mechanisms must be considered)
// - all logging-related issues (a logging system must be used)
public class Test {
	public static void main(String args[]) throws Exception {
		// Uncomment the test(s) to execute
		Locale.setDefault(Locale.UK);
		// testGeometricMean();
		// testDividendYield();
		// testPERatio();
		// testTradeRecord();
		// testStockPrice();
		// testGbceAllShareIndex();
	}

	public static void testGeometricMean() {
		System.out.println("************************ testGeometricMean()");
		List<BigDecimal> values = new ArrayList<>();
		values.add(new BigDecimal(3));
		System.out.println(Utility.getGeometricMean(values) + " - "
				+ Utility.getClassicGeometricMean(values));
		values.add(new BigDecimal(4));
		System.out.println(Utility.getGeometricMean(values) + " - "
				+ Utility.getClassicGeometricMean(values));
		values.add(new BigDecimal(5));
		System.out.println(Utility.getGeometricMean(values) + " - "
				+ Utility.getClassicGeometricMean(values));
		values.add(new BigDecimal(10));
		System.out.println(Utility.getGeometricMean(values) + " - "
				+ Utility.getClassicGeometricMean(values));
		values.add(new BigDecimal(100));
		System.out.println(Utility.getGeometricMean(values) + " - "
				+ Utility.getClassicGeometricMean(values));
	}

	public static void testDividendYield() {
		System.out.println("************************ testDividendYield()");
		GenericStock tea = new CommonStock("TEA", 0, 100, 5.6);
		System.out.println(tea.getSymbol() + ": " + tea.getDividendYield());
		GenericStock pop = new CommonStock("POP", 8, 100, 5.6);
		System.out.println(pop.getSymbol() + ": " + pop.getDividendYield());
		GenericStock ale = new CommonStock("ALE", 23, 60, 5.6);
		System.out.println(ale.getSymbol() + ": " + ale.getDividendYield());
		GenericStock gin = new PreferredStock("GIN", 8, 2, 100, 5.6);
		System.out.println(gin.getSymbol() + ": " + gin.getDividendYield());
		GenericStock joe = new CommonStock("JOE", 13, 250, 5.6);
		System.out.println(joe.getSymbol() + ": " + joe.getDividendYield());
	}

	public static void testPERatio() {
		System.out.println("************************ testPERatio()");
		GenericStock tea = null;
		try {
			tea = new CommonStock("TEA", 0, 100, 5.6);
			System.out.println(tea.getSymbol() + ": " + tea.getPERatio());
		} catch (IllegalStateException ise) {
			String symbol = "";
			if (tea != null)
				symbol = tea.getSymbol();
			System.out.println(symbol + ": " + ise.getMessage());
		}
		GenericStock pop = new CommonStock("POP", 8, 100, 5.6);
		System.out.println(pop.getSymbol() + ": " + pop.getPERatio());
		GenericStock ale = new CommonStock("ALE", 23, 60, 5.6);
		System.out.println(ale.getSymbol() + ": " + ale.getPERatio());
		GenericStock gin = new PreferredStock("GIN", 8, 2, 100, 5.6);
		System.out.println(gin.getSymbol() + ": " + gin.getPERatio());
		GenericStock joe = new CommonStock("JOE", 13, 250, 5.6);
		System.out.println(joe.getSymbol() + ": " + joe.getPERatio());
	}

	public static void testTradeRecord() {
		System.out.println("************************ testPortfolio()");
		Portfolio portfolio = new Portfolio();
		GenericStock teaBuy = new CommonStock("TEA", 0, 100, 5.6);
		portfolio.buy(teaBuy, 100, new CurrencyValue(5));
		printPortfolio(portfolio);
		GenericStock popBuy1 = new CommonStock("POP", 8, 100, 5.6);
		portfolio.buy(popBuy1, 200, new CurrencyValue(7));
		printPortfolio(portfolio);
		GenericStock popBuy2 = new CommonStock("POP", 8, 100, 5.6);
		portfolio.buy(popBuy2, 300, new CurrencyValue(7));
		printPortfolio(portfolio);
		GenericStock popSell = new CommonStock("POP", 8, 100, 5.6);
		portfolio.sell(popSell, 500, new CurrencyValue(7));
		printPortfolio(portfolio);
		try {
			portfolio.sell(popSell, 500, new CurrencyValue(7));
		} catch (IllegalStateException ise) {
			System.out.println(portfolio + ": " + ise.getMessage());
		}
		printPortfolio(portfolio);
		printOrders(portfolio);
	}

	private static void printPortfolio(Portfolio portfolio) {
		System.out.println("*** Portfolio: " + portfolio);
		Map<GenericStock, Integer> stocks = portfolio.getStocks();
		for (Entry<GenericStock, Integer> stock : stocks.entrySet())
			System.out.println("Stock: " + stock.getKey().getSymbol()
					+ " - Quantity: " + stock.getValue());
		System.out.println("*** End portfolio: " + portfolio);
	}

	private static void printOrders(Portfolio portfolio) {
		System.out.println("*** Orders: " + portfolio);
		List<Order> orders = portfolio.getOrders();
		for (Order order : orders)
			System.out.println("Order: " + order);
		System.out.println("*** End orders: " + portfolio);
	}

	public static void testStockPrice() {
		System.out.println("************************ testStockPrice()");
		GenericStock tea = new CommonStock("TEA", 0, 100, 5.6);
		Portfolio portfolio1 = new Portfolio();
		portfolio1.buy(tea, 100, new CurrencyValue(5));
		portfolio1.buy(tea, 200, new CurrencyValue(6));
		Portfolio portfolio2 = new Portfolio();
		portfolio2.buy(tea, 300, new CurrencyValue(7));
		portfolio2.sell(tea, 200, new CurrencyValue(8));
		System.out.println(tea.getStockPrice(10));
		System.out
				.println((5.0 * 100 + 6.0 * 200 + 7.0 * 300 + 8.0 * 200) / 800);
	}

	public static void testGbceAllShareIndex() {
		System.out.println("************************ testGbceAllShareIndex()");
		GbceAllShare gbceAllShare = new GbceAllShare();
		GenericStock tea = new CommonStock("TEA", 0, 100, 3);
		gbceAllShare.addStock(tea);
		GenericStock pop = new CommonStock("POP", 8, 100, 4);
		gbceAllShare.addStock(pop);
		GenericStock ale = new CommonStock("ALE", 23, 60, 5);
		gbceAllShare.addStock(ale);
		GenericStock gin = new PreferredStock("GIN", 8, 2, 100, 6);
		gbceAllShare.addStock(gin);
		GenericStock joe = new CommonStock("JOE", 13, 250, 7);
		gbceAllShare.addStock(joe);
		System.out.println(gbceAllShare.getIndex());
		System.out.println(Math.pow(3 * 4 * 5 * 6 * 7, 1.0 / 5));
	}
}