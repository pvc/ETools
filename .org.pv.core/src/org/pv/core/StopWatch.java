//*****************************************************************************
//* Licensed Materials - Property of IBM
//*
//* com.ibm.sal.rapidsat
//*
//* (C) Copyright IBM Corp. 2007
//*
//* US Government Users Restricted Rights - Use, duplication or
//* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
//*****************************************************************************
/*
 * Created on 19-May-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.pv.core;

import org.pv.plugin.Copyright;


//import com.ibm.compat.c;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StopWatch {
	private Utils utils=Utils.getSingleton();
	private boolean paused=false;
	double lastElapsed=0;
	double totalElapsed=0;
	long lastStart=0;
	public StopWatch() {startOver();}
	private void restart() {lastStart=System.currentTimeMillis(); paused=false;}
	public void startOver() {totalElapsed=lastElapsed=0;restart();}
	public StopWatch start() {startOver();return this;}
	public void resume() {if (!paused) {totalElapsed+=getElapsed();} restart();}; 
	public void pause() {if (!paused) {lastElapsed=getElapsed();totalElapsed+=lastElapsed; paused=true;}}
	public double getElapsed() {if (paused) {return lastElapsed;} else {return (System.currentTimeMillis()- lastStart)/1000.0;} }
	public double getTotalElapsed() {if (paused) {return totalElapsed;} else {return (totalElapsed + getElapsed());} }
	public String toString() {return ""+getElapsed();}
	
	public void run() {
		StopWatch s = new StopWatch();
		utils.log(s);
		utils.log(""+lastStart);
		utils.sleep(1560);
		utils.log(s);
		utils.log(""+s.getTotalElapsed());
		utils.sleep(1200);
		utils.log(s);
		s.startOver();
		utils.log(s);
		utils.log(""+s.getTotalElapsed());
		
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
