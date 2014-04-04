/*****************************************************************************
Testing: Class now detects if it is loading itself and doesn't try recursive execution
so testing is safe!
Simplest way to test is to add an f= line in the run() method to force loading of a particular file
NB Last bug found was that the p method of the tested class was doing a System.out instead of utils.log!
*/
package org.pv.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.NewProjectAction;
import org.osgi.framework.Bundle;
import org.pv.core.StopWatch;
import org.pv.core.Utils;
import org.pv.plugin.Activator;
import org.pv.plugin.Copyright;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author Administrator
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PVLoader {
	// boolean testing=false; // set to true to load but not execute: not in use
	static final Utils utils = Utils.getSingleton();
	// static final Activator plugin=Activator.getDefault();
	static final Preferences globals = utils.getGlobals();
	String className;
	// String className="com.ibm.p4eb.SVG.SVGTransform";
//	static String className = "com.ibm.pbe.core.MenuAdder";
//	 String className="com.ibm.p4eb.wsdl.GenWPSProcessTestAppdef";
	// String className="com.ibm.issw.actionImpl.RunDPTK";
	
	static final String PLUGINNATURE = "org.eclipse.pde.PluginNature";
	Properties mfMap = null;
	List<Bundle> bundleMap = null;
	IProject project;
	IJavaProject jproject;
	boolean initManifest = false;
	IFile f;
	String pathName;
	ICompilationUnit ju;
	StopWatch timer;
	private MyLoader ldr;

	public void plugletmain(String[] args) {
		run();
	}

	public void run() {
//		 utils.log("Starting loader");
		// getLocalPlugins();
		// if (true) {return;}
		IResource f = utils.getSelectedResource();
		// f=utils.getFile(".org.pv.tools/src/com/ibm/pv/widgets/TestShell.java");
//		 f=utils.getFile("/org.pv.Downloader/src/download/OldMain.java");
		// //testing
		if (f == null || f.getType() != IResource.FILE) {
			f = utils.getEditedFile();
		}
		if (f != null) {
			execute((IFile) f);
		}
//		 utils.log("Loader run complete");
	}

	public void execute(IFile f) {
		IJavaElement j = JavaCore.create(f);
		if ((j == null) || j.getElementType() != IJavaElement.COMPILATION_UNIT) {
			utils.log("Please select a Java Source File");
			return;
		}
		ju = (ICompilationUnit) j;
		this.f = f;
		this.pathName = f.getFullPath().toString();
		utils.saveEditors();
		execute();
	}

	public Class<?> load(IProject p, String qualifiedClass) {
		processManifest(p);

		// IPath projectPath=project.getLocation();
		// IPackageFragmentRoot pfr =
		// (IPackageFragmentRoot)ju.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
		try {
			URL[] urls = getLocalCPURLs(p);
			ldr = new MyLoader(urls);
			Class<?> targetClass = ldr.loadClass(qualifiedClass);
//			utils.log("Loaded: "+targetClass);
			// Method method=chooseMethod(targetClass);
			// utils.log("Successfully loaded: "+targetClass.getName());
			return targetClass;
		} catch (ClassNotFoundException e) {
			utils.log("***Could not load class: " + e.getMessage());
			return null;
		}
	}

	
	public void execute() {
		// utils.log(j.getElementName());
		PlatformUI.getWorkbench().saveAllEditors(true);
		try {
			utils.ws.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
					new NullProgressMonitor());
		} catch (CoreException e1) {
			e1.printStackTrace(utils.getLogger());
		}
		project = f.getProject();
		jproject = ju.getJavaProject();

		// IPath projectPath=project.getLocation();
		// IPackageFragmentRoot pfr =
		// (IPackageFragmentRoot)ju.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
		try {
			timer = utils.getTimer();
			utils.log("\n*****************************Starting Dynamic Execution of: "
					+ pathName + " at " + new Date());
			className = ju.getTypes()[0].getFullyQualifiedName();
			Class<?> targetClass = load(project, className);
			if (targetClass == null) {utils.log(className+" could not be loaded");} else {
//			utils.log("Attempting to run " + targetClass + " ...");
				Method run = null;
				try {
					run = targetClass.getMethod("run", null);
				} catch (NoSuchMethodException e) {
				} catch (Exception e) {
					e.printStackTrace(utils.getLogger());
				}
//				utils.log("Run method retrieved="+run);
				if (run == null) {
					utils.log("Error - this class does not currently have a run() method to call - please add one and try again");
				}
				// if (testing) {run=null;} // avoid execution for testing
				// purposes
				if (run != null) {
					if (targetClass.getName().equals(this.getClass().getName())) {
						utils.log("Test mode detected - recursive execution avoided");
					} else {
					if (Modifier.isStatic(run.getModifiers())) {
						run.invoke(targetClass, (Object[])null);
					} else {
						run.invoke(targetClass.newInstance(), (Object[])null);
					}
					}
				}
			}

			// Object target = targetClass.newInstance();
			// if (target==null) {utils.log("Could not load "+className+
			// " - Check classpath for references in other workspace projects");return;}
			// try {run=targetClass.getMethod("run",null);} catch
			// (NoSuchMethodException e) {} catch (Exception e)
			// {e.printStackTrace(utils.getLogger());}
			// if (run!=null) {try {run.invoke(target,null);} catch (Exception
			// e) {e.printStackTrace(utils.getLogger());} }
			// if (run==null) {
			// try {run=targetClass.getMethod("run",new Class[]
			// {IAction.class});} catch (NoSuchMethodException e) {} catch
			// (Exception e) {e.printStackTrace(utils.getLogger());}
			// if (run!=null) {try {run.invoke(target,new Object[] {new
			// NewProjectAction()} ); } catch (Exception e)
			// {e.printStackTrace(utils.getLogger());} }
			// }
			// if (run==null) {
			// try {run=targetClass.getMethod("execute",null);} catch
			// (NoSuchMethodException e) {} catch (Exception e)
			// {e.printStackTrace(utils.getLogger());}
			// if (run!=null) {try {run.invoke(target,null); } catch (Exception
			// e) {e.printStackTrace(utils.getLogger());} }
			// }

		} catch (Throwable e) {
			e.printStackTrace(utils.getLogger());
		}

		utils.log("*****************************End of Dynamic Execution of: " + pathName
				+ "Total Elapsed=" + timer);

		if (Activator.REPEAT.equals(className) || Activator.RUNNER.equals(className)) {
		} else {
			globals.setValue("lastAction", pathName);
		}
	}

	/**
	 * @param targetClass
	 * @return
	 */
	private Method chooseMethod(Class targetClass) {
		Method[] methods = targetClass.getMethods();
		ArrayList acceptable = new ArrayList();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (accept(method)) {
				acceptable.add(method);
			}
		}
		if (acceptable.size() == 0) {
			return null;
		}
		if (acceptable.size() == 1) {
			return (Method) acceptable.get(0);
		}
		return chooseMethod(acceptable);
	}

	/**
	 * @param acceptable
	 * @return
	 */
	private Method chooseMethod(List c) {
		return (Method) c.get(0);
	}

	/**
	 * @param method
	 * @return
	 */
	private boolean accept(Method method) {
		if (method.getName().equals("run")) {
			return true;
		}
		return false;
	}

	public void processManifest(IProject project) {
		if (initManifest) {
			return;
		}
		initManifest = true;
		// utils.log("Processing manifest");
		try {
			IFile file = project.getFile("/META-INF/MANIFEST.MF");
			if (file.exists()) {
				mfMap = new Properties();
				InputStream is = file.getContents();
				LineNumberReader lr = new LineNumberReader(
						new InputStreamReader(is));
				String line = null;
				String curline = "";
				boolean started = false;
				while (null != (line = lr.readLine())) {
					if (line.length() == 0) {
						continue;
					}
					if (line.charAt(0) == ' ') {
						curline += line;
					} else {
						if (!started) {
							started = true;
						} else {
							add2map(curline);
							curline = line;
						}
					}
				}
				add2map(curline);
			}
		} catch (Exception t) {
			t.printStackTrace(utils.getLogger());
		}
	}

	/**
	 * @param curline
	 */
	private void add2map(String curline) {
		// utils.log("Adding mf line: "+curline);
		if (curline.length() > 0) {
			String[] parts = curline.split(":", 2); // qualifiers now specified
													// with := (idiotic!)
			// utils.log("Key="+parts[0].trim()+" - parts="+parts.length);
			if (parts.length >= 2) {
				mfMap.setProperty(parts[0].trim(), parts[1]);
			}
		}
	}

	private class MyLoader extends URLClassLoader {
		// This loader takes jvm loader as parent - fine for workspace search,
		// but not for plugins
		// So plugin search is replicated - MUST first search through plugin
		// loader of this class to avoid linkage errors
		// (ie if this plugin has already loaded Foo, we must obtain this
		// instance of Foo, not load thru another bundle)
		/*
		 * We override findclass which is called by loadclass (which is called
		 * by JVM). Loadclass definition: Loads the class with the specified
		 * binary name. The default implementation of this method searches for
		 * classes in the following order:
		 * 
		 * Invoke findLoadedClass(String) to check if the class has already been
		 * loaded.
		 * 
		 * Invoke the loadClass method on the parent class loader. If the parent
		 * is null the class loader built-in to the virtual machine is used,
		 * instead.
		 * 
		 * Invoke the findClass(String) method to find the class.
		 * 
		 * If the class was found using the above steps, and the resolve flag is
		 * true, this method will then invoke the resolveClass(Class) method on
		 * the resulting Class object.
		 */

		public MyLoader(URL[] urls) {
			super(urls);
//			 utils.log("MyLoader initialised - parent is: "+getParent().getClass()+", local Classpath is:");
//			 for (int i = 0; i < urls.length; i++) {
//			 utils.log(urls[i]);
//			 }

		}

		// public Class loadClass(String cName) {
		// utils.log("LoadClass called - delegating up");
		// Class<?> clazz=null;
		// try {
		// clazz = super.loadClass(cName);
		// } catch (ClassNotFoundException e) {}
		// utils.log("Super returned: "+clazz);
		//
		// return clazz;
		// }
		public Class findClass(String cName) throws ClassNotFoundException {
			// Called by loadclass. At this point, it has checked the class not
			// already loaded, and the parent (jvm) can't load it.

			Class c = null;
			
//			 utils.log("MyLoader loading: "+cName);
			try {
//				 utils.log("Trying load from workspace projects");
				c = super.findClass(cName); // URLClassLoader searches each
											// workspace url
//				 utils.log("Successfully loaded from workspace");
			} catch (ClassNotFoundException e) {
//				cne = e;
			}
			if (c != null) {
				return c;
			}
//			 utils.log("Searching using my plugin classloader ...");
			// first see if this plugin can load it to avoid linkage errors
			// Note: if this plugin is not imported by the plugin of the
			// original class we are loading
			// we may be giving that plugin access to more classes than planned.
			// That's a downside
			// c=this.getClass().getClassLoader().loadClass(cName);
			// if (c!=null) {utils.log("Success");}
			if (c != null) {
				return c;
			}
			// ok, now search any other plugins imported

			// utils.log("Searching required plugins ...");
			Bundle b = null;
			// Warning - currently getbundlemap only fetches required plufind,
			// NOT imported packages - needs fixing
			for (Iterator iter = getBundleMap().iterator(); iter.hasNext();) {
				b = (Bundle) iter.next();
				try {
//					 utils.log("Trying bundle: "+b.getSymbolicName());
					c = b.loadClass(cName);
				} catch (ClassNotFoundException e1) {
//					cne = e1;
				}
				if (c != null) {
//					 utils.log(cName+" successfully loaded");
					break;
				}
			}
//			 utils.log("Search complete - result="+c);
			if (c != null) {
				return c;
			} else {
//				utils.log("Throwing not found for: "+cName);
				throw new ClassNotFoundException(cName);
			}
		}
		/**
		 * @return
		 */

	}

	private List getBundleMap() {
		if (bundleMap == null) {
			bundleMap = new LinkedList();
			// utils.log("Building bundle map");
			if (getMFMap() != null) {
				String s = getMFMap().getProperty("Require-Bundle");
				// utils.log("Require-Bundle entry from manifest = "+s);
				if (s != null) {
					String[] plugins = s.split(",");
					for (int i = 0; i < plugins.length; i++) {
						String plugin = plugins[i].split(";")[0].trim();
						Bundle b = Platform.getBundle(plugin);
						// utils.log("Adding to map "+plugin+":"+b);
						if (b != null) {
							bundleMap.add(b);
						}
					}
				}
			} else {
				Document doc = utils.getDoc(project.getFile("plugin.xml"));
				utils.p("Plugin.xml loaded: " + (doc != null));
				if (doc != null) {
					NodeList nl = doc.getElementsByTagName("requires");
					if (nl.getLength() > 0) {
						Element requires = (Element) nl.item(0);
						NodeList imports = requires
								.getElementsByTagName("import");
						for (int i = 0; i < imports.getLength(); i++) {
							Element imp = (Element) imports.item(i);
							String plugin = imp.getAttribute("plugin");
							Bundle b = Platform.getBundle(plugin);
							if (b != null) {
								bundleMap.add(b);
							}
						}
					}
				}
			}
		}
		return bundleMap;
	}

	private Properties getMFMap() {
		if (mfMap == null) {
			processManifest(project);
		}
		return mfMap;
	}

	/* return a list of urls */
	public List<URL> getInternalCP(IProject p) {
		List<URL> urls = new LinkedList<URL>();
		try {
			IJavaProject jp = JavaCore.create(p);
			IPath defaultOl = jp.getOutputLocation();
			URL url = getCPURL(defaultOl);
			if (url != null) {
				urls.add(url);
			}
			IClasspathEntry[] cpes = jp.getRawClasspath();
			for (int j = 0; j < cpes.length; j++) {
				IClasspathEntry cpe = cpes[j];
//				 utils.log(cpe);
				IPath ol = cpe.getPath();
//				utils.log("Path="+ol);
				if ((ol != null) && (ol != defaultOl)) {
//					 utils.log(ol);
					url = getCPURL(ol);
//					utils.log("URL="+url);
					if (url != null) {
						urls.add(url);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(utils.getLogger());
		}
		return urls;
	}

	public URL getCPURL(IPath path) {
		URL url = null;
		try {
			url = utils.wsr.findMember(path).getLocation()
					.addTrailingSeparator().toFile().toURL();
		} catch (Exception e) {
			// e.printStackTrace(utils.getLogger());
//			utils.log("Loader found bad classpath entry: " + path);
		}
		return url;
	}

	public Set getReferencedProjects(IProject p) {
		final String PLUGINS = "org.eclipse.pde.core.requiredPlugins";
		Set refs = new HashSet();
		try {
			IProject[] list1 = p.getDescription().getReferencedProjects();
			for (int i = 0; i < list1.length; i++) {
				refs.add(list1[i]);
			}
			if (p.hasNature(PLUGINNATURE)) {
				IJavaProject pj = JavaCore.create(p);
				IClasspathEntry[] cpes = pj.getRawClasspath();
				for (int j = 0; j < cpes.length; j++) {
					IClasspathEntry cpe = cpes[j];
					if ((cpe.getEntryKind() == IClasspathEntry.CPE_CONTAINER)
							&& (cpe.getPath().toString().equals(PLUGINS))) {
						IClasspathEntry[] rcpes = JavaCore
								.getClasspathContainer(cpe.getPath(), pj)
								.getClasspathEntries();
						for (int k = 0; k < rcpes.length; k++) {
							IClasspathEntry cpe2 = rcpes[k];
							if (cpe2.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
								refs.add(utils.wsr.getProject(cpe2.getPath()
										.lastSegment()));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(utils.getLogger());
		}
		return refs;
	}

	public URL[] getLocalCPURLs(IProject p) {
		final URL[] URLType = new URL[] {};
		List<URL> urls = new LinkedList<URL>();
		urls.addAll(getInternalCP(p));
		Set refprojs = getReferencedProjects(p);
		for (Iterator iter = refprojs.iterator(); iter.hasNext();) {
			IProject proj = (IProject) iter.next();
			urls.addAll(getInternalCP(proj));
		}
		return (URL[]) urls.toArray(URLType);
	}

	static String copyright() {
		return Copyright.PV_COPYRIGHT;
	}
}
