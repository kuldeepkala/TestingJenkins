package com.crestech.opkey.plugin.exceptionhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Handleability implements Comparable<Handleability> {

	private Stack<Integer> allPoints = new Stack<>();
	private Stack<Operator> allOperators = new Stack<>();

	public Handleability(int points) {
		allPoints.push((Integer) points);
	}

	public final Comparetor _or_ = new Comparetor(this, Operator.OR);

	public final Comparetor _and_ = new Comparetor(this, Operator.AND);

	public int getPonts() {
		List<Integer> ands = new ArrayList<>();
		List<Integer> ors = new ArrayList<>();

		while (allOperators.size() > 0) {
			Operator op = allOperators.pop();
			Integer points = allPoints.pop();

			switch (op) {
			case AND:
				ands.add(points);
				break;

			case OR:
				ands.add(points);
				int i = crunchAnds(ands);
				ors.add((Integer) i);
				break;

			default:
				throw new Error("Unknown operator: " + op.toString());
			}
		}

		// now we process undigested ands. these ands were not preceded by any ors so, were not digested
		ands.add(allPoints.pop());
		int q = crunchAnds(ands);
		allPoints.add((Integer) q);

		/*
		 * there must be one-and-only-one element left in points-stack -> the
		 * initial value
		 */
		if (allPoints.size() != 1)
			throw new Error("or-stack not single");

		// finally we include the initial points
		ors.add(allPoints.pop());

		//
		int i = crunchOrs(ors);
		allPoints.add(i);
		return i;
	}

	private Integer crunchAnds(List<Integer> ands) {
		int i = 1;
		for (int j : ands) {
			i = i * j;
		}
		ands.clear();
		return i;
	}
	
	private Integer crunchOrs(List<Integer> ors) {
		int i = Integer.MIN_VALUE;
		for(int j : ors) {
			i = Math.max(i, j);
		}
		ors.clear();
		return i;
	}

	void push(Operator operator, int points) {
		allPoints.push((Integer) points);
		allOperators.push(operator);
	}

	@Override
	public int compareTo(Handleability o) {
		return Integer.compare(getPonts(), o.getPonts());
	}
}

enum Operator {
	AND, OR
}