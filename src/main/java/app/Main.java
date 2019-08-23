//package app;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//	static Pane root = null;
//
//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			root = (Pane) FXMLLoader.load(getClass().getResource("MainDisplay.fxml"));
//			Scene scene = new Scene(root);
//			primaryStage.setTitle("Novi Drill Client");
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static Pane getRoot() {
//		return root;
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//}