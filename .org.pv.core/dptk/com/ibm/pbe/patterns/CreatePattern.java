package com.ibm.pbe.patterns;

//Uses adapter to gen profile from diagram
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;

import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.dptk.patternWizard.PatternApplicationStatus;

import com.ibm.pbe.rsa.interfaces.Executable;


// Notes: Pattern just deletes all existing templates at present - could do with keeping anything whcih does not need overwriting!
public class CreatePattern  implements Executable {
	Utils utils=Utils.getSingleton();
	String sourceName;
	String targetName;
	String rootName;
	String rootNameL1;
	String rootNameL;
	List sourcePaths=new LinkedList();
	TextFileDocumentProvider tp = new TextFileDocumentProvider();
	String rootNameVar="<attr node=\"entry\" name=\"name\" />";
	String rootNameVarlc1="<attr node=\"entry\" name=\"name\" format=\"L1\"/>";
	String rootNameVarlc="<attr node=\"entry\" name=\"name\" format=\"L\"/>";
	
	public void run() {execute(null);}
	public Object execute(Object o) {execute(); return null;}
	public void execute() {
		// todo - find source classpathentry & net it off front of resource path for java
		// suppress class files, & frag
		// how to elim timing problems (dont build auto)
		// 
		
		Object selob = utils.getFirstSelected();
		IResource rsel;IProject source=null;
		if (selob!=null) {
			if (selob instanceof IAdaptable) {
				rsel=(IResource)((IAdaptable)selob).getAdapter(IResource.class);
				if ((rsel!=null)&&(IResource.PROJECT==rsel.getType())) {source=(IProject)rsel;}
			} 
			if (selob.getClass().getName().equals("com.ibm.wbit.ui.logicalview.model.Module")) {
				try {
					source=(IProject)selob.getClass().getMethod("getParentProject",null).invoke(selob,null);
				} catch (Exception e) {}
			}
		}
		
		if (source==null) {utils.p("Please select a Project!"); return;}
//		if (true) {utils.p("Succesfully located Project: "+source.getName());return;}
		
		sourceName=source.getName();
		targetName="."+sourceName+"Pattern";
		//utils.p("Creating Pattern: "+sourceName);
		Document d=utils.getNewDocument("app");
		Element app=d.getDocumentElement();
		app.setAttribute("patternName",sourceName);
		app.setAttribute("patternProject",targetName);
//		add(d.getDocumentElement(),p);
		
		rootName=sourceName; 
		rootNameL1=lc1(rootName);
		rootNameL=rootName.toLowerCase();
		IProject[] projects=utils.wsr.getProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject p=projects[i];
			utils.p("Processing project" +p);
			if (!p.getName().startsWith(rootName)) {continue;}
			utils.p("About to enter sp for: "+p);
			getSourcePaths(p);
			add(d.getDocumentElement(),p);
		}
		
		PatternApplicationStatus st = utils.execDPTK(d,"com.ibm.pv.patterns.createPattern");
		//utils.p("Success="+st.isSuccessful());
		utils.p(st.getStatus());
		
		IPath patternPath=new Path("/"+targetName+"/pattern");
		IPath viewPath=patternPath.append("templates");
		IResource viewFolder=utils.wsr.findMember(viewPath);
		if (viewFolder!=null) {try {
			viewFolder.delete(true,null);
		} catch (CoreException e) {
			//			e.printStackTrace(); // won't happen - viewFolder variable is null if folder doesn't exist
		}}
		utils.p("Copying files ...");
		for (int i = 0; i < projects.length; i++) {
			IProject p=projects[i];
			if (!p.getName().startsWith(rootName)) {continue;}
			IPath root=viewPath.append(p.getName());
			try {
				utils.genFolder(root);
				IResource[] resList = p.members(); // can't copy a project to a non-project
				for (int i2 = 0; i2 < resList.length; i2++) {
					IResource r = resList[i2];
//					utils.p("copying: "+r.getName());
					IPath copyPath=viewPath.append(r.getFullPath());
//					IResource existing=utils.wsr.findMember(copyPath);
//					utils.p("Existsing result: "+existing);
//					if (existing!=null) {
//						existing.delete(true,null);  // DO overwrite - must at present because deep copying FOLDERS, not indinvid files
//						utils.p("Deleted: "+copyPath);
////						continue; // DON'T overwrite 
//						}
					r.copy(copyPath,true,null);
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
			
		}
		doFileUpdates(utils.wsr.getFolder(viewPath));
		utils.log("Pattern creation complete!");
	}

	/**
	 * @param root
	 */
	private void doFileUpdates(IFolder f) {
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
					doFileUpdates((IFolder)r); break;
				default: break;
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
	}

	/**
	 * @param file
	 */
	private void doFileUpdate(IFile f) {
		//utils.p("Doing file update on: "+f);
		String ext=f.getFileExtension();
		if (("class".equals(ext))||("frag".equals(ext))||("jar".equals(ext))) {return;} // ignore these
		IDocument d=null;
		try {
			//		utils.p(f.getCharset());
			//		if (true) return;
			
			FileEditorInput fi = new FileEditorInput(f);
			tp.connect(fi);
			d=tp.getDocument(fi);
			if (d.getLength()>0) {try {
				//			utils.p("Doc="+d);
				//			StringBuffer sr = new StringBuffer();
				//			BufferedReader br = new BufferedReader(new InputStreamReader(f.getContents()));
				//			for (;;) {
				//				sr.append(br.readLine());
				//			}
				//			d=new Document(f.getContents());
				FindReplaceDocumentAdapter fr = new FindReplaceDocumentAdapter(d);
				//IRegion find(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive, boolean wholeWord, boolean regExSearch) 
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootName,true,true,false,false);
					//utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVar,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc,false);
					start=r.getOffset()+r.getLength();
				}
				for (int start = 0;;) {
					//				utils.p("Int:"+start);
					IRegion r=fr.find(start,rootNameL1,true,true,false,false);
					//				utils.p("Pos: "+((r!=null)?""+r.getOffset():"-1"));
					if (r==null) {break;} 
					fr.replace(rootNameVarlc1,false);
					start=r.getOffset()+r.getLength();
				}
				//			tp.resetDocument(fi);
				tp.saveDocument(null,fi,d,true);
				
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
			} // end doclength>0
			tp.disconnect(fi);
			
			IPath newPath=f.getFullPath().addFileExtension("pat");
			IResource temp=utils.wsr.findMember(newPath);
			if (temp!=null) {temp.delete(true,null);}
			f.move(newPath,false,true,null);
			
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(utils.getLogger());
		}
		
	}

	/**
	 * @param p
	 */
	private void getSourcePaths(IProject p) {
		sourcePaths.clear();
		IJavaProject jp = JavaCore.create(p);
		if (jp==null) {return;}
		utils.p("Getting sourcepaths for: "+jp);
		IClasspathEntry[] cpes;
		try {
			jp.open(null); // not necessary?
			cpes = jp.getRawClasspath();
		
			for (int i = 0; i < cpes.length; i++) {
				IClasspathEntry cpe = cpes[i];
//				utils.p(""+cpe.getEntryKind()+":"+cpe.getPath());
				if (cpe.getEntryKind()!=IClasspathEntry.CPE_SOURCE) {continue;}
				sourcePaths.add(cpe.getPath().removeFirstSegments(1).toString());
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(utils.getLogger());  // exception thrown for non-Java project or EAR file - ignore
		}
		
	}

	/**
	 * @param d
	 * @param p
	 */
	private void add(Element d, IResource r) {
		Element n;IResource[] mbrs;
//		p("Adding: "+r);
			try {
		switch (r.getType()) {
		case IResource.PROJECT:
			n = d.getOwnerDocument().createElement("project");
			d.appendChild(n);
			n.setAttribute("name",r.getName());
			n.setAttribute("suffix",r.getName().substring(sourceName.length()));
			mbrs = ((IContainer)r).members();
			for (int i = 0; i < mbrs.length; i++) {
				add(n,mbrs[i]);
			}
			break;
		case IResource.FILE:
			String ext=r.getFileExtension();
		//utils.p("Processing file: "+r+", ext="+ext);
			if (("class".equals(ext))||("frag".equals(ext))) {break;} // ignore these
		//utils.p("Continuing!");
			String fileType;
			if ("jar".equals(ext)) {fileType="bin";} else {fileType="file";}
			n = d.getOwnerDocument().createElement(fileType);
			d.appendChild(n);
//			n.setAttribute("name",r.getName()); 
			String path=r.getProjectRelativePath().toString();
			n.setAttribute("source",path);
			// next few lines handle dptk oddity of treatinng java source file paths differently from other directories
			if ("java".equals(ext)) {
				for (Iterator iter = sourcePaths.iterator(); iter.hasNext();) { 
					String sp = (String ) iter.next();
					if (sp.length()==0) {continue;}
					if (path.startsWith(sp)) {path=path.substring(sp.length()+1);break;} //take off '/'
				}
			}
			n.setAttribute("target",subVar(path));
			break;
		case IResource.FOLDER:
			n = d.getOwnerDocument().createElement("folder");
			d.appendChild(n);
//			n.setAttribute("name",r.getName());
			n.setAttribute("name",subVar(r.getProjectRelativePath().toString()));
			
			mbrs = ((IContainer)r).members();
			for (int i = 0; i < mbrs.length; i++) {
				add(d,mbrs[i]);
			}
			break;

		default:
			break;
		}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
			}
		
	}
	
	/**
	 * @param string
	 * @return
	 */
	private String subVar(String s) {
		return s.replaceAll(rootName,"%entry(name)%").replaceAll(rootNameL,"%entry(nameL)%").replaceAll(rootNameL1,"%entry(nameL1)%");
	}

	public String lc1(String s) {
		return s.substring(0,1).toLowerCase()+s.substring(1);
	}
}
