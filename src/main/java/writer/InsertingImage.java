package writer;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class InsertingImage {

	public static void main(String args[]) throws Exception {
		// Loading an existing document
		File file = new File("sample.pdf");
		PDDocument doc = PDDocument.load(file);

		// Retrieving the page
		PDPage page = doc.getPage(0);

		// Creating PDImageXObject object
		PDImageXObject pdImage = PDImageXObject.createFromFile("Set17.png", doc);

		// creating the PDPageContentStream object
		PDPageContentStream contents = new PDPageContentStream(doc, page);

		// Drawing the image in the PDF document
		contents.drawImage(pdImage, 0, 575);

		System.out.println("Image inserted");

		// Closing the PDPageContentStream object
		contents.close();

		// Saving the document
		doc.save("sample.pdf");

		// Closing the document
		doc.close();

	}
}