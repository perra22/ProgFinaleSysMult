//package histogram;

// THIS PROGRAM IS MODIFIED FROM THE FOLLOWING SOURCE:
// http://www.cs.armstrong.edu/liang/intro6e/evennumberedexercise/Histogram.java
// By G. Leonardi, 06/10/14

import javax.swing.*;
import java.awt.*;

public class HistogramPanel extends JPanel {
	// Count of occurrences of each category
	private int[] count;

	/** Set the count histogram */
	public void showHistogram(int[] count) {
		this.count = count;
		repaint();
	}

	/** Paint the histogram */
	protected void paintComponent(Graphics g) {
		if (count == null) return; // No display if count is null

		super.paintComponent(g);

		// Find the panel size and bar width and interval dynamically
		int width = getWidth();
		int height = getHeight();

		// Find the maximum count. The maximum count has the highest bar
		int maxCount = 0;
		for (int i = 0; i < count.length; i++) {
			if (maxCount < count[i])
				maxCount = count[i];
		}

		// Draw a horizontal base line
		g.drawLine(10, height - 44, 10+ 2*256, height - 44);
		for (int i = 0; i < count.length; i++) {
			// Find the bar height
			int barHeight = (int)(((double)count[i] / (double)maxCount) * (double)(height - 85));

			// Find the bar width and position
			int Xs = (int)(10 + 2*i);

			// Set the draw color
			g.setColor(Color.black);

			// Display the vertical line
			g.drawLine(Xs, height - 45, Xs, height - 45 - barHeight);
			g.drawLine(Xs+1, height - 45, Xs+1, height - 45 - barHeight);

			g.setColor(Color.black);
		}
	}

	/** Override getPreferredSize */
	public Dimension getPreferredSize() {
		return new Dimension(20 + 2*256, 300);
	}
}