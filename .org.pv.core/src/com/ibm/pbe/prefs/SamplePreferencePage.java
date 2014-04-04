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
package com.ibm.pbe.prefs;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;
import org.pv.core.Utils;
import org.pv.plugin.Activator;


/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */


public class SamplePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	public static final String P_PATH = "pathPreference";
	public static final String P_BOOLEAN = "booleanPreference";
	public static final String P_CHOICE = "choicePreference";
	public static final String P_STRING = "stringPreference";
	static final Utils utils=Utils.getSingleton();

	public SamplePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
		initializeDefaults();
	}
/**
 * Sets the default values of the preferences.
 */
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(P_BOOLEAN, true);
		store.setDefault(P_CHOICE, "choice2");
		store.setDefault(P_STRING, "Default value");
	}
	public void run() throws BackingStoreException {
		Shell wbShell=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		Shell mainShell=new Shell(wbShell, SWT.SHELL_TRIM);  // = CLOSE, RESIZE, etc
		mainShell.setText("My Title");
		mainShell.setLayout(new FillLayout());
		
		this.createControl(mainShell);
		this.getShell().open();
		if (true) {return;}
		Activator plugin = Activator.getDefault();
		Preferences prefs = plugin.getPluginPreferences();
		utils.dump(prefs.propertyNames());
		utils.log(prefs);
		utils.log("End of dump");
		prefs.setValue("MyValue","fred");
		utils.log("Save="+prefs.needsSaving());
		plugin.savePluginPreferences();
		utils.log("Save="+prefs.needsSaving());
		utils.log(Platform.getConfigurationLocation().getURL());
		IPreferencesService ps = Platform.getPreferencesService();
//		utils.dump(ps.getDefaultLookupOrder("","MyValue"));
		IEclipsePreferences root = ps.getRootNode();
		org.osgi.service.prefs.Preferences child = root.node("default");
		utils.dump(child.childrenNames());
//		org.osgi.service.prefs.Preferences cfg = root.node("configuration");
//		org.osgi.service.prefs.Preferences n2 = cfg.node("com.ibm.wbiserver.runtime.core");
//		utils.log(n2.get(n2.keys()[0],"HHH"));
	}
	
/**
 * Creates the field editors. Field editors are abstractions of
 * the common GUI blocks needed to manipulate various types
 * of preferences. Each field editor knows how to save and
 * restore itself.
 */

	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(P_PATH, 
				"&Directory preference:", getFieldEditorParent()));
		addField(
			new BooleanFieldEditor(
				P_BOOLEAN,
				"&An example of a boolean preference",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
			P_CHOICE,
			"An example of a multiple-choice preference",
			1,
			new String[][] { { "&Choice 1", "choice1" }, {
				"C&hoice 2", "choice2" }
		}, getFieldEditorParent()));
		addField(
			new StringFieldEditor(P_STRING, "A &text preference:", getFieldEditorParent()));
	}
	
	public void init(IWorkbench workbench) {
	}
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}