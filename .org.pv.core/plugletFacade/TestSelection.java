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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.pv.core.Utils;

 


public class TestSelection    {
	
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
		utils.log("***************************************");
		IWorkbenchPage p=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IWorkbenchPart wp=p.getActivePart();
		utils.log("Part: "+wp);
//		utils.log("isRN="+(wp instanceof ResourceNavigator));
//		utils.log("isTree="+(wp instanceof TreeViewer));
//		Method[] meths = wp.getClass().getMethods();
//		for (int i = 0; i < meths.length; i++) {
//			utils.log(meths[i].toString());
//		}
		ISelection sel=wp.getSite().getSelectionProvider().getSelection();
		utils.log("SelClass: "+sel.getClass());
		if (sel instanceof IStructuredSelection) {
			Object o=((IStructuredSelection)sel).getFirstElement();
			utils.log("FirstElement: "+o.getClass());
			utils.log("Adaptable= "+(o instanceof IAdaptable));
//			IResource r=(IResource)((IAdaptable)o).getAdapter(IResource.class);
//			Element r=(Element)((IAdaptable)o).getAdapter(Element.class);
			EObject r=(EObject)((IAdaptable)o).getAdapter(EObject.class);
			utils.log("Adapter: "+r);
//			if (r!=null) {utils.log("Type:"+r.getType()+" Name: "+r.getName());}
			if (r!=null) {utils.log(" Name: "+r);}
//			Method[] meths = o.getClass().getMethods();
//			for (int i = 0; i < meths.length; i++) {
//				utils.log(meths[i].toString());
//			}
		}
		else if (wp instanceof IEditorPart){
			IEditorInput inp = ((IEditorPart)wp).getEditorInput();
			IFile f=(IFile)inp.getAdapter(IFile.class);
			utils.log("EditorInput: "+f);
		}
		if (true) {return;}
		
		
		
//		final List elements = UMLModeler.getUMLUIHelper().getSelectedElements();
//		if (elements.isEmpty()) {
//			utils.log("No selection made");
//			return;
//		}
//		for (Iterator iter = elements.iterator(); iter.hasNext();) {
//			Object o = (Object) iter.next();
//			utils.log(o);
//			
//		}
//		View v=(View)elements.get(0);
//		EObject e=v.getElement();
//		utils.log("ViewElement: "+e);
//		utils.log("IsClass? "+ (e instanceof Class));
//		EList ch=v.getChildren();
//		utils.log("View Children: "+ch);
		
//		EList atts=((Class)e).getOwnedAttributes();
//		utils.log(atts);
//		if (!(o instanceof View)) {
//			utils.log("Please select a Diagram");
//			return;
//		}
//		com.ibm.xtools.modeler.ui.ui.views.internal.providers.content.FileViewerElement();
	}

	static String copyright() { return Copyright.PV_COPYRIGHT; }
}