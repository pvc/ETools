/**
 * 
 */
import java.util.Date;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.pv.core.Utils;


/**
 * @author PV
 *
 */
public class TestDialog extends TitleAreaDialog {
	final static Utils utils = Utils.getSingleton();
	/**
	 * @param parentShell
	 */
	public TestDialog() {super(utils.wb.getActiveWorkbenchWindow().getShell());}
	
	public TestDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}


	// Method called to run the class
	public void run() {
		p("Starting run of TestDialog at " + new Date());
		setShellStyle(SWT.SHELL_TRIM);
		setHelpAvailable(true);
		setDialogHelpAvailable(true);
		
		create(); // enable following lines to run without npe's

		setMessage("My message",IMessageProvider.INFORMATION);
		setTitle("TitleArea Title");
		getShell().setText("Shell Title");
				
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getShell(), "org.pv.tools.Context1");
		int rc = open();
		
		p("Finished with rc="+rc);
		p("Finished run of TestDialog at " + new Date());
	}
	
	protected void configureShell(Shell dialogShell) {
		super.configureShell(dialogShell);
		dialogShell.setMinimumSize(0, 450); // if smaller, help displays in infopop
	}	
		
	
//	@Override
//	protected Control createDialogArea(Composite parent)
//	{
//	 Composite dialogArea = (Composite)super.createDialogArea(parent);
//	 Composite container = new Composite(dialogArea, SWT.NONE);
//	 container.setLayoutData(
//	     new GridData(GridData.FILL, GridData.FILL, true, true));
//	  then add your content to the Composite container...
//	}
	
	

	// Utility method for quick printing to console
	void p(Object o) {
		utils.log(o);
	}

}
