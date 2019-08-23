package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interpreter.Dot;
import writer.ImageHandler;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private static final int PREF_W = 466;
	private static final int PREF_H = 256;
	private static final Color GRAPH_COLOR = Color.BLACK;
	private static final Color GRAPH_POINT_COLOR = Color.black;
	private static final Stroke GRAPH_STROKE = new BasicStroke(1.0f);
	private static final int GRAPH_POINT_WIDTH = 3;
	private JLabel setNumberLabel = new JLabel();
	private JLabel countsLabel = new JLabel();
	private BufferedImage image;
	private List<double[]> cords;
	List<Integer> counts;
	List<String> sets;

	public ImagePanel(List<double[]> points, List<Integer> counts, List<String> sets) {
		this.cords = points;
		this.counts = counts;
		this.sets = sets;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		try {
			Image bg = ImageIO.read(Paths.get("field.png").toFile());
			g2.drawImage(bg, 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = 2.83;
		double yScale = 2.8;

		List<double[]> graphPoints = new ArrayList<double[]>();
		for (int i = 0; i < cords.size(); i++) {
			double x1 = cords.get(i)[0] * xScale + 7;
			double y1 = (85.3333333 - cords.get(i)[1]) * yScale + 4;

			if (i == cords.size() - 1) {
				JLabel ptLbl = new JLabel(sets.get(i));
				ptLbl.setBounds((int) x1, (int) y1, 25, 20);
				ptLbl.setForeground(Color.BLACK);
				ptLbl.setVisible(true);
				this.add(ptLbl);
			}
			graphPoints.add(new double[] { x1, y1 });
		}

		Stroke oldStroke = g2.getStroke();
		g2.setColor(GRAPH_COLOR);
		g2.setStroke(GRAPH_STROKE);
		// countsLabel.setLocation((int) graphPoints.get(graphPoints.size() - 1)[0] + 3,
		// (int) graphPoints.get(graphPoints.size() - 1)[1] - 17);
		for (int i = 0; i < graphPoints.size() - 1; i++) {
			double x1 = graphPoints.get(i)[0];
			double y1 = graphPoints.get(i)[1];
			double x2 = graphPoints.get(i + 1)[0];
			double y2 = graphPoints.get(i + 1)[1];

			// double yOffset = (y2 - y1) / 2;
			// double xOffset = (x2 - x1) / 2;
			//
			// countsLabel.setLocation((int) (graphPoints.get(graphPoints.size() - 1)[0] -
			// xOffset),
			// (int) (graphPoints.get(graphPoints.size() - 1)[1] - yOffset - 30));
			if (i == graphPoints.size() - 2) {
				JLabel ptLbl = new JLabel(counts.get(i + 1).toString() + " counts");
				Font f = new Font("Courier", Font.BOLD, 12);
				ptLbl.setFont(f);
				ptLbl.setHorizontalAlignment(JLabel.CENTER);
				ptLbl.setBounds((int) (x1 / 2 + x2 / 2), (int) (y1 / 2 + y2 / 2 - 45), 80, 40);
				ptLbl.setVisible(true);
				this.add(ptLbl);
			}
			g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		}

		g2.setStroke(oldStroke);
		g2.setColor(GRAPH_POINT_COLOR);

		for (int i = 0; i < graphPoints.size(); i++) {
			int x = (int) (graphPoints.get(i)[0] - GRAPH_POINT_WIDTH / 2);
			int y = (int) (graphPoints.get(i)[1] - GRAPH_POINT_WIDTH / 2);
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			setNumberLabel.setLocation(x + 2, y + 2);
			g2.fillOval(x, y, ovalW, ovalH);
		}

		// setNumberLabel.setText(sets.get(sets.size() - 1));
		// Font f = setNumberLabel.getFont();
		// Font f = new Font("Courier", Font.BOLD, 12);
		// countsLabel.setFont(f);
		// countsLabel.setText(counts.get(counts.size() - 1).toString() + " Counts");

		// this.add(setNumberLabel);
		// this.add(countsLabel);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}

	private static ImagePanel instance;

	private static void setInstance(ImagePanel i) {
		instance = i;
	}

	public static ImagePanel getInstance() {
		return instance;
	}

	public static void createAndShowGui(List<Dot> dots) {

		List<double[]> points = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();
		List<String> sets = new ArrayList<>();

		for (int i = 0; i < dots.size(); i++) {
			points.add(i, dots.get(i).getXYCoords());
			counts.add(i, dots.get(i).getCounts());
			sets.add(i, dots.get(i).getSetNumber());
		}
		// points.add(new double[] { 0, 0 });
		// points.add(new double[] { 160, 85.33333 });
		ImagePanel mainPanel = new ImagePanel(points, counts, sets);
		setInstance(mainPanel);

		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		// frame.setVisible(true);

		ImageHandler.saveImage("deploy\\Set" + dots.get(dots.size() - 1).getSetNumber(), mainPanel);
		
	}

}