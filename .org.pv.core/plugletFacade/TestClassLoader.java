/* Examines the classloader hierarchy which loaded this class
 * 
 */
import org.pv.core.Utils;


public class TestClassLoader implements Runnable {
	Utils utils=Utils.getSingleton();

	public void run() {
		boolean done=false;
		Object current=this;
		do {
		ClassLoader cl = current.getClass().getClassLoader();
		if (cl==null) {break;}
		utils.log(current.getClass()+" was loaded by: "+cl);
		utils.log("My parent is: "+cl.getParent());
		current=cl;
		} while (!done);

	}
	
	
}
