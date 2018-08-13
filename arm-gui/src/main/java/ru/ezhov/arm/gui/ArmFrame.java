package ru.ezhov.arm.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class ArmFrame extends Application {
	private static final Logger LOG = Logger.getLogger(ArmFrame.class.getName());

	@Override
	public void start(Stage primaryStage) throws Exception {
		ApplicationPanel applicationPanel = new ApplicationPanel();
		Scene scene = new Scene(applicationPanel);
		primaryStage.setScene(scene);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(600);
		primaryStage.setTitle("МОЙ АРМ");
		primaryStage.show();
	}
}
