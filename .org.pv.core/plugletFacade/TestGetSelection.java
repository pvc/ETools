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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.uml2.uml.Element;


import org.eclipse.gmf.runtime.notation.View;
import org.pv.core.Utils;
 


public class TestGetSelection    {
	
	Utils utils=Utils.getSingleton();
	
	public void plugletmain(String[] args) {
		utils.log("***************************************");

		IResource r=utils.getSelectedResource();
		utils.p(r);
		utils.p(utils.getSelectedUMLElement());
		utils.p(utils.getBaseSelectionList());
		Object o=utils.getBaseSelectionList().get(0);
		utils.log("Adaptable= "+(o instanceof IAdaptable));
//		Element e=(Element)((IAdaptable)o).getAdapter(Element.class);
		View v=(View)((IAdaptable)o).getAdapter(View.class);
		utils.log(v);
//		utils.log("Part: "+wp);
//		utils.log("isRN="+(wp instanceof ResourceNavigator));
//		utils.log("isTree="+(wp instanceof TreeViewer));
//		Method[] meths = wp.getClass().getMethods();
//		for (int i = 0; i < meths.length; i++) {
//			utils.log(meths[i].toString());
//		}
//		IWorkbenchPage p=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		IWorkbenchPart wp=p.getActivePart();
//		ISelection sel=wp.getSite().getSelectionProvider().getSelection();
//		utils.log("SelClass: "+sel.getClass());
//		if (sel instanceof IStructuredSelection) {
//			Object o=((IStructuredSelection)sel).getFirstElement();
//			utils.log("FirstElement: "+o.getClass());
//			utils.log("Adaptable= "+(o instanceof IAdaptable));
//			IResource r=(IResource)((IAdaptable)o).getAdapter(IResource.class);
//			utils.log("Adapter: "+r);
//			if (r!=null) {utils.log("Type:"+r.getType()+" Name: "+r.getName());}
//			Method[] meths = o.getClass().getMethods();
//			for (int i = 0; i < meths.length; i++) {
//				utils.log(meths[i].toString());
//			}
//		}
//		if (true) {return;}
		
		
		
		
//		View v=(View)elements.get(0);
//		EObject e=v.getElement();
//		utils.log(e);
//		utils.log("IsClass? "+ (e instanceof Class));
//		EList ch=v.getChildren();
//		utils.log(ch);
//		
//		EList atts=((Class)e).getOwnedAttributes();
//		utils.log(atts);
//		if (!(o instanceof View)) {
//			utils.log("Please select a Diagram");
//			return;
//		}
	}
	

	
	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}