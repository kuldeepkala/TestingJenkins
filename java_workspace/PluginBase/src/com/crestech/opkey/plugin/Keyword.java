package com.crestech.opkey.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to purposefully attach an additional name to a method. 
 * Say a method is named <code>Method_sum(int a, int b)</code>.
 * then the same method can be attached an additional name like this:
 * <pre><code>
 * @Keyword("Method_add")
 * public FunctionResult Method_sum(int a, int b) {
 *    ...
 * }
 * </code></pre>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Keyword {

	/**
	 * The intended method name by which opkey will call it.
	 * Something like <code>Method_WebBrowserOpen</code>
	 **/
	String value();

}
