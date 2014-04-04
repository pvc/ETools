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
 * Created on 12-Feb-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.pbe.prefs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.prefs.PBEDialog;

/**
 * @author administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EZDialog extends PBEDialog {
	static final Utils utils=Utils.getSingleton();
//	final PBEDialog dialog=new PBEDialog();
	boolean modal=true;
	boolean resizable=true;
	private Document doc=utils.getNewDocument("properties");
	private Element root=doc.getDocumentElement();
	private Map fields=new HashMap();
	String NOHELPMSG="Sorry - No help has been provided for this field";
	String DEFAULTLABEL="Please provide the following input\nPlace the mouse over any field label for help";
	
	
	public EZDialog() {
		root.setAttribute("title","PBE Input Dialog");
		root.setAttribute("label",DEFAULTLABEL);
	}
	public EZDialog(String title) {
		root.setAttribute("title",title);
		root.setAttribute("label",DEFAULTLABEL);
	}
	
	public void addField(String name) {
		Element field=utils.addElement(root,"property");
		field.setAttribute("name",name);
		field.setAttribute("help",NOHELPMSG);
		fields.put(name,field);
	}
	public void setAdditionalText(String text) {
		root.setAttribute("label",text);
	}
	public Document show() {
		execute(doc);
		utils.dump(doc);
		return doc;
	}
	public void run() {
//		setAdditionalText("My message \nto you \n...");
//		doc=utils.getDoc("PVTools/NewSWDialog.xml");
//		root.setAttribute("title","hijojojoijtitle");
//		doc.getDocumentElement().setAttribute("label","hijojojoijtitle");
		addField("fred");
		setFieldAttribute("fred","help","Fred's a nice guy\nBut don't mess with him!");
		setFieldAttribute("fred","value","Fred's a nice guy\nBut don't mess with him!");
		utils.dump(doc);
		int result = this.execute(doc);
		utils.log(""+result);
		if (result==Window.OK) {utils.log("Success");}
		utils.dump(doc);
//		utils.dump("Equality: "+(doc==doc2));
	}
	/**
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void setFieldAttribute(String fieldName, String fieldAttribute, String attributeValue) {
		Element e = (Element)fields.get(fieldName);
		if (e==null) {utils.log("Error - attempt to set value on unknown field: "+fieldName);return;}
		e.setAttribute(fieldAttribute,attributeValue);
	}
	/**
	 * @param string
	 */
	public String getFieldValue(String fieldName) {
		String value=prefs.getString(fieldName);
		utils.log("Returning field value: "+fieldName+'='+value);
		return value;
//		Element f=(Element)fields.get(fieldName);
//		if (f==null) {return "";} else {return f.getAttribute("value");}
	}
	/**
	 * @param exists
	 */
	static String copyright() { return Copyright.PV_COPYRIGHT; }
	
}
