package com.ibm.pbe.patterns;

import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.NewExampleAction;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import com.ibm.dptk.patternWizard.PatternApplicationStatus;

//public class P4ebEditAction implements IWorkbenchWindowActionDelegate {
public class WcdlDeabstracter implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	private ISelection sel;
	private Element ports;
	private Element components;
	private Element logic;
	
	public void run() {
//		Test the Action by setting up any required input here, eg:
		IAction a=new NewExampleAction();
		a.setId("com.ibm.pbe.tdmm.dummy.001");
		
		run(a);
//		Now Use Ctl-Alt-R to run, Ctl-Alt-M to add to Tools menu, Ctl-ALT-= to rerun last action in Tools menu
	}
	
	public void run(IAction action) {
		utils.log("Running Action: "+this.getClass().getName()+" "+new Date());
		StopWatch timer = utils.getTimer();
		IFile inFile=utils.getActiveFile();
		if (!utils.isFileTypeIn(inFile,"wcdl,wcdl.appdef")) {utils.log("Please select or edit a wcdl file and try again");return;}
		Element app=utils.getDocRoot(inFile); Element module=null;
		if (app.getNodeName().equals("pbe.wrapper")) {module=utils.getFirstChild(app);} else {module=app;}
		
		
		logic=utils.getFirstElementAt(module,"LOGIC");
//		compactLogic(logic);
//		compactSCA(module);
		utils.dump(app);
		IFile out=utils.getFile("/.temp/test.wcdl");
		utils.save(app,out);
		utils.edit(out,Utils.XMLEDITOR);
		utils.log("Action complete: "+this.getClass().getName()+" at "+new Date()+" -- Total elapsed="+timer.getTotalElapsed() );
	}
	public void decompact(Element module) {
		utils.log("Module tag="+module.getNodeName());
		if (!module.getNodeName().equals("MODULE")) {utils.log("Module Element passed to abtracter is actually: "+module.getNodeName());return;}
		decompactSCA(module);
		logic=utils.getFirstElementAt(module,"LOGIC");
		decompactLogic(logic);
	}
	public void decompactSCA(Element module) {
		if (true) {return;}
		ports=utils.getFirstElementAt(module,"PORTS");
		components=utils.getFirstElementAt(module,"COMPONENTS");
		List<Element> portElts = utils.getChildElements(ports);
		for (Element portElt:portElts) {
			utils.log("Processing port");
			if (portElt.getNodeName().equals("export")) {
				utils.log("Processing export");
				List<Element> qosElts = utils.getChildElements(portElt,"qosx");
				portElt.setAttribute("qos", "custom");
				if (qosElts.size()==0) {portElt.setAttribute("qos", "default");} 
			} else
			if (portElt.getNodeName().equals("import")) {
				utils.log("Processing import");
				List<Element> qosElts = utils.getChildElements(portElt,"qosi");
				portElt.setAttribute("qos", "custom");
				if (qosElts.size()==1) {
					Element qos=qosElts.get(0);
					if (qos.getAttribute("type").equals("scdl:JoinTransaction") && qos.getAttribute("value").equals("false") ) {
						portElt.setAttribute("qos", "default");
						portElt.setTextContent("");
//						Node lf=qos.getNextSibling();
////						utils.log("lf="+lf);
//						if (lf!=null) {portElt.removeChild(lf);}
//						portElt.removeChild(qos);
					}
				}
			}
		}
		
		List<Element> compElts = utils.getChildElements(components);
		for (Element compElt:compElts) {
			if (compElt.getNodeName().equals("mediation")) {
				utils.log("Processing mediation comp");
				List<Element> qosElts = utils.getChildElements(compElt,"qosc");
				compElt.setAttribute("qos", "custom");
				if (qosElts.size()==1) {
					Element qos=qosElts.get(0);
					if (qos.getAttribute("type").equals("scdl:Transaction") && qos.getAttribute("value").equals("global") ) {
						compElt.setAttribute("qos", "default");
//						compElt.setTextContent("");
						Node lf=qos.getNextSibling();
						compElt.removeChild(qos);
						if (lf!=null && lf.getNodeType()==Node.TEXT_NODE) {compElt.removeChild(lf);}
					}
				}
				List<Element> ifElts = utils.getChildElements(compElt,"interface");
				for (Element ifElt:ifElts) {
					utils.log("Processing mediation iface");
					qosElts = utils.getChildElements(ifElt,"qosi");
					ifElt.setAttribute("qos", "custom");
					if (qosElts.size()==1) {
						Element qos=qosElts.get(0);
						if (qos.getAttribute("type").equals("scdl:JoinTransaction") && qos.getAttribute("value").equals("true") ) {
							ifElt.setAttribute("qos", "default");
							ifElt.setTextContent("");
//							Node lf=qos.getNextSibling();
//							ifElt.removeChild(lf);
//							ifElt.removeChild(qos);
						}
					}
				}
				List<Element> refElts = utils.getChildElements(compElt,"reference");
				for (Element refElt:refElts) {
					utils.log("Processing mediation ref");
					qosElts = utils.getChildElements(refElt,"qosr");
					refElt.setAttribute("qos", "custom");
					if (qosElts.size()==3) {
						int score=0;
						for (Element qos:qosElts) {
						if (qos.getAttribute("type").equals("scdl:SuspendTransaction") && qos.getAttribute("value").equals("false") ) {score++;}
						else if (qos.getAttribute("type").equals("scdl:Reliability") ) {score++;}
						else if (qos.getAttribute("type").equals("scdl:DeliverAsyncAt") && qos.getAttribute("value").equals("commit") ) {score++;}
						}
						if (score==3) {
							refElt.setAttribute("qos", "default");
							refElt.setTextContent("");
//							for (Element qos:qosElts) {Node lf=qos.getNextSibling();utils.log("lf");if(lf!=null) {refElt.removeChild(lf);};utils.log("ref");refElt.removeChild(qos);}
						}
					}
				}
			} // end mediation
		}
	}


	public void decompactLogic(Element logic) {
		List<Element> compLogicElts = utils.getChildElements(logic);
		for (Element compLogicElt:compLogicElts) {
			List<Element> flowElts = utils.getChildElements(compLogicElt,"flow");
			for (Element flowElt:flowElts) {
				Element nodesElt=utils.getFirstElementAt(flowElt,"nodes");
				List<Element> nodeElts = utils.getChildElements(nodesElt);
				for (Element nodeElt: nodeElts) {
					String name=nodeElt.getNodeName();
//					if ("InputResponse,CalloutResponse".contains(name) || nodeElt.getAttribute("name").equals(nodeElt.getAttribute("displayName")) ) {nodeElt.removeAttribute("name");}
					String type;Element t;
					if (nodeElt.hasAttribute("in")) {t=utils.addElement(nodeElt,"inTerminal");t.setAttribute("name","in");t.setAttribute("type","Composite"+nodeElt.getAttribute("in"));nodeElt.removeAttribute("in");
						if ("InputResponse,Callout".contains(name)) {t.setAttribute("explicitType", "true");}
					}
					if (nodeElt.hasAttribute("out")) {t=utils.addElement(nodeElt,"outTerminal");t.setAttribute("name","out");t.setAttribute("type","Composite"+nodeElt.getAttribute("out"));nodeElt.removeAttribute("out");
						if ("Input,CalloutResponse".contains(name)) {t.setAttribute("explicitType", "true");}
					}
					if (nodeElt.hasAttribute("Failure")) {t=utils.addElement(nodeElt,"outTerminal");t.setAttribute("name","Failure");t.setAttribute("type","Composite"+nodeElt.getAttribute("Failure"));nodeElt.removeAttribute("Failure");
						if ("CalloutResponse".contains(name)) {t.setAttribute("explicitType", "true");}
					}
				
//					utils.log("Terminal Child nodes="+nodeElt.getChildNodes().getLength());
//					utils.log("Terminal Child elts="+utils.getChildElements(nodeElt).size());
					if (utils.getChildElements(nodeElt).size()==0) {nodeElt.setTextContent("");}
				}
				Element wiresElt=utils.getFirstElementAt(flowElt,"wires");
				List<Element> wireElts = utils.getChildElements(wiresElt);
				utils.log("Decompacting wires");
				for (Element wireElt: wireElts) {
					utils.log("Decompacting single wire");
					if (!wireElt.hasAttribute("srcT")) {wireElt.setAttribute("srcT","out");}
					if (!wireElt.hasAttribute("tgtT")) {wireElt.setAttribute("tgtT","in");}
				}	
			}
		}
	}
	
	
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel=selection;
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
