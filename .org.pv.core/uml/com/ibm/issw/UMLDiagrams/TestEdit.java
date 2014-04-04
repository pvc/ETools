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
package com.ibm.issw.UMLDiagrams;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileInPlaceEditorInput;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import com.ibm.pbe.rsa.interfaces.Executable;
import com.ibm.xtools.modeler.ui.UMLModeler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
import org.pv.plugin.Copyright;

public class TestEdit  implements Executable {
//	public static String configPath="C:/MyWorkspaces/RSAGold/MediationHandlers/src/mediationHandlers/";
//	public static String configPath="C:/MyWorkspaces/RSAGold/ESBHandlers/src/handlers/";
	public static String configPath="C:/MyWorkspaces/Lab089/MediationHandlers/src/mediationHandlers/";
	public static String mediationPath="MediationHandlers/src/mediationHandlers";
	public void plugletmain(String[] args) {
		execute();
	}
	Utils utils=Utils.getSingleton();
	
	public void run() {execute();}
	public Object execute(Object o) {execute();return null;}
	public void execute() {
		final List elements = UMLModeler.getUMLUIHelper().getSelectedElements();
//		utils.p("***************************************");
		if (elements.isEmpty()) {utils.p("Must select a pregenerated diagram element"); return;}
		Object o=elements.get(0);
		if (o instanceof View) {
			View v=(View)o;
			if (v instanceof Diagram) {utils.p("Must select a pregenerated diagram element"); return;}
//			utils.p("Diagram: "+v.getDiagram());
//			o.setVisible(false); //Need write context to do this
			
			//logObject((View)o,"");
			//out.println("Isview: "+o.toString()+":"+o.getClass()); 
			Element object= (Element) v.getElement();  //NB returns null for Diagram/Note (OK for UML Comment)
			if (object==null) {utils.p("Must select a pregenerated diagram element"); return;}
			Element el=object;
			String flowName=v.getDiagram().getName();
			
			Stereotype st=null;
//			utils.p("Applied Stereotypes:");
//			EListsts=el.getAppliedStereotypes();
//			for (Iterator iter = sts.iterator(); iter.hasNext();) {
//				st = (Stereotype) iter.next();
//				utils.p(st.getName());
//			}
//			utils.p("Must be Mediation"); return;
			IWorkspaceRoot wsr = ResourcesPlugin.getWorkspace().getRoot();
			IPath wsrPath = wsr.getLocation();
			
			String name=((NamedElement)el).getName();
			IPath filePath=null;
			
			st=el.getAppliedStereotype("P4ebESBProfile::ESBMediation");
			if (st!=null) {
			String fileName=name+"Handler01POV.java";
//			String fileName=name+"HandlerPOV.java";
			filePath=wsrPath.append(mediationPath).append(fileName);
			}
			
			st=el.getAppliedStereotype("P4ebESBProfile::TestRequester");
			if (st!=null) {
			String fileName=name+".java";
			filePath=new Path("C:/MyWorkspaces/Lab089/ESBTestClient/ejbModule/ejbs/"+"DefaultSessionBean.java");
			}
			
			st=el.getAppliedStereotype("P4ebESBProfile::TestProvider");
			if (st!=null) {
			String fileName=name+"Stub.java";
			filePath=new Path("C:/MyWorkspaces/Lab089/"+"SupplierSelector"+"MDB/ejbModule/ejbs/"+fileName);
			}
			
			utils.p("Selected:"+el.eClass());
			if (filePath==null) {
				if (el instanceof org.eclipse.uml2.uml.Class) {
					String pkgName="ejbs"; //default ejb
					
//					filePath=wsrPath.append(el.getNearestPackage().getName()).append("ejbModule").append("ejbs").append(((NamedElement)el).getName()+"Bean").addFileExtension("java");
					filePath=wsrPath.append(((NamedElement)el).getName()).append("ejbModule").append(pkgName).append(((NamedElement)el).getName()+"Bean").addFileExtension("java");
				}
			}
//			filePath=new Path(configPath+flowName+"/"+name+"POV.java");
			utils.p("Editing POV: "+filePath);
			if (filePath==null) {utils.p("This object has no associated Java class"); return;}
			
//			IFile f=wsr.getFileForLocation(new Path("C:/MyWorkspaces/RSAGold/TestClient/ejbModule/ejbs/DefaultSession.java"));
			
			IFile f=wsr.getFileForLocation(filePath);
			utils.p("Editing file: "+f);
			if (f==null) {utils.p("File must be accessible within this workspace to be edited");return;}
			if (!(f.exists())) {utils.p("This Flow resource has not yet been generated - please run the genESBFlow tool: "+filePath); return;}
			edit(f);
			if (true) {return;}
			
			utils.p("Atts of "+st.getName());
			EList atts=st.getAttributes();
			for (Iterator iter = atts.iterator(); iter.hasNext();) {
				Property att=(Property) iter.next();
				utils.p("ATT: "+att);
				//Property att = (Property) iter.next();
				//Object attvalue=st.eGet(att);
				
			}
		}
		
		if (true) {return;}
		
		

	}
	void edit(IFile f) {
//		JavaCore jc=JavaCore.getJavaCore();
		IWorkspaceRoot wsr = ResourcesPlugin.getWorkspace().getRoot();
//		IFile f=wsr.getFileForLocation(new Path("C:/MyWorkspaces/RSAGold/TestClient/ejbModule/ejbs/DefaultSession.java"));
		ICompilationUnit cu= JavaCore.createCompilationUnitFrom(f);
		if (cu==null) {utils.p("Could not create Java compilation unit from "+f);return;}
        try {
			IEditorPart javaEditor = JavaUI.openInEditor(cu);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        ICompilationUnit cu = member.getCompilationUnit();
//        JavaUI.revealInEditor(javaEditor, member);
		
		
		
		FileInPlaceEditorInput fin=new FileInPlaceEditorInput(f);
//		"org.eclipse.ui.DefaultTextEditor"
    }
	

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
