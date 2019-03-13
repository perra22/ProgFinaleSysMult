import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;

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
	static JPanel buttonHistoContainer = null;
	static JPanel buttonGammaContainer = null;
	static JButton bGManuale, bGFixed, bGAuto = null;
	static JButton bH, bHManuale, bHNaive = null;

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

				buttonHistoContainer.add(bHManuale);
				buttonHistoContainer.add(bHNaive);
				buttonHistoContainer.add(bH);
				buttonHistoContainer.setLayout(new BoxLayout(buttonHistoContainer, BoxLayout.Y_AXIS));

				buttonGammaContainer.add(bGManuale);
				buttonGammaContainer.add(bGFixed);
				buttonGammaContainer.add(bGAuto);
				buttonGammaContainer.setLayout(new BoxLayout(buttonGammaContainer, BoxLayout.Y_AXIS));

				buttonContainer.add(buttonHistoContainer);
				buttonContainer.add(buttonGammaContainer);
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

			for (HistogramPanel hp : initilizeHistogram(isto)) {
				container.add(hp);
			}

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
		buttonHistoContainer = new JPanel();
		buttonGammaContainer = new JPanel();
		fileChooser = new JFileChooser();

		//bottoni istogrammi
		bH = new JButton("Auto Histogram Correction");
		bHNaive = new JButton("Naive Histogram Correction");
		bHManuale = new JButton("Manual Histogram Correction");

		//bottoni curva gamma
		bGFixed = new JButton("Gamma Curve Correction");
		bGAuto = new JButton("Smart Gamma Curve Correction");
		bGManuale = new JButton("Manual Gamma Curve Correction");

		bH.setName("bH");
		bHNaive.setName("bHNaive");
		bHManuale.setName("bHManuale");

		bGFixed.setName("bG2");
		bGAuto.setName("bGAuto");
		bGManuale.setName("bGManuale");

		bH.addActionListener(new Main());
		bHNaive.addActionListener(new Main());
		bHManuale.addActionListener(new Main());

		bGFixed.addActionListener(new Main());
		bGAuto.addActionListener(new Main());
		bGManuale.addActionListener(new Main());


	}

	private static ArrayList<HistogramPanel> initilizeHistogram(HistogramValues istoCorrected){

		ArrayList<HistogramPanel> isto = new ArrayList<>();

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

		isto.add(histogramR);
		isto.add(histogramG);
		isto.add(histogramB);
		isto.add(histogramI);

		return isto;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
		HistogramValues istoCorrected = null;
		JLabel label = null; 
		JFrame f = new JFrame("Corrected picture"); 

		//caso di click sul bottone di correzione tramite istogramma
		if(button.getName().equals("bHManuale")) {

			ArrayList<Integer> valori = get_vm_Vm();

			//riprongo la finestra di inserimento fin che non viene inserito un valore corretto
			if(valori != null) {
				while (valori.get(0) == -1.0) {
					valori = get_vm_Vm();
				}
			}
			modifiedImage = isto.manualHistoCorrection(valori.get(0), valori.get(1));
			istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();
			label = new JLabel(new ImageIcon(modifiedImage)); 

		}
		else if(button.getName().equals("bHNaive")) {
			modifiedImage = isto.naiveHistoCorrection();
			istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();
			label = new JLabel(new ImageIcon(modifiedImage)); 
		}
		else if(button.getName().equals("bH")) {
			modifiedImage = isto.histoCorrection();
			istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();
			label = new JLabel(new ImageIcon(modifiedImage)); 


		}
		else if(button.getName().equals("bGManuale")) {

			Double getP = getDoubleValue();

			//riprongo la finestra di inserimento fin che non viene inserito un valore corretto
			if(getP!=null) {
				while (getP == -1.0) {
					getP = getDoubleValue();
				}
				modifiedImage = gamma.gammaCorrection(getP);
				istoCorrected = new HistogramValues(modifiedImage);
				istoCorrected.calculate();
				label = new JLabel(new ImageIcon(modifiedImage)); 

			}
		}

		else if(button.getName().equals("bG2")) {


			modifiedImage = gamma.gammaCorrection2(isto);
			istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();
			label = new JLabel(new ImageIcon(modifiedImage)); 

		}
		
		else if(button.getName().equals("bGAuto")) {


			modifiedImage = gamma.gammaAutoCorrection(isto);
			istoCorrected = new HistogramValues(modifiedImage);
			istoCorrected.calculate();
			label = new JLabel(new ImageIcon(modifiedImage)); 

		}


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

			container.removeAll();

			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

			for (HistogramPanel hp : initilizeHistogram(istoCorrected)) {
				container.add(hp);
			}

			f.getContentPane().add(container);
			f.add(label,BorderLayout.EAST);

			f.pack();
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
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

	public ArrayList<Integer> get_vm_Vm() {
		ArrayList<Integer> v = new ArrayList<>();
		try {
			String vm = JOptionPane.showInputDialog("Insert the value of vm (it must be a number 0<vm<256): ", "");
			String Vm = JOptionPane.showInputDialog("Insert the value of Vm (it must be a number 0<Vm<256): ", "");
			if(vm!= null && Vm != null) {
				int vmRes = Integer.parseInt(vm);
				int VmRes = Integer.parseInt(Vm);
				if (vmRes<256 && vmRes >=0 && VmRes <256 && VmRes >=0) {
					v.add(vmRes);
					v.add(VmRes);

					return v;
				}
			}
			v.add(-1);
			return v;
		} catch (Exception e) {
			v.add(-1);
			return v;
		}


	}

}
