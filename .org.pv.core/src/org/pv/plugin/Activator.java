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
package org.pv.plugin; 

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;
import org.pv.core.Utils;




import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin implements IStartup {
//public class Activator extends AbstractUIPlugin {
//public class Activator extends AbstractUIPlugin {
	//The shared instance.
	private static Activator plugin;
	private static final String PLUGINID="org.pv.core";
	public static final String COREPACKAGE=PLUGINID;
	public static final String REPEAT = COREPACKAGE+".Repeat";
	public static final String RUNNER = COREPACKAGE+".PVLoader";
	public static final String LOADER = RUNNER+"$MyLoader";
	static final String PLUGINNATURE = "org.eclipse.pde.PluginNature";
	//Resource bundle.
	private ResourceBundle resourceBundle;
	Utils utils=Utils.getSingleton();
	
	/**
	 * The constructor.
	 */
	public Activator() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.pv.core.Plugin");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		String inst=getResourceString("installed");
//		utils=Utils.activate(this);
//		utils=Utils.getSingleton();
		utils.log("PV Eclipse accelerators starting up ...");
		Utils.setActivator(this);
//		utils.log("Plugin="+plugin);
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
//		ClassLoader b = this.getClass().getClassLoader();
//		utils.log(b);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public void earlyStartup() {
		utils.log("PV Eclipse accelerators loaded and ready!");
	}
//	static String copyright() { return Copyright.PV_COPYRIGHT; }

	public static String getPluginId() {
		// TODO Auto-generated method stub
		return PLUGINID;
	}
}
