/**
 * 
 */
import java.util.Date;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.pv.core.Utils;


/**
 * @author PV
 *
 */
public class TestMenu {
	final Utils utils = Utils.getSingleton();

	// Method called to run the class
	public void run() {
		p("Starting run of TestMenu at " + new Date());
		Menu mb = utils.wb.getActiveWorkbenchWindow().getShell().getMenuBar();
		p(mb);
		for (MenuItem m: mb.getItems()) {
			p(m.getText());
			if (m.getText().equals("PBECore Tools")) {
				final MenuItem m2 = m;
				p(m);
				utils.dump(m.getData().getClass());
				m.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						super.widgetSelected(e);
						p("Selected");
						p(m2);
						p(m2.getData());
					}
				}) ;
				
			}
		}
		
		
		
		p("Finished run of TestMenu at " + new Date());
	}

	// Utility method for quick printing to console
	void p(Object o) {
		utils.log(o);
	}

}
