import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage originalImage = null;
		HistogramValues isto = null;
		JFileChooser fileChooser = null;
		JLabel label = null;
		try {
			JFrame FileFrame= new JFrame();
			fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(FileFrame);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    originalImage = ImageIO.read(new File(selectedFile.getAbsolutePath()));
			    isto = new HistogramValues(originalImage);
			    ImageIcon icon = new ImageIcon(originalImage);
		        label = new JLabel(icon);
			}
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		isto.calculate();
		
		if(originalImage.getRaster().getNumBands() == 1) {
			JFrame histogramFrame = new JFrame();
			
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			
		    HistogramPanel histogramBW = new HistogramPanel();
		    histogramBW.setBorder(BorderFactory.createTitledBorder("B/W Histogram"));

		    histogramBW.showHistogram(isto.grey);

		    container.add(histogramBW);
    
		    
		    histogramFrame.add(container);
		    histogramFrame.add(label,BorderLayout.EAST);

		    histogramFrame.setTitle("B/W Histogram");
		    histogramFrame.pack();
		    histogramFrame.setLocationRelativeTo(null); // Center the frame
		    histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    histogramFrame.setVisible(true);
		}
		else {
			

			JFrame histogramFrame= new JFrame();
			
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			
		    HistogramPanel histogramR = new HistogramPanel();
		    HistogramPanel histogramG = new HistogramPanel();
		    HistogramPanel histogramB = new HistogramPanel();
		    HistogramPanel histogramI = new HistogramPanel();
		    
		    histogramR.setBorder(BorderFactory.createTitledBorder("Red Histogram"));
		    histogramG.setBorder(BorderFactory.createTitledBorder("Green Histogram"));
		    histogramB.setBorder(BorderFactory.createTitledBorder("Blue Histogram"));
		    histogramI.setBorder(BorderFactory.createTitledBorder("Intensity Histogram"));

		    // Send the data to the histogram for display
		    histogramR.showHistogram(isto.red);
		    histogramG.showHistogram(isto.green);
		    histogramB.showHistogram(isto.blue);
		    histogramI.showHistogram(isto.rgb);
		    
		    container.add(histogramR);
		    container.add(histogramG);
		    container.add(histogramB);
		    container.add(histogramI);
		    
		    histogramFrame.getContentPane().add(container);
		    histogramFrame.add(label,BorderLayout.EAST);
		    
		    histogramFrame.setTitle("Red Histogram");
		    histogramFrame.pack();
		    histogramFrame.setLocationRelativeTo(null); // Center the frame
		    histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    histogramFrame.setVisible(true);
		   
		}
		
	}

}
