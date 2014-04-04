package org.pv.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.NewExampleAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.pbe.prefs.IPBEFieldValidator;
import com.ibm.pbe.prefs.PBEDialog;

// Generate mediation from wsdl
public class WsdlDoc implements IPBEFieldValidator {
	private static final String SUBFLOWMSG = "Please use the mouse to select a valid error handler subflow";
	private static final String WSDLMSG = "Please use the mouse to select the WSDL interface";
	private IWorkbenchWindow window;
	Utils utils=Utils.getSingleton();
	private ISelection sel;
	private int minSegs=5;
	static String MSET="com.ibm.etools.msg.validation.msetnature";
	private String patternId="com.ibm.pbe.patterns.Adapter.jet";
	static final String pluginId="com.ibm.pbe.umlToolkit";
//	final PatternApplicator runner=new PatternApplicator();
	Document doc=null; Element root=null;
	private WsdlDoc parent;
	private String wsdlPrefix;
	private Element serviceElt;
	private Element portElt;
	private Element addressElt;
	private Element portTypeElt;
	private Element schemaElt;
	List<Element> importElts;
	private Set<IProject> libraries=null;
	private List<WsdlDoc> childWsdls=new ArrayList<WsdlDoc>();
	IProject module=null;
	IFile wsdlFile=null;
	private boolean loaded=false;
	private boolean portTypeEltProcessed=false;
	private boolean schemaEltProcessed=false;
	private boolean fileSelected=false;
	private boolean operationsProcessed=false;
	private boolean importEltsProcessed=false;
	
	String service=""; String serviceNS; String servicePrefix;
	String port;
	String address="";
	String binding="";	String bindingNS; String bindingPrefix;
	String portType=""; String portTypeNS; String portTypePrefix;
	List<Operation> operations=new ArrayList<Operation>();
	
	public WsdlDoc(IFile wsdlFile) {
		setWsdlFile(wsdlFile);
	}
	public WsdlDoc() {
		super();
	}
	private void setWsdlFile(IFile file) {
		wsdlFile=file;
		fileSelected=true;
		initialise();
	}
	
	public void run() {
		IAction a=new NewExampleAction();
		a.setId("com.ibm.pbe.wMBPatterns.NewAction");
		run(a); 
	}
	
	public void run(IAction action) {
		utils.log("\nRunning Action: "+this.getClass().getName()+" "+new Date());
		StopWatch timer = utils.getTimer();
		boolean validSel=false; 
		Document view;Document model;
		
		IFile inFile = utils.getActiveFile();
		if (!utils.isFileType(inFile,"wsdl")) {utils.log("Please select a wsdl file & retry");return;}
		setWsdlFile(inFile);
		Operation op=getOperations().get(0);
		utils.log(portType);
		utils.log(portTypeNS);
		utils.log(op.name);
//		root=utils.getDocRoot(inFile);
//		wsdlPrefix=root.getPrefix();
//		
//		utils.log(root.getNodeName());
//		utils.log(root.getLocalName());
//		utils.log(root.getTagName());
//		utils.log(getService());
	}

	public List<Operation> getOperations() {
		if (!operationsProcessed) {
			List<Element>opElts=utils.getChildElements(portTypeElt);
			for (Element op:opElts) {
				operations.add(new Operation(op));
			}
			operationsProcessed=true;
		}
		return operations;
	}
	public void processServiceElt() {
		serviceElt=utils.getFirstElementAt(root,"service");
		if (serviceElt!=null) {
		portElt=utils.getFirstElementAt(serviceElt,"port");
		addressElt=utils.getFirstElementAt(portElt,"address");
		}
	}
	private void load() {
		if (loaded) {return;}
		root=utils.getDocRoot(wsdlFile);
		if (root!=null) {
		module=wsdlFile.getProject();
		libraries=utils.getLibraries(module);
		root.setUserData("wsdlDoc",this,null);
		}
		loaded=true;
	}

	public void initialise() {
		load();
		processServiceElt();
		processPortTypeElt();
//		SCASummary summ = new SCASummary();
		if (serviceElt!=null) {
			service=serviceElt.getAttribute("name");
			serviceNS=root.getAttribute("targetNamespace");
			if (portElt!=null) {
				port=portElt.getAttribute("name");
				String[] parts=portElt.getAttribute("binding").split(":");
				if (parts.length==2) {
					binding=parts[1];
					bindingNS=portElt.lookupNamespaceURI(parts[0]);
				}
				if (addressElt!=null) {
					address=addressElt.getAttribute("location");
				}
			}
		}
		if (portTypeElt!=null) {
			portType=portTypeElt.getAttribute("name");
			portTypeNS=portTypeElt.getOwnerDocument().getDocumentElement().getAttribute("targetNamespace");
		}
	}
	
	private Element processPortTypeElt() {
		if (portTypeEltProcessed) {return portTypeElt;}
		portTypeEltProcessed=true;
		portTypeElt=utils.getFirstElementAt(root,"portType");
		if (portTypeElt!=null) {return portTypeElt;}
		for (Element imp:getImportElts()) {
			utils.log("Hunting prtType doc");
			WsdlDoc w = getImportedWsdlDoc(imp);
			w.load();
			portTypeElt = w.processPortTypeElt();
			if (portTypeElt!=null) {break;}
		}
		return portTypeElt;
	}
	public Element getSchemaElt() {
		// Problem of ensuring all libraries handled (transitive closure) is currently ignored
		if (schemaEltProcessed) {return schemaElt;}
		schemaEltProcessed=true;
		schemaElt=utils.getFirstElementAt(root,"types");
		if (schemaElt!=null) {return schemaElt;}
		
		for (Element imp:getImportElts()) {
			String path=imp.getAttribute("location");
			if (path.endsWith(".xsd")) {
				schemaElt=imp;
				IFile file=findFile(path);
				if (file==null) {utils.log("***Error - could not locate required file: "+path+" specified in wsdl: "+getWsdlDoc(imp).wsdlFile.getFullPath());}
				else {schemaElt.setUserData("rootLoc", file.getProjectRelativePath().toString(),null);
				schemaElt.setUserData("project", file.getProject().getName(),null);
				}
			} else
			if (path.endsWith(".wsdl")) {	
			WsdlDoc w = getImportedWsdlDoc(imp);
			w.load();
			schemaElt = w.getSchemaElt();
			}
			if (schemaElt!=null) {break;}
		}
		return schemaElt;
	}
	
	public WsdlDoc getWsdlDoc(Element elt) {
		return (WsdlDoc)elt.getOwnerDocument().getDocumentElement().getUserData("wsdlDoc");
	}
	
	
	
	
	public List<Element> getImportElts() {
		if (importEltsProcessed) {return importElts;}
		importElts = utils.getChildElements(root,"wsdl:import");
		return importElts;
	}

	public WsdlDoc getImportedWsdlDoc(Element importElt) {
		if (importElt==null) {return null;}
		WsdlDoc w=(WsdlDoc)importElt.getUserData("wsdlDoc");
		if (w!=null) {return w;}
		String path=importElt.getAttribute("location");
		IFile wsdl=null;
		if (path.charAt(0)=='/') {
			wsdl=utils.getFile(path);
			if (wsdl==null) {wsdl=findFile(path.substring(1));}
		} else wsdl = findFile(wsdlFile.getParent().getProjectRelativePath().append(path).toString());
		if (wsdl!=null) {
			w=new WsdlDoc(wsdl);
			w.parent=this;
			childWsdls.add(w);
			importElt.setUserData("wsdlDoc", w,null);
		}
		return w;
	}
	

	
	public Element findDocRoot(String relPath) {
		return utils.getDocRoot(findFile(relPath));
	}
	public Document findDoc(String relPath) {
		return utils.getDoc(findFile(relPath));
	}
	public IFile findFile(String relPath) {
		IFile file = utils.getFile(module,relPath);
		if (file!=null) {return file;}
		for (IProject lib:libraries) {
			file = utils.getFile(lib,relPath);
			if (file!=null) {return file;}
		}
		return file;
	}
	


	public WsdlDoc askUserForFile() {
		Element modelRoot=utils.getDocRoot(pluginId, "files/WsdlDoc.appdef");
		Element viewRoot=utils.getDocRoot(pluginId, "files/WsdlDoc.pbeDialog");
		PBEDialog dlg = new PBEDialog();
		int res=dlg.execute(viewRoot, modelRoot,this );
		if (res==Window.CANCEL) {fileSelected=false;return this;}
		String path=utils.getFirstChild(modelRoot).getAttribute("wsdl");
		IFile file = utils.getFile(path);
		setWsdlFile(file);
//		fileSelected=true;
		return this;
	}
	
	public String doField(FieldEditor fe) {
		// TODO Auto-generated method stub
		return null;
	}
	public String doSelect(FieldEditor fe, Object selection) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isFileSelected() {
		return fileSelected;
	}

	public class OperationPart {
		String name;
		String messageName;
		String namespace;
		String prefix;
		String elementName;
		String elementNamespace;
		String elementPrefix;
		public OperationPart(Element opPart) {
			if (opPart!=null) {
				name=opPart.getAttribute("name");
				String parts[]=opPart.getAttribute("message").split(":");
				if (parts.length==2) {
					messageName=parts[1];
					prefix=parts[0];
					namespace=opPart.lookupNamespaceURI(prefix);
				} else {messageName=parts[0];}
				
				Element msg=utils.getFirstElementAt(utils.getRootElement(opPart),"message[name=\""+messageName+"\"]");
				utils.log("Searched for msg "+messageName+" - found="+(msg!=null));
				Element msgPart0=utils.getFirstChild(msg);
				if (msgPart0!=null) {
				parts=msgPart0.getAttribute("element").split(":");
				if (parts.length==2) {
					elementName=parts[1];
					elementPrefix=parts[0];
					elementNamespace=msgPart0.lookupNamespaceURI(elementPrefix);
				} else {elementName=parts[0];}
				}
			}
		}
		public String getMessageName() {
			return messageName;
		}
		public String getMessageNS() {
			return namespace;
		}
		public String getMessagePrefix() {
			return prefix;
		}
		public String getPartName() {
			return name;
		}
		public String getElementName() {
			return elementName;
		}
		public String getElementNS() {
			return elementNamespace;
		}
		public String getElementPrefix() {
			return elementPrefix;
		}
	}
	
	
	public class Operation {
		boolean valid=false;
		Element opElt;
		String name;
		OperationPart input;
		OperationPart output;
		List<OperationPart> faults=new ArrayList<OperationPart>();
		public Operation(Element opElt) {
			this.opElt=opElt;
			if (opElt==null|| !"operation".equals(opElt.getLocalName())) {return;}
//			Element root=utils.getRoot(opElt);
			name=opElt.getAttribute("name");
			input=new OperationPart(utils.getFirstElementAt(opElt,"input"));
			output=new OperationPart(utils.getFirstElementAt(opElt,"output"));
			List<Element> faultElts = utils.getChildElements(opElt,"fault");
			for (Element f:faultElts) {
				OperationPart fault = new OperationPart(f);
				faults.add(fault);
			}
		}
		
		public boolean isValid() {
			return valid;
		}
		public Element getOpElt() {
			return opElt;
		}
		public String getName() {
			return name;
		}
		public OperationPart getInput() {
			return input;
		}
		public String getInputMessageName() {
			return input.messageName;
		}
		public String getInputMessageNS() {
			return input.namespace;
		}
		public String getInputMessagePrefix() {
			return input.prefix;
		}
		public String getInputPartName() {
			return input.name;
		}
		public String getInputElementName() {
			return input.elementName;
		}
		public String getInputElementNS() {
			return input.elementNamespace;
		}
		public String getInputElementPrefix() {
			return input.elementPrefix;
		}
		
		public OperationPart getOutput() {
			return output;
		}
		public String getOutputMessageName() {
			return output.messageName;
		}
		public String getOutputMessageNS() {
			return output.namespace;
		}
		public String getOutputMessagePrefix() {
			return output.prefix;
		}
		public String getOutputPartName() {
			return output.name;
		}
		public String getOutputElementName() {
			return output.elementName;
		}
		public String getOutputElementNS() {
			return output.elementNamespace;
		}
		public String getOutputElementPrefix() {
			return output.elementPrefix;
		}

		public List<OperationPart> getFaults() {
			return faults;
		}

	}




	public String getWsdlPrefix() {
		return wsdlPrefix;
	}
	public void setWsdlPrefix(String wsdlPrefix) {
		this.wsdlPrefix = wsdlPrefix;
	}
	public boolean isPortTypeEltProcessed() {
		return portTypeEltProcessed;
	}
	public void setPortTypeEltProcessed(boolean portTypeEltProcessed) {
		this.portTypeEltProcessed = portTypeEltProcessed;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getServiceNS() {
		return serviceNS;
	}
	public void setServiceNS(String serviceNS) {
		this.serviceNS = serviceNS;
	}
	public String getServicePrefix() {
		return servicePrefix;
	}
	public void setServicePrefix(String servicePrefix) {
		this.servicePrefix = servicePrefix;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBinding() {
		return binding;
	}
	public void setBinding(String binding) {
		this.binding = binding;
	}
	public String getBindingNS() {
		return bindingNS;
	}
	public void setBindingNS(String bindingNS) {
		this.bindingNS = bindingNS;
	}
	public String getBindingPrefix() {
		return bindingPrefix;
	}
	public void setBindingPrefix(String bindingPrefix) {
		this.bindingPrefix = bindingPrefix;
	}
	public String getPortType() {
		return portType;
	}
	public String getPortTypeDocLocation() {
		return utils.getFile(portTypeElt).getFullPath().toString();
	}
	public void setPortType(String portType) {
		this.portType = portType;
	}
	public String getPortTypeNS() {
		return portTypeNS;
	}
	public void setPortTypeNS(String portTypeNS) {
		this.portTypeNS = portTypeNS;
	}
	public String getPortTypePrefix() {
		return portTypePrefix;
	}
	public void setPortTypePrefix(String portTypePrefix) {
		this.portTypePrefix = portTypePrefix;
	}
	public boolean hasPort() {
		return (port!=null);
	}
	public boolean hasPortType() {
		return (portType!=null);
	}
	public Set<IProject> getLibaries() {
		return libraries;
	}
	
}
