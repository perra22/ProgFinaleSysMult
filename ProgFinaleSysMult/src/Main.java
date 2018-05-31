import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage originalImage = null;
		HistogramValues isto = null;
		try {
			originalImage = ImageIO.read(new File("Guinness.jpg"));
			isto = new HistogramValues(originalImage);

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		isto.calculate();
		
		JFrame histogramFrame = new JFrame();
		JFrame histogramFrameR = new JFrame();
		JFrame histogramFrameG = new JFrame();
		JFrame histogramFrameB = new JFrame();

	    HistogramPanel histogramBW = new HistogramPanel();
	    HistogramPanel histogramR = new HistogramPanel();
	    HistogramPanel histogramG = new HistogramPanel();
	    HistogramPanel histogramB = new HistogramPanel();

	    // Send the data to the histogram for display
	    histogramBW.showHistogram(isto.grey);
	    histogramR.showHistogram(isto.red);
	    histogramG.showHistogram(isto.green);
	    histogramB.showHistogram(isto.blue);
	    
	    
	    // Create a new frame to hold the histogram panel
	    histogramFrame.add(histogramBW);
	    histogramFrameR.add(histogramR);
	    histogramFrameG.add(histogramG);
	    histogramFrameB.add(histogramB);
	    
	    histogramFrame.setTitle("Example Histogram");
	    histogramFrame.pack();
	    histogramFrame.setLocationRelativeTo(null); // Center the frame
	    histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    histogramFrame.setVisible(true);
	    
	    histogramFrameR.setTitle("Example Histogram");
	    histogramFrameR.pack();
	    histogramFrameR.setLocationRelativeTo(null); // Center the frame
	    histogramFrameR.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    histogramFrameR.setVisible(true);
	    
	    histogramFrameG.setTitle("Example Histogram");
	    histogramFrameG.pack();
	    histogramFrameG.setLocationRelativeTo(null); // Center the frame
	    histogramFrameG.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    histogramFrameG.setVisible(true);
	    
	    histogramFrameB.setTitle("Example Histogram");
	    histogramFrameB.pack();
	    histogramFrameB.setLocationRelativeTo(null); // Center the frame
	    histogramFrameB.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    histogramFrameB.setVisible(true);
	}

}
