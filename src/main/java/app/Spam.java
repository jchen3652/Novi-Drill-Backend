package app;

import java.io.IOException;
import java.util.ArrayList;

import graphics.ImagePanel;
import interpreter.Dot;
import interpreter.SetCard;
import writer.PDFGenerator;

public class Spam {
	private static SetCard sets = null;
	static ArrayList<String> performers = new ArrayList<String>();
	private static String setCardText;

	public static void main(String[] args) {

		String address = "D:\\Users\\James\\Desktop\\Novi2019M2Coords.pdf";

		sets = new SetCard(address);
		setCardText = sets.toString();
		for (int i = 1; i <= 18; i++) {
			performers.add("AS" + i);
		}

		for (int i = 1; i <= 2; i++) {
			performers.add("BS" + i);
		}

		for (int i = 1; i <= 9; i++) {
			performers.add("B" + i);
		}

		for (int i = 1; i <= 4; i++) {
			performers.add("LR" + i);
		}

		for (int i = 1; i <= 5; i++) {
			performers.add("D" + i);
		}

		for (int i = 1; i <= 20; i++) {
			performers.add("C" + i);
		}

		for (int i = 1; i <= 19; i++) {
			performers.add("F" + i);
		}

		for (int i = 1; i <= 20; i++) {
			performers.add("Label: " + i);
		}

		for (int i = 1; i <= 8; i++) {
			performers.add("H" + i);
		}

		for (int i = 1; i <= 2; i++) {
			performers.add("Q" + i);
		}

		for (int i = 1; i <= 3; i++) {
			performers.add("N" + i);
		}

		for (int i = 1; i <= 3; i++) {
			performers.add("TS" + i);
		}

		for (int i = 1; i <= 13; i++) {
			performers.add("R" + i);
		}
		// TODO: Trumpet 1 doesn't exist for part 2 onwards

		for (int i = 2; i <= 26; i++) {
			performers.add("T" + i);
		}
		for (int i = 1; i <= 6; i++) {
			performers.add("U" + i);
		}

		for (String o : performers) {

			System.out.println("Generation for " + o + " is beginning");
			System.out.println("Getting performer information");
			ArrayList<Dot> dots = sets.getPerformer(o);
			//		for (int i = 0; i < dots.size(); i++) {
			//			System.out.println(dots.get(i).getXYCoords()[0] + "," + dots.get(i).getXYCoords()[1]);
			//			System.out.print(i != 0 && dots.get(i).getXYCoords().equals(dots.get(i - 1).getXYCoords()));
			//		}

			System.out.println("Generating images");
			ArrayList<Dot> dispDots = new ArrayList<>();

			for (int i = 0; i < dots.size(); i++) {
				dispDots.clear();
				if (i != 0) {
					dispDots.add(dots.get(i - 1));
				}
				dispDots.add(dots.get(i));
				ImagePanel.createAndShowGui(dispDots);
				//			ImageHandler.saveImage("Set" + dots.get(i).getSetNumber(), ImagePanel.getInstance());
			}

			System.out.println("Beginning PDF generation");
			try {
				PDFGenerator.generatePDF(dots, o + "DrillSheet", false);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Generation for " + o + " ended");

		}
	}
}
