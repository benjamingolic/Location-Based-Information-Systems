package at.fhooe.sail.ois.map.client.test;

import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {
	
	
	public static void main(String[] _argv) {
		System.out.println("Hello World");
		
		launch(_argv);
	}
	
	@Override
	public void start(Stage _stage) throws Exception {
		_stage.setTitle("GIS-Client -- Warmup -- ");
		_stage.setScene(null);
		_stage.show();
	}
}