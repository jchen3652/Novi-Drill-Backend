package interpreter;

import java.io.File;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Graphics generation process
 * 
 * @author johuang50
 * @author jchen3652
 *
 */
public class SetCard {

	private static String parsedText;
	private static String[] lines;

	PDFParser parser;
	PDDocument pdDoc;
	COSDocument cosDoc;
	PDFTextStripper pdfStripper;

	File file;

	public SetCard(File file) {
		this.file = file;
	}

	public SetCard(String fileName) {
		this(new File(fileName));
	}
	
	public String toString() {
		try {
			parser = new PDFParser(new RandomAccessBufferedFileInputStream(file));
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			parsedText = pdfStripper.getText(pdDoc);
			lines = parsedText.split("\\r?\\n");

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (cosDoc != null) {
					cosDoc.close();
				}
				if (pdDoc != null) {
					pdDoc.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return parsedText;
	}

	public ArrayList<Dot> getPerformer(String performerID) {
		int endIndex = 0, startIndex = 0;
		ArrayList<Dot> toReturn = new ArrayList<Dot>();

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains(performerID + " ")) {
				endIndex = i;
				startIndex = endIndex;
			}
		}

		while (!lines[startIndex - 2].contains("Page") && startIndex > 2) {
			startIndex--;
		}

		startIndex = (startIndex == 2) ? 1 : startIndex;

		for (int i = startIndex; i < endIndex; i++) {
			Dot dot = processLine(lines[i]);
			toReturn.add(dot);
		}

		return toReturn;

	}

	public static Dot processLine(String line) {

		int offset = 0;

		double LRMod = 0, FBMod = 0;

		line = line.replaceAll("\\s+", " ");

		String[] split = line.split(" ");

		hash shithash = null;

		if (line.contains("Side 1: On 50 yd ln")) {
			offset += 2;
		} else if (line.contains("Side 2: On 50 yd ln")) {
			offset += 2;
		}

		else if (line.contains("On 50 yd")) {
			offset += 4;

		} else {
			// Determine L/R Modifier
			if (split[4].equals("On")) {
				offset += 2;
				LRMod = 0;
			} else {
				LRMod = Double.parseDouble(split[4]);
			}
		}

		// Determine F/B Modifier
		if (split[10 - offset].equals("On")) {
			FBMod = 0;

		} else {
			FBMod = Double.parseDouble(split[10 - offset]);
		}

		switch (split[split.length - 3] + " " + split[split.length - 2] + " " + split[split.length - 1]) {
		case "Back Hash (HS)":
			shithash = hash.BACK_HASH;
			break;
		case "Front Hash (HS)":
			shithash = hash.FRONT_HASH;
			break;

		case "Front side line":
			shithash = hash.FRONT_SIDELINE;
			break;

		case "Back side line":
			shithash = hash.BACK_SIDELINE;

		}

		return new Dot(split[0], Integer.parseInt(split[1]), split[3].equals("1:") ? side.ONE : side.TWO, LRMod,
				split[6].equals("inside") ? leftToRightModifier.INSIDE : leftToRightModifier.OUTSIDE,
				Integer.decode(split[7 - offset]), FBMod,
				line.contains("in front of") ? frontToBackModifier.FRONT : frontToBackModifier.BEHIND, shithash);
	}

	public static void main(String[] args) {

		SetCard sets;
		String address = "D:\\Users\\James\\Desktop\\Novi2019CompleteM1Coords.pdf";

		sets = new SetCard(address);
		sets.toString();
		//		Dot dot = processLine("8 12 On 50 yd ln 9.25 steps behind Front side line");
		//		System.out.println(dot.toString());
		ArrayList<Dot> dots = sets.getPerformer("AS10");
		for (Dot o : dots) {
			System.out.println(o);
		}

		//
	}
}
