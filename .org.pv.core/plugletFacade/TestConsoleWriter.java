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
import org.pv.core.Utils;
 


public class TestConsoleWriter    {
	
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
		utils.log(System.out);
		utils.log(System.err);
//		System.out=p4eb.getLogger();
//		ConsolePrintStream x;
		System.out.println("Hello World");
//		PrintWriter p = p4eb.getLogger();
//		p.println(240);
//		p.println(false);
//		p.println();
//		Object o=null;
//		p.println(o);
//		p.println("Text");
//		IOException e=new IOException("Nasty problem!");
//		try {throw e;} catch (IOException e2) {p4eb.p("Exception");e2.printStackTrace(p);}
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}