package org.pv.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class PBEPath extends Path  {
	/**
	 * 
	 */
	private static final Utils utils=Utils.getSingleton();
	private static final PVLoader runner=new PVLoader();
	
	public PBEPath(String fullPath) {
		super((fullPath==null)?"":fullPath);
		makeAbsolute();
	}
	public PBEPath(IFile f) {
		super(f.getFullPath().toString());
	}
	
	public java.lang.Class loadClass() {
		if (segmentCount()<2) {return null;}
		utils.log("Loading "+segment(1));
		java.lang.Class clas = loadProjectClass();
		if (clas!=null) {utils.log("Found in Workspace!");}
		if (clas==null) {clas=loadPluginClass();}
		if (clas!=null) {utils.log("Found in plugins!");}
		return clas;	
	}
	
	public java.lang.Class loadPluginClass() {
		if (segmentCount()<2) {return null;}
		Bundle b=Platform.getBundle(segment(0));
		if (b==null) {return null;}
		try {
			return b.loadClass(segment(1));
		} catch (ClassNotFoundException e) {return null;}
	}
	public java.lang.Class loadProjectClass() {
		if (segmentCount()<2) {return null;}
		return runner.load(getProject(), segment(1));
	}
	
	public Object toExecutable() {
		java.lang.Class clas = loadClass();
		if (clas==null) {return null;}
		try {
			return clas.newInstance();
		} catch (Exception e) {e.printStackTrace(utils.logger); return null;}
	}
	
	public IProject getProject() {
		if (isEmpty()) {return null;}
		return utils.wsr.getProject(segment(0));
	}
	public IProject getCreateProject() {
		if (isEmpty()) {return null;}
		try {
			return utils.getProject(segment(0));
		} catch (CoreException e) {return null;}
	}
	public PBEPath getParentPath() {
		if (isEmpty()) {return null;}
		return (PBEPath)removeLastSegments(1);
	}
	public Bundle getPlugin() {
		if (isEmpty()) {return null;}
		return Platform.getBundle(segment(0));
	}
	public PBEPath changeRoot(String newFirstSegmentName) {
		return (PBEPath)new Path(newFirstSegmentName).append(removeFirstSegments(1));
	}
	public PBEPath changeName(String newLastSegmentName) {
		return (PBEPath)removeLastSegments(1).append(newLastSegmentName);
	}
	public PBEPath changeExtension(String newFileExtension) {
		return (PBEPath)removeFileExtension().addFileExtension(newFileExtension);
	}
	public String getName() {
		return lastSegment();
	}
	public String getNameWithoutExtension() {
		return removeFileExtension().lastSegment();
	}
	public String toString() {
		return super.makeRelative().toString();
	}
	public String toAbsoluteString() {
		return super.makeAbsolute().toString();
	}
	
	
	
}