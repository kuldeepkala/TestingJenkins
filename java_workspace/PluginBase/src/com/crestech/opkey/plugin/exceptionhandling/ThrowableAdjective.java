package com.crestech.opkey.plugin.exceptionhandling;

public class ThrowableAdjective {
	
	private Throwable throwable;
	private Handleability handleability;
	private Operator operator;
	
	ThrowableAdjective(Throwable e, Handleability handleability, Operator operator) {
		this.throwable = e;
		this.handleability = handleability;
		this.operator = operator;
	}

	public Handleability isSubclassOf(Class<?> clazz) {
		int points = calculatePoints(throwable, clazz);
		handleability.push(operator, points);
		return handleability;
	}
	
	private int calculatePoints(Throwable e, Class<?> clazz) {
		int points = 0;
		
		Class<?> eClass = e.getClass();
		if(clazz.isAssignableFrom(eClass)) {
			points = 200;
			
			while ((eClass != null) && (eClass != clazz)) {
				points--; //the farther the match, the more points are deducted
				eClass = eClass.getSuperclass();
			}
		}
		
		return points;
	}
}