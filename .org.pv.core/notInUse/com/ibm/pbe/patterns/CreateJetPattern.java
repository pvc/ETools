package com.ibm.pbe.patterns;

//Uses adapter to gen profile from diagram
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.rsa.interfaces.Runner;
/* Knows how to:
 * Get a list resourceRoots as input
 * Get a simple patternName & create plugin name
 * Create container project 
 * Use a templatiser to populate with specific templates - supplying stdEdits for each resourceRoot 
 * Use a pattern to set up pattern generic templates (controller, dump, tma etc)
 * Finalise
 */

// Notes: Pattern just deletes all existing templates at present - could do with keeping anything whcih does not need overwriting!
public class CreateJetPattern  implements IWorkbenchWindowActionDelegate, Runner {
	Utils utils=Utils.getSingleton();
	String patternSimpleName;
	String projName;
	String nameSuggestion; //rootContainer - may be be multi
	String nameSuggestionL1;
	String nameSuggestionL;
	List sourcePaths=new LinkedList();
//	TextFileDocumentProvider tp = new TextFileDocumentProvider();
//	String nameSuggestionVar="<attr node=\"entry\" name=\"name\" />";
//	String nameSuggestionVarlc1="<attr node=\"entry\" name=\"name\" format=\"L1\"/>";
//	String nameSuggestionVarlc="<attr node=\"entry\" name=\"name\" format=\"L\"/>";
	boolean guessPaths=true;
	List<IResource> resourceList;
	IPath templatePath;
	Element genLogic;
//	Element wcdl;
	private TemplatiserJetBase templatiser;
	private static final String mediationModule="com.ibm.wbit.sib.ui.logicalview.model.MediationModule";
	private static final String module="com.ibm.wbit.ui.logicalview.model.Module";
	ArrayList<ChangeDef> stdEdits = new ArrayList<ChangeDef>();
	ArrayList<ChangeDef> wcdlEdits = new ArrayList<ChangeDef>();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	
	public Object run() {execute();return null;}
	
	
	private List<ChangeDef> getEdits(IFile workFile) {
		utils.log("getedits for "+workFile);
		if (true) {return stdEdits;}
		if ("wcdl".equals(workFile.getFileExtension())) {
			Element root = utils.getDocRoot(workFile);
			Element oldWsdls=utils.getFirstElementAt(root,"WSDLS");
			Element newWsdls = root.getOwnerDocument().createElement("WSDLS");
			root.replaceChild(newWsdls,oldWsdls);
//			List<Element> wsdls = utils.getChildElements(wsdlroot);
//			int count=1;
//			for (Element wsdl:wsdls) {
//				wsdl.removeAttribute("portType");
//				wsdl.removeAttribute("portTypeNS");
//				wsdl.setAttribute("location","WSDLSTART"+wsdl.getAttribute("id")+"WSDLEND");
//			}
			Element portroot=utils.getFirstElementAt(root,"PORTS");
			List<Element> ports = utils.getChildElements(portroot);
			for (Element port:ports) {
				port.removeAttribute("portType");
				port.removeAttribute("portTypeNS");
				port.removeAttribute("port");
				port.removeAttribute("portNS");
				port.removeAttribute("service");
				port.removeAttribute("serviceNS");
				port.removeAttribute("address");
			}
//			exp.setAttribute("wsdl","inWsdl");
//			Element imp=utils.getFirstElementAt(root,"import");
//			imp.setAttribute("wsdl","outWsdl");
//			Element med=utils.getFirstElementAt(root,"mediation");
//			for (Element map:utils.getChildElements(med,"map")) {
//				med.removeChild(map);
//			}
//			utils.addElement(med,"map");
			utils.save(root,workFile);
			return wcdlEdits;
		}
		else {return stdEdits;}
		
	}
	public Object execute(Object o) {execute(); return null;}
	public void execute() {
		List selobs = utils.getBaseSelectionList();
		IResource rsel;IResource source=null;
		resourceList=new ArrayList<IResource>();
		for (Iterator iter = selobs.iterator(); iter.hasNext();) {
			Object selob = iter.next();
			
		if (selob!=null) {
			if (selob instanceof IAdaptable) {
				rsel=(IResource)((IAdaptable)selob).getAdapter(IResource.class);
				if ((rsel!=null)&&(IResource.PROJECT==rsel.getType())) {source=(IProject)rsel;utils.log("Selected: "+rsel.getName());}
				if (rsel!=null) {resourceList.add(rsel);}
			} 
//			if (selob.getClass().getName().equals(mediationModule)) {
//				try {
//					resourceList.add((IProject)selob.getClass().getMethod("getParentProject",null).invoke(selob,null));
//				} catch (Exception e) {}
//			}
		}
		}
		
		if (resourceList.size()==0) {utils.p("Please select one or more resources and retry!"); return;}
		utils.log("Resources selected="+resourceList.size());
//		if (true) {utils.p("Succesfully located Project: "+source.getName());return;}
		source=resourceList.get(0);
//		try {
//			if ( (!utils.isProject(source)) || !((IProject)source).hasNature("com.ibm.wbit.project.generalmodulenature") ) {utils.p("Please select a Mediation Module & retry!"); return;}
//		} catch (CoreException e) {e.printStackTrace(utils.getLogger()); return;}
		utils.log("Selected: "+source);
//		IProject inProject=(IProject)source;
		if (utils.isContainer(source)) {
		String[] segs = source.getName().split("\\.");
		nameSuggestion=segs[segs.length-1];
		} else {
			nameSuggestion=utils.substringBeforeOrAll(source.getName(),".");
		}
		nameSuggestionL1=utils.lc1(nameSuggestion);
		nameSuggestionL=nameSuggestion.toLowerCase();
//		setnameSuggestion(nameSuggestion);
		
		templatiser=new TemplatiserJetBase(this);
		int projCount=0;	
		if (resourceList.size()>1) {
			for (IResource r:resourceList) {if (utils.isProject(r)) {projCount++;}}
		}
		if (projCount>1) {templatiser.setMultiProjects(true);} else {templatiser.setMultiProjects(false);}
		patternSimpleName = utils.askForInput("New Pattern from Selected Resources", "Enter name of new pattern", nameSuggestion);
		if (patternSimpleName==null || patternSimpleName.length()==0) {return;}
		utils.p("Creating Pattern: "+patternSimpleName);
		String patternId="com.ibm.pbe.patterns."+patternSimpleName.toLowerCase()+".jet";
	
/*  Only for WID		
		try {
		wcdl = new ReverseTDSCA().run(inProject);
		utils.save(wcdl.getOwnerDocument(), utils.getFile(inProject,inProject.getName()+".wcdl"));
		} catch (IllegalArgumentException e) {
			utils.log("This module does not conform with current standards - please correct it and try again.");
			utils.log("Reported problem: "+e.getMessage());
			return;
		}
*/		
		Document d=utils.getNewDocument("app");
		Element app=d.getDocumentElement();
		app.setAttribute("name",patternSimpleName);
		app.setAttribute("patternId",patternId);
		app.setAttribute("templateFolder","templates");
//		projName="."+patternSimpleName+"Pattern";
//		app.setAttribute("targetProject",projName); //leave to default
		genLogic=app;
		templatiser.setEditPaths(true);
		templatiser.setPatternName(patternSimpleName);
//		templatiser.setPatternProject(patternId);
		execute(app);
	}

	/**************************************************************************/
	public void execute(Element root) {
		// todo - find source classpathentry & net it off front of resource path for java
		// suppress class files, & frag
		// how to elim timing problems (dont build auto)
		// 
//		utils.dump(root.getOwnerDocument());
		utils.log("Creating pattern project shell for "+root.getAttribute("name"));
//		Document d=root.getOwnerDocument();
//		IStatus st = utils.runJet(root, "com.ibm.pbe.patterns.jetproject.jet");
//		utils.dump(st);
//		if (!st.isSuccessful()) {utils.log("Error Applying Pattern - cannot continue");return;};
//		d=utils.getDocFromString(st.getAfterModel());
		utils.dump(root);  
//		root=d.getDocumentElement();
		
		templatiser.setControlLogic(root);
		projName=root.getAttribute("patternId");
		IPath projPath=new Path("/"+projName);
//		IPath patternPath=projPath.append(root.getAttribute("patternFolder"));
		templatePath=projPath.append(root.getAttribute("templateFolder"));
		utils.log("TemplatePath set to: "+templatePath);
		templatiser.setTemplatePath(templatePath);
//		utils.log("Templatepath="+templatePath);
//		IResource viewFolder=utils.wsr.findMember(viewPath);
//		if (viewFolder!=null) {try {
//			viewFolder.delete(true,null);
//		} catch (CoreException e) {
//			//			e.printStackTrace(); // won't happen - viewFolder variable is null if folder doesn't exist
//		}}
//		utils.p("Copying files ...");
//		copyContent(resourceList,templatePath);
		utils.log("Populating with templates ...");
		templatise();
//		doFileUpdates(utils.wsr.getFolder(templatePath));
		
//		root.setAttribute("update","true");
		utils.dump(root);
//		if (true) {return;}
		/* WID only		
		utils.log("Creating main.jet");
		IStatus st2 = utils.runJet(root,"com.ibm.pbe.patterns.createjetmain.jet"); 
		utils.dump(st2);
		*/
		finalisePattern(root);
		utils.log("Pattern creation complete!");
	}
	private void finalisePattern(Element root) {
		IStatus st = utils.runJet(root, "com.ibm.pbe.patterns.jetproject.jet");
		utils.dump(st);
/* WID only		
		utils.log("Creating sample.xml");
		Element ports = utils.getFirstElementAt(wcdl,"PORTS");
		int nImports = utils.getChildElements(ports,"import").size();
		root.setAttribute("nImports",""+nImports);
		int nExports = utils.getChildElements(ports,"export").size();
		root.setAttribute("nExports",""+nExports);
		IStatus st2 = utils.runJet(root,"com.ibm.pbe.patterns.defaultWIDAppdef.jet"); 
		utils.dump(st2);
*/		
		applyXMLEdits();
		applyTextEdits();
	}

	private void applyTextEdits() {}
	private void applyXMLEdits() {}

	public void templatise() {
		int index=0;
		for (IResource r:resourceList) {
			String tplPath=setup(r,index++);
			templatiser.templatise(r, tplPath);
		}
	}
	
	public String setup(IResource r,int index) {
		IContainer c; String pfx="container";boolean isFile=false; String fileName=null;
		if (utils.isFile(r)) {c=r.getParent();isFile=true;fileName=utils.substringBeforeOrAll(r.getName(),".");} else {c=(IContainer)r;if (utils.isProject(c)) {pfx="project";}}
		String xpath="$model/@"+pfx+index;
		String replace="<c:get select=\""+xpath+"\"/>";
		utils.log("Set edit for "+r+" to: "+replace);
		
//		this.nameSuggestion = nameSuggestion;
//		nameSuggestionL1=utils.lc1(nameSuggestion);
//		nameSuggestionL=nameSuggestion.toLowerCase();
//		String nameSuggestionVar="<c:get select=\"$model/@name\"/>";
//		String nameSuggestionVarL1="<c:get select=\"lowercaseFirst($model/@name)\"/>";
//		String nameSuggestionVarL="<c:get select=\"lower-case($model/@name)\"/>";
		stdEdits.clear();
		stdEdits.add(new ChangeDef(c.getFullPath().makeRelative().toString(),replace));
//		if (isFile) {stdEdits.add(new ChangeDef(fileName,replace2));}
//		stdEdits.add(new ChangeDef(nameSuggestion, nameSuggestionVar));
//		stdEdits.add(new ChangeDef(nameSuggestionL, nameSuggestionVarL));
//		stdEdits.add(new ChangeDef(nameSuggestion, nameSuggestionVar));
//		wcdlEdits.clear();
//		wcdlEdits.addAll(stdEdits);
//		wcdlEdits.add(new ChangeDef("WSDLSTARTIn","<c:get select=\"$entry/@wsdlIn"));
//		wcdlEdits.add(new ChangeDef("WSDLSTARTOut","<c:get select=\"$entry/@wsdlOut"));
//		wcdlEdits.add(new ChangeDef("WSDLEND","\"/>"));
//		wcdlEdits.add(new ChangeDef("<WSDLS/>","<WSDLS><c:iterate select=\"$model/wsdl\" var=\"wsdl\"><c:dump select=\"$wsdl\"/></c:iterate></WSDLS>"));
		 
//		wcdlEdits.add(new ChangeDef("wsdl=\"outWsdl\"","wsdl=\"<c:get select=\"$entry/@outWsdl\"/>\""));
//		wcdlEdits.add(new ChangeDef("<map/>","<CHANGED/>"));
//		wcdlEdits.add(new ChangeDef("<map/>","hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"));
//		wcdlEdits.add(new ChangeDef("<map/>","<c:iterate select=\"$entry/map\" var=\"map\"><map srcOp=\"<c:get select=\"$map/@srcOp\"/>\" tgtOp=\"<c:get select=\"$map/@tgtOp\"/>\"/></c:iterate>"));
		return pfx;
	}
	
	public Object run(Object input) {
		IFile file=(IFile)input;
		List<ChangeDef> changes = getEdits(file);
		if (changes.size()==0) {return null;}
		IDocument d=null;
//		utils.p(workFile.getCharset());
		//		if (true) return;
		
		FileEditorInput fi = new FileEditorInput(file);
		try {
		tp.connect(fi);
		d=tp.getDocument(fi);
		if (d.getLength()==0) { return null;}
			//			utils.p("Doc="+d);
			//			StringBuffer sr = new StringBuffer();
			//			BufferedReader br = new BufferedReader(new InputStreamReader(f.getContents()));
			//			for (;;) {
			//				sr.append(br.readLine());
			//			}
			//			d=new Document(f.getContents());
			FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
			//IRegion find(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, boolean regExSearch) 
			for (Iterator iter = changes.iterator(); iter.hasNext();) {
				ChangeDef chg = (ChangeDef) iter.next();
//				if (chg.getTarget().equals("<map/>")) {utils.log("Processing mapchange with: "+chg.getReplacement());}
				for (int start = 0;;) {
					IRegion r=fr.find(start,chg.getTarget(),true,chg.isCaseSensitive(),false,chg.isRegex());
					if (r==null) {break;} 
//					if (chg.getTarget().equals("<map/>")) {utils.log("Processing mapchange with: "+chg.getReplacement());utils.log(r);}
					IRegion r2=fr.replace(chg.getReplacement(),false);
//					if (chg.getTarget().equals("<map/>")) {utils.log("rLength="+r.getLength());utils.log(r2);utils.log(r);}
					start=r.getOffset()+r2.getLength();
				}
			}
			
			//			tp.resetDocument(fi);
			
			tp.saveDocument(null,fi,d,true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		tp.disconnect(fi);
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction arg0) {
		execute();
		
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}
}
