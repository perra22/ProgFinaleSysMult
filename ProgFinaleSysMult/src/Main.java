import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;

import javax.imageio.ImageIO;
import javax.lang.model.element.VariableElement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main implements ActionListener{

	static JFrame FileFrame = null;
	static JFrame histogramFrame = null;
	static BufferedImage originalImage = null, modifiedImage = null;;
	static HistogramValues isto = null;
	static Gamma gamma = null;
	static JFileChooser fileChooser = null;
	static JLabel label = null;
	static JPanel container = null;
	static JPanel buttonContainer = null;
	static JButton bH, bG = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			initializeComponents();
			int result = fileChooser.showOpenDialog(FileFrame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				originalImage = ImageIO.read(new File(selectedFile.getAbsolutePath()));
				isto = new HistogramValues(originalImage);
				gamma = new Gamma(originalImage);
				ImageIcon icon = new ImageIcon(originalImage);
				label = new JLabel(icon);
				buttonContainer.add(bH);
				buttonContainer.add(bG);
				buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
				histogramFrame.add(buttonContainer, BorderLayout.SOUTH);
				container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

			}


		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		isto.calculate();

		if(originalImage.getRaster().getNumBands() == 1) {


			HistogramPanel histogramBW = new HistogramPanel();
			histogramBW.setBorder(BorderFactory.createTitledBorder("B/W Histogram"));

			histogramBW.showHistogram(isto.grey);

			container.add(histogramBW);


			histogramFrame.add(container, BorderLayout.WEST);
			histogramFrame.add(label,BorderLayout.EAST);

			histogramFrame.setTitle("B/W Histogram");
			histogramFrame.pack();
			histogramFrame.setLocationRelativeTo(null); // Center the frame
			histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			histogramFrame.setVisible(true);
		}
		else {


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

			histogramFrame.pack();
			histogramFrame.setLocationRelativeTo(null); // Center the frame
			histogramFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			histogramFrame.setVisible(true);

		}

	}

	private static void initializeComponents() {
		FileFrame= new JFrame();
		histogramFrame = new JFrame();
		container = new JPanel();
		buttonContainer = new JPanel();
		fileChooser = new JFileChooser();
		bH = new JButton("Histogram Correction");
		bG = new JButton("Gamma Curve Correction");
		bH.setName("bH");
		bG.setName("bG");
		bH.addActionListener(new Main());
		bG.addActionListener(new Main());
		bH.setToolTipText("automatic image correction based on histograms");
		bG.setToolTipText("image correction based on gamma curve");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		//caso di click sul bottone di correzione tramite istogramma
		if(button.getName().equals("bH")) {
			modifiedImage = isto.histoCorrection();
			HistogramValues istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();

			JLabel label = new JLabel(new ImageIcon(modifiedImage)); 
			JFrame f = new JFrame("Corrected picture"); 


			if(modifiedImage.getRaster().getNumBands() == 1) {
				HistogramPanel histogramBW = new HistogramPanel();
				histogramBW.setBorder(BorderFactory.createTitledBorder("Corrected B/W Histogram"));
				histogramBW.showHistogram(istoCorrected.grey);

				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				f.getContentPane().add(label,BorderLayout.EAST);
				f.getContentPane().add(histogramBW);  
				f.pack();
				f.setLocation(100,100);
				f.setVisible(true);
			}
			
			//caso di click sul bottone di correzione tramite gamma curve
			else {
				HistogramPanel histogramR = new HistogramPanel();
				HistogramPanel histogramG = new HistogramPanel();
				HistogramPanel histogramB = new HistogramPanel();
				HistogramPanel histogramI = new HistogramPanel();

				histogramR.setBorder(BorderFactory.createTitledBorder("Red Histogram"));
				histogramG.setBorder(BorderFactory.createTitledBorder("Green Histogram"));
				histogramB.setBorder(BorderFactory.createTitledBorder("Blue Histogram"));
				histogramI.setBorder(BorderFactory.createTitledBorder("Intensity Histogram"));

				histogramR.showHistogram(istoCorrected.red);
				histogramG.showHistogram(istoCorrected.green);
				histogramB.showHistogram(istoCorrected.blue);
				histogramI.showHistogram(istoCorrected.rgb);

				JPanel container = new JPanel();
				container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

				container.add(histogramR);
				container.add(histogramG);
				container.add(histogramB);
				container.add(histogramI);

				f.getContentPane().add(container);
				f.add(label,BorderLayout.EAST);

				f.pack();
				f.setLocationRelativeTo(null);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setVisible(true);
			}

		}
		else {
			
			Double getP = getDoubleValue();
			JLabel label = null;
			JFrame f = null;
			HistogramValues istoCorrected = null;
			//riprongo la finestra di inserimento fin che non viene inserito un valore corretto
			if(getP!=null) {
				while (getP == -1.0) {
					getP = getDoubleValue();
				}
				modifiedImage = gamma.gammaCorrection(getP);
				istoCorrected = new HistogramValues(modifiedImage);
				istoCorrected.calculate();

				label = new JLabel(new ImageIcon(modifiedImage)); 
				f = new JFrame("Corrected picture"); 

				if(modifiedImage.getRaster().getNumBands() == 1) {
					HistogramPanel histogramBW = new HistogramPanel();
					histogramBW.setBorder(BorderFactory.createTitledBorder("Corrected B/W Histogram"));
					histogramBW.showHistogram(istoCorrected.grey);

					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
					f.getContentPane().add(label,BorderLayout.EAST);
					f.getContentPane().add(histogramBW);  
					f.pack();
					f.setLocation(100,100);
					f.setVisible(true);
				}
				else {
					HistogramPanel histogramR = new HistogramPanel();
					HistogramPanel histogramG = new HistogramPanel();
					HistogramPanel histogramB = new HistogramPanel();
					HistogramPanel histogramI = new HistogramPanel();

					histogramR.setBorder(BorderFactory.createTitledBorder("Red Histogram"));
					histogramG.setBorder(BorderFactory.createTitledBorder("Green Histogram"));
					histogramB.setBorder(BorderFactory.createTitledBorder("Blue Histogram"));
					histogramI.setBorder(BorderFactory.createTitledBorder("Intensity Histogram"));

					histogramR.showHistogram(istoCorrected.red);
					histogramG.showHistogram(istoCorrected.green);
					histogramB.showHistogram(istoCorrected.blue);
					histogramI.showHistogram(istoCorrected.rgb);

					JPanel container = new JPanel();
					container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

					container.add(histogramR);
					container.add(histogramG);
					container.add(histogramB);
					container.add(histogramI);

					f.getContentPane().add(container);
					f.add(label,BorderLayout.EAST);

					f.pack();
					f.setLocationRelativeTo(null);
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setVisible(true);
				}
			}
		}

	}

//	funzione che mostra una finestra di inserimento e che restituisce un double se il valore inserito e' corretto
	public Double getDoubleValue() {
		Double i;
		try {
			String resString = JOptionPane.showInputDialog("Insert the value of P (it must be a number): ", "");
			if(resString!= null) {
				i = Double.parseDouble(resString);
				return i;
			}
			return null;
		} catch (Exception e) {
			return -1.0;
		}


	}

}
