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
 * Created on 27-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.p4eb.sdl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.pv.core.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.dptk.patternWizard.PatternApplicationStatus;
import com.ibm.pbe.trees.TreeIterator;
import com.ibm.pbe.trees.TreeNode;
import com.ibm.pbe.trees.TreeNodeFileAdapter;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GenMetadata {
	Utils utils=Utils.getSingleton();
	String source="C:/SDLC/SDLC4ESB/LocalArtifacts";
	String target="C:/SDLC/SDLC4ESB/Metadata";
	String baseUri="../LocalArtifacts";
	
	
	
	public void execute() {
		utils.log("****************************Beginning Test Run");
		Properties sdlViews=new Properties();
		try {
			sdlViews.load(new FileInputStream("C:/MyWorkspaces/MyToolkits/ISSWRSAToolkit/src/com/ibm/p4eb/sdl/sdlViews.txt"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace(utils.getLogger());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace(utils.getLogger());
		}
		
		Document d=utils.getNewDocument("app");
		Element app=(Element) d.getFirstChild();
		app.setAttribute("baseUri",baseUri);
		app.setAttribute("metadataDir",target);
		Element sdl=d.createElement("SDLViews");
		app.appendChild(sdl);
		Collection sdlv = sdlViews.values();
		for (Iterator iter = sdlv.iterator(); iter.hasNext();) {
			String vName = (String) iter.next();
			Element v=d.createElement("SDLView");
			v.setAttribute("name",vName);
			sdl.appendChild(v);
		}
		
		TreeIterator it=new TreeIterator(new TreeNodeFileAdapter(source));
		for (; it.hasNext();) {
			TreeNode tn = (TreeNode) it.next();
			if (!tn.isLeaf()) {continue;} 
			File f=(File)tn.getData();
			IPath relPath = new Path(f.getPath().substring(source.length()));
			IPath sdlPath=relPath.removeLastSegments(1);
			String fullName=f.getName();
			String ext;
			int n=fullName.lastIndexOf('.');
			if (n>0) {ext=fullName.substring(n+1); fullName=fullName.substring(0,n);} else {ext="unknown";}
			String id="temp"+utils.getUniqueString();
			Date date=new Date(f.lastModified());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String sDate=df.format(date);
			Element v=d.createElement("file");
			v.setAttribute("name",f.getName());
			v.setAttribute("id",id);
			v.setAttribute("uri",relPath.toString());
			v.setAttribute("sdlpath",sdlViews.getProperty(sdlPath.toString(),"unclassified"));
			v.setAttribute("name",fullName);
			v.setAttribute("ext",ext);
			v.setAttribute("date",sDate);
			app.appendChild(v);
		}

		IFile f2=(IFile)utils.wsr.getFile(new Path("/GenArtifactDescriptors/gen.appdef"));
		utils.p(f2);
		utils.save(d,f2);
		utils.edit(f2);
		if (true) {return;}
		
		
		
		
		EObject eo=utils.getSelectedUMLElement();
		
		IFile f=utils.getFile(utils.getProjectName(eo)+"/.pattern");
		InputStream is=null;
		utils.log("Getting file contents");
		try {
			is = f.getContents();
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace(utils.getLogger());
		}
		utils.log("Got file contents");
		if (is==null) {return;}
		LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
		boolean stop=false;
		String line=null;
		String id=null;
		while (!stop) {
			try {
			line = lr.readLine();
			if (line.startsWith("SetID:")) {id=line.substring(6); break;}
			stop=!lr.ready();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(utils.getLogger());
				stop=true;
			}
		}
		if (id==null) {utils.log("Cannot find a pattern id in this Project - cannot continue"); return;}
		
		PatternApplicationStatus status = utils.execDPTK(d,id,utils.getProject(eo),null);
		utils.log("DPTK invocation result:");
		utils.log(status.getStatus());
		

	}	
	static String copyright() { return Copyright.PV_COPYRIGHT; }
}
