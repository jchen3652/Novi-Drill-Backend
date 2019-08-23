//package app;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import graphics.ImagePanel;
//import interpreter.Dot;
//import interpreter.SetCard;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.stage.FileChooser;
//import writer.PDFGenerator;
//
//public class MainController {
//
//	SetCard sets = null;
//	private String setCardText;
//
//	@FXML
//	private TextField fileNameField;
//
//	@FXML
//	private TextField labelField;
//
//	@FXML
//	private Button uploadButton;
//
//	@FXML
//	private Button generateButton;
//
//	@FXML
//	private void handleFileButton() {
//		FileChooser chooser = new FileChooser();
//		chooser.setTitle("Open File");
//		File file = chooser.showOpenDialog(Main.getRoot().getScene().getWindow());
//
//		try {
//			fileNameField.setText(file.getName());
//			sets = new SetCard(file);
//		} catch (RuntimeException e) {
//
//		}
//
//		uploadButton.setDisable(sets == null);
//
//	}
//
//	@FXML
//	private void handleUploadButton() {
//		setCardText = sets.toString();
//		//		System.out.println(setCardText);
//		enableSecondStage(isValidSetCard(setCardText));
//	}
//
//	@FXML
//	private void handleGenerateButton() throws IOException {
//		System.out.println("Generation for " + labelField.getText() + "...");
//		ArrayList<Dot> dots = sets.getPerformer(labelField.getText());
//		//		for (int i = 0; i < dots.size(); i++) {
//		//			System.out.println(dots.get(i).getXYCoords()[0] + "," + dots.get(i).getXYCoords()[1]);
//		//			System.out.print(i != 0 && dots.get(i).getXYCoords().equals(dots.get(i - 1).getXYCoords()));
//		//		}
//		ArrayList<Dot> dispDots = new ArrayList<>();
//
//		//		ImagePanel.createAndShowGui(dots);
//		for (int i = 0; i < dots.size(); i++) {
//			dispDots.clear();
//			if (i != 0) {
//				dispDots.add(dots.get(i - 1));
//			}
//			dispDots.add(dots.get(i));
//			ImagePanel.createAndShowGui(dispDots);
//			//			ImageHandler.saveImage("Set" + dots.get(i).getSetNumber(), ImagePanel.getInstance());
//		}
//
//		PDFGenerator.generatePDF(dots, labelField.getText() + "DrillSheet", true);
//
//	}
//
//	@FXML
//	private void initialize() {
//	}
//
//	public MainController() {
//
//	}
//
//	private boolean isValidSetCard(String text) {
//		return text.contains("Performer:");
//	}
//
//	private void enableSecondStage(boolean enable) {
//		labelField.setEditable(enable);
//		labelField.setOpacity(enable ? 1.0 : 0.55);
//		generateButton.setDisable(!enable);
//	}
//}
