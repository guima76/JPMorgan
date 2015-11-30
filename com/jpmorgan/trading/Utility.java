package com.jpmorgan.trading;

import java.math.BigDecimal;
import java.util.List;

public class Utility {
	// Instead of the provided formula, I used this equivalent way to calculate
	// the geometric mean to lower the possibilities of overflow; this method
	// provides 5 decimal digits
	public static BigDecimal getGeometricMean(List<BigDecimal> values) {
		int n = values.size();
		double gm = 0;
		for (BigDecimal value : values) {
			if (value.compareTo(BigDecimal.ZERO) == 0)
				return BigDecimal.ZERO;
			gm += Math.log(value.doubleValue());
		}
		return new BigDecimal(Math.exp(gm / n)).setScale(5,
				BigDecimal.ROUND_HALF_UP);
	}

	// This method calculates the geometric mean with the provided formula; I
	// used it just to verify the correctness of the method above
	public static BigDecimal getClassicGeometricMean(List<BigDecimal> values) {
		double gm = 1.0;
		for (int i = 0; i < values.size(); i++)
			gm *= values.get(i).doubleValue();
		gm = Math.pow(gm, 1.0 / (double) values.size());
		return new BigDecimal(gm).setScale(5, BigDecimal.ROUND_HALF_UP);
	}
}