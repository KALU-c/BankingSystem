import java.awt.EventQueue;
import GUI.GUIForm;
import Data.DatabaseIO;

public class Application {
	public static void main(String[] args) {
		// Initialize database connection
		DatabaseIO.initialize();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIForm.login.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
