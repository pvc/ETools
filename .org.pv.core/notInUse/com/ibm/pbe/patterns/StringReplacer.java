//*****************************************************************************
//* Licensed Materials - Property of IBM
//*
//* com.ibm.pbe.patterns
//*
//* (C) Copyright IBM Corp. 2008
//*
//* US Government Users Restricted Rights - Use, duplication or
//* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
//*****************************************************************************
package com.ibm.pbe.patterns;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/****************************************************************************/
public class StringReplacer {
	Utils utils=Utils.getSingleton();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String tgtString; String newString;
	Filter root=new Filter("/",false);

	/****************************************************************************/	
	class Visitor implements IResourceVisitor {
		public boolean visit(IResource r) {
			if (r.getType()==IResource.ROOT) {return true;}
			boolean match=root.match(r);
			return match;
		}
	}
	/****************************************************************************/	
	public void run() {
		Document doc=null;
		doc = utils.getDoc(utils.getActiveFile());
		if (doc==null) {utils.log("Please select a Change Definition File and retry");return;}
		utils.log("Content of Change macro is ...");
		utils.dump(doc);
		Element rootElement = doc.getDocumentElement();
		List<Element> elts = utils.getChildElements(rootElement,"filter");
		addFilters(root,elts);
		try {
			utils.wsr.accept(new Visitor());
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
	}
	/****************************************************************************/	
	private void addFilters(Filter parent, List<Element> elts) {
		for (Iterator<Element> iter = elts.iterator(); iter.hasNext();) {
			Node node = iter.next();
			if ("filter".equals(node.getNodeName())) {
				addFilter((Element)node,parent);
			}
		}
	}
	/****************************************************************************/	
	private void addFilter(Element e, Filter parent) {
		Filter f=parent.createSubfilter(e.getAttribute("path"), "true".equals(e.getAttribute("regex")));
		utils.log("Adding filter "+f.getPath()+" to "+parent.getPath());
		List<Element> elts;
		elts = utils.getChildElements(e,"change");
		for (Iterator<Element> iter = elts.iterator(); iter.hasNext();) {
			Element change = iter.next();
			utils.log("Adding change from "+change.getAttribute("from")+" to "+change.getAttribute("to"));
			f.addChange(change.getAttribute("from"),change.getAttribute("to"),"true".equals(change.getAttribute("regex")));
		}
		elts = utils.getChildElements(e,"filter");
		addFilters(f,elts);
	}
	/****************************************************************************/	
	private void doFiles(Element e) {
		if ("files".equals(e.getNodeName())) {
			String pathString=e.getAttribute("path");
			IPath path = new Path(pathString).makeAbsolute();
//			if (!path.isAbsolute()) {path.makeAbsolute();}
			utils.dump(path+" valid="+path.isValidPath(pathString));
			utils.dump(path.segment(0));

		}
	}
	/****************************************************************************/	
	private void doFileUpdates(IContainer f) {
//		utils.p("Doing file updates for: "+f);
		IResource[] mbrs;
		try {
			mbrs = f.members();
			
			for (int i = 0; i < mbrs.length; i++) {
				IResource r = mbrs[i];
				//utils.p("Processing: "+r);
				switch (r.getType()) {
				case IResource.FILE:
					doFileUpdate((IFile)r); break;
				case IResource.FOLDER: 
				case IResource.PROJECT: 
				case IResource.ROOT: 
					doFileUpdates((IContainer)r); break;
				default: break;
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
	}
	/****************************************************************************/	
	private void doFileUpdate(IFile f) {
		applyChange(f,tgtString,newString);
	}
	/****************************************************************************/	
	private void applyChanges(IResource tgt, List<ChangeDef> changes) {
		if (utils.isFile(tgt)) {
			utils.p("Applying changes to: "+tgt);
			for (ChangeDef change: changes) {
				applyChange((IFile)tgt, change.getFrom(),change.getTo());
			}
		}
		else {
			try {
				for (IResource mbr: ((IContainer)tgt).members()) {applyChanges(mbr,changes);}
			} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
		}
	}
	/****************************************************************************/	
	private void applyChange(IFile f, String from, String to) {
		String ext=f.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))||("jar".equals(ext))) {return;} // ignore these
		if (!f.exists()) {return;}
		IDocument d=null;
		try {
			FileEditorInput fi = new FileEditorInput(f);
			tp.connect(fi);
			d=tp.getDocument(fi);
			if (d.getLength()>0) {try {
				FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
				for (int start = 0;;) {
					IRegion r=fr.find(start,from,true,true,false,false);
					if (r==null) {break;} 
					fr.replace(to,false);
					start=r.getOffset()+r.getLength();
				}
				tp.saveDocument(null,fi,d,true);
			} catch (BadLocationException e) {e.printStackTrace(utils.getLogger());}
			} // end doclength>0
			tp.disconnect(fi);
		} catch (CoreException e) {e.printStackTrace(utils.getLogger());}
	}
	/****************************************************************************/	
	public class Filter {
		String path="/";
		
		boolean regex=false;
		List<Filter> subFilters=new ArrayList<Filter>();
		List<ChangeDef> changes=new ArrayList<ChangeDef>();
		
		private Filter(String path, boolean regex) {this.path=path; this.regex=regex;}
		
		public void addChange(String from,String to, boolean regex) {
			changes.add(new ChangeDef(from, to,regex));
		}
		public Filter createSubfilter(String path, boolean regex) {
			String newPath = new Path(this.path).append(path).removeTrailingSeparator().toString();
			Filter f=new Filter(newPath,regex);
			subFilters.add(f);
			return f;
		}
		public boolean match(IResource r) {
			String inPath=r.getFullPath().toString();
			return subMatch(inPath,r);
		}
		private boolean subMatch(String inPath, IResource r) {
//			utils.log("Filtering:"+inPath);
			boolean match=false;
			for (Filter filter: subFilters) {
				match=match | filter.match(inPath,r);
			}
			return match;
		}

		private boolean match(String inPath,IResource r) {
			boolean match=false;
			if (inPath.startsWith(path)) {
				match=true; // no exclude processing currently
				if (subFilters.size()>0) {subMatch(inPath,r);} //allow subFilters precedence
				if (utils.isFile(r)) {applyChanges(r, changes);}  
			}
			else if (path.startsWith(inPath)) { // this is a filter on some content of inpath - signal interest
				match=true;
			}
			return match;
		}

		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public boolean isRegex() {
			return regex;
		}
		public void setRegex(boolean regex) {
			this.regex = regex;
		}
	}
	/****************************************************************************/	
	
	/****************************************************************************/	
}
