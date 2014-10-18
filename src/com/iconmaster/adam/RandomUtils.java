package com.iconmaster.adam;

import java.util.Map;
import java.util.Random;

/**
 *
 * @author iconmaster
 */
public class RandomUtils {
    public static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
		E result = null;
		double bestValue = Double.MAX_VALUE;

		for (E element : weights.keySet()) {
			double value = -Math.log(random.nextDouble()) / weights.get(element);

			if (value < bestValue) {
				bestValue = value;
				result = element;
			}
		}

		return result;
	}
	
	public static <E> E getWeightedRandom(Map<E, Double> weights) {
		return getWeightedRandom(weights, new Random());
	}
}
