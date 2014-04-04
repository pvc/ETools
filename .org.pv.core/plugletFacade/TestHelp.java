/**
 * 
 */
import java.util.Date;

import org.eclipse.ui.PlatformUI;
import org.pv.core.Utils;


/**
 * @author PV
 *
 */

public class TestHelp {
	final Utils utils = Utils.getSingleton();

	// Method called to run the class
	public void run() {
		p("Starting run of TestHelp at " + new Date());
		PlatformUI.getWorkbench().getHelpSystem().displayHelp("org.pv.tools.Context1");
		p("Finished run of TestHelp at " + new Date());
	}

	// Utility method for quick printing to console
	void p(Object o) {
		utils.log(o);
	}

}
