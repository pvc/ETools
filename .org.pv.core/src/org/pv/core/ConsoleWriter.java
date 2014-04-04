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
 * Created on 29-Dec-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.pv.core;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;

import org.eclipse.ui.console.MessageConsoleStream;
import org.pv.plugin.Copyright;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConsoleWriter extends Writer {
	MessageConsoleStream mcs;
	public ConsoleWriter(MessageConsoleStream mcs) {
		this.mcs=mcs;
	}
	
	/* (non-Javadoc)
	 * @see java.io.Writer#write(char[], int, int)
	 */
	public void write(char[] cbuf, int off, int len) throws IOException {
		mcs.print(new String(cbuf,off,len));
	}

	/* (non-Javadoc)
	 * @see java.io.Writer#flush()
	 */
	public void flush() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.io.Writer#close()
	 */
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
