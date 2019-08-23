package writer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import interpreter.Dot;

public class PDFGenerator {
	static File outputFile = null;

	public static File generatePDF(ArrayList<Dot> dots, String file, boolean open) throws IOException {

		String filename = file + ".pdf";
		String title, dotInfo;

		int setLineX = 10, setLineY = 780, imageX = 0, imageY = 591, dotX = 10, dotY = 765, offsetX = 305,
				offsetY = 200;

		PDDocument doc = new PDDocument();
		try {
			PDPage page = new PDPage();
			doc.addPage(page);

			PDFont font = PDType1Font.HELVETICA;

			PDPageContentStream contents = new PDPageContentStream(doc, page);

			for (int i = 0; i < dots.size(); i++) {

				if (i % 8 == 0 && i != 0) {

					PDPage newPage = new PDPage();
					doc.addPage(newPage);

					contents.close();
					contents = new PDPageContentStream(doc, newPage);

					setLineX = 10;
					imageX = 0;
					dotX = 10;

					setLineY = 780;
					imageY = 591;
					dotY = 765;
				} else if (i % 4 == 0 && i != 0) {
					setLineX += offsetX;
					imageX += offsetX;
					dotX += offsetX;

					setLineY = 780;
					imageY = 591;
					dotY = 765;
				}

				title = "Set " + dots.get(i).getSetNumber();

				contents.beginText();
				contents.setFont(font, 12);
				contents.newLineAtOffset(setLineX, setLineY);
				contents.showText(title);
				contents.endText();

				dotInfo = dots.get(i).shortHand();

				contents.beginText();
				contents.setFont(font, 8);
				contents.newLineAtOffset(dotX, dotY);
				contents.showText(dotInfo);
				contents.endText();

				// Creating PDImageXObject object
				PDImageXObject pdImage = PDImageXObject.createFromFileByContent(
						Paths.get("/deploy/Set" + dots.get(i).getSetNumber() + ".png").toFile(), doc);

				// Drawing the image in the PDF document
				contents.drawImage(pdImage, imageX, imageY);

				setLineY -= offsetY;
				imageY -= offsetY;
				dotY -= offsetY;
			}

			contents.close();
			if (filename.contains("Label: ")) {
				filename = filename.replace("Label: ", "G");
			}

			doc.save(filename);

		} finally {
			doc.close();

		}

		return Paths.get(filename).toFile();
	}

	private static void printPaths(File file) throws IOException {
		System.out.println("Absolute Path: " + file.getAbsolutePath());
		System.out.println("Canonical Path: " + file.getCanonicalPath());
		System.out.println("Path: " + file.getPath());
	}
}
