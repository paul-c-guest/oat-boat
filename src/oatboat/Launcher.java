package oatboat;

import javafx.application.Application;

// import com.sun.javafx.PlatformUtil;

/**
 * this class acts as a workaround to successfully launch a standalone javafx
 * application from within a 'fat jar' compiled with the maven plugin for
 * eclipse.
 * <p>
 * the fat jar is packaged properly using 'shade' plugin for maven.
 * <p>
 * this workaround is suggested because the primary class implementing any
 * javafx libraries needs to extend {@link Application} - which is problematic
 * for the maven packaging process.
 * 
 * @author ballsies
 *
 */
public class Launcher {

	public static void main(String[] args) {
		/**
		 * some Win environments do not display program properly without an extra
		 * command line switch to use some specific Prism screen rendering mode
		 * (?incomplete information - better description needed) - thus the command line
		 * is always required and unneccessarily visible during runtime.
		 * <p>
		 * one suggestion is to set the mode at launch, although this statement does not
		 * appear to be making any change:
		 */
//		if (PlatformUtil.isWindows())
//			System.setProperty("com.sun.prism.order", "sw");

		Main.main(args);
	}

}
