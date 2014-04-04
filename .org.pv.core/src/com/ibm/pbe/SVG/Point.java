/**
 * 
 */
package com.ibm.pbe.SVG;
import java.util.Date;

import org.pv.core.Utils;

/**
 * @author PV
 *
 */
public class Point {
	final Utils utils = Utils.getSingleton();
	float x;
	float y;
	public Point(float x,float y) {
		this.x=x;
		this.y=y;
		
	}

	// Utility method for quick printing to console
	void p(Object o) {
		utils.log(o);
	}

}
