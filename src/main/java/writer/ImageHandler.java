package writer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageHandler {

	public static BufferedImage createImage(JPanel panel) {
		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.paint(g);
		g.dispose();
		return bi;
	}

//	public static void saveImage(String name, JPanel panel) {
////		File outputfile = new File(name);
////		try {
////			ImageIO.write(createImage(panel), "jpg", outputfile);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		JFrame frame = new JFrame(name);
//		frame.getContentPane().setLayout(new FlowLayout());
//		frame.getContentPane().add(new JLabel(new ImageIcon(createImage(panel))));
//		frame.pack();
//		frame.setVisible(true);
//	}

	public static void saveImage(String name, JPanel component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		// paints into image's Graphics
		component.paint(image.getGraphics());
		try {
			ImageIO.write(resize(image, 311, 171), "png", new File(name + ".png"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error");
		}
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
