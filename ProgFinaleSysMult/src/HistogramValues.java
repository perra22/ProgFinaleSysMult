import java.awt.Image;
import java.awt.image.BufferedImage;

public class HistogramValues {

	BufferedImage image;
	int[] grey, red, green, blue, rgb;
	int bands;

	public HistogramValues(BufferedImage image) {
		this.image = image;
		bands = image.getRaster().getNumBands();
		if(bands == 1) {
			grey = new int[256];
		}
		else {
			red = new int[256];
			green = new int[256];
			blue = new int[256];
			rgb = new int[256];
		}
	}

	public void calculate() {
		if(bands == 1) {
			for(int x = 0; x<image.getWidth(); x++) {
				for(int y = 0; y<image.getHeight(); y++) {
					grey[image.getRaster().getSample(x, y, 0)]++;
				}
			}
		}
		else {
			for(int x = 0; x<image.getWidth(); x++) {
				for(int y = 0; y<image.getHeight(); y++) {
					int r = image.getRaster().getSample(x, y, 0);
					int g = image.getRaster().getSample(x, y, 1);
					int b = image.getRaster().getSample(x, y, 2);
					int intensity = (int)Math.round(0.333*r + 0.588*g + 0.114*b);
					red[r]++;
					green[g]++;
					blue[b]++;
					if(intensity<256)
						rgb[intensity]++;
				}
			}
		}

	}

	public BufferedImage histoCorrection() {
		BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int sum = 0; //variabile temporanea usata come sommatoria
		int sumLevel = 0; //variabile temporanea usata come sommatoria
		int totPixel = 0; //numero di pixel totali
		double mean = 0; //variabile rappresentate la media
		//immagine in B/W
		if(bands==1) {
			int[] levels = new int[8]; //array in cui memorizzo l'istogramma diviso in 8 parti
			for(int i = 1; i <= grey.length ; i++) {
				sum +=  i*grey[i-1];
				sumLevel +=  i*grey[i-1];
				totPixel += grey[i-1];
				
				//divido l'istogramma in 8 livelli 
				//quindi se l'indice %32 e' uguale a zero devo memorizzarmi la media di
				//quel livello e reinizializzare a 0 la variabile temporanea
				if(i % 32 == 0) {
					levels[(i/32)-1] = sumLevel/totPixel; //salvo la media associata a quell'intervallo
					sumLevel = 0;
				}
			}
			mean = sum/totPixel; //calcolo la media generale
			double meanWhite, meanBlack;
			int sumW = 0, sumB = 0;
			double vm = 0;
			double Vm = 256;
			//considero come livelli significativi per le ombre i primi 3 livelli (0-96)
			for(int i = 0; i<3; i++) {
				sumB +=  levels[i];
			}
			meanBlack = sumB/3;//calcolo la media delle ombre
			
			//considero come livelli significativi per le luci gli ultimi 3 livelli (160-256)
			for(int i = levels.length-1; i>4; i--) {
				sumW +=  levels[i];
			}
			meanWhite = sumW/3;//calcolo la media delle luci

			//se il valore della media delle ombre e' inferiore alla media generale /4 e quella delle luci e' maggiore
			//probabilmente  l'immagine risultera' essere sovraesposta e dunque aumento il valore di vm mentre lascio invariato Vm
			if(meanBlack<mean/4 && meanWhite>mean/4) {
				Vm = 256;
				vm = 96;
			}
			//se il valore della media delle ombre e' maggiore della media generale /4 e quella delle luci e' minore
			//probabilmente  l'immagine risultera' essere sottoesposta e dunque diminuisco il valore di Vm e lascio invariato vm
			if(meanBlack>mean/4 && meanWhite<mean/4) {
				Vm = 160;
				vm = 0;
			}
			//se entrambe le medie di luci e ombre sono minori della media /4
			//l'immagine potrebbe presentare un contrasto basso e dunque sposto sia vm che Vm
			if(meanBlack<mean/4 && meanWhite<mean/4) {
				Vm = 160;
				vm = 96;
			}
			//se il valore della media /2 (valore scelto per non effettuare una correzione troppo aggressiva)
			//e' maggiore di entrambe le medie di luci ed ombre probabilmente l'immagine e' stata scattata correttamente e dunque non
			//apporto nessuna modifica
			if(meanBlack>mean/2 && meanWhite>mean/2) {
				Vm = 256;
				vm = 0;
			}

			//applica la scelta di correzione
			for(int x = 0; x < image.getWidth(); x++) {
				for(int y = 0; y < image.getHeight(); y++) {
					int g = image.getRaster().getSample(x, y, 0);
					modifiedImage.getRaster().setSample(x, y, 0, Math.max(0, Math.min(255, 255*(g - vm)/(Vm - vm))));
				}
			}
		}
		else {
			int[] levels = new int[8];
			for(int i = 1; i <= rgb.length ; i++) {
				sum +=  i*rgb[i-1];
				totPixel += rgb[i-1];
				sumLevel +=  i*rgb[i-1];
				if(i % 32 == 0) {
					if(totPixel!=0) {
						levels[(i/32)-1] = sumLevel/totPixel;
					}
					else {
						levels[(i/32)-1] = 0;

					}
					sumLevel = 0;

				}

			}
			mean = sum/totPixel;
			double meanWhite, meanBlack;
			int sumW = 0, sumB = 0;
			double vm = 0;
			double Vm = 256;

			for(int i = 0; i<3; i++) {
				sumB +=  levels[i];
			}
			meanBlack = sumB/3;


			for(int i = levels.length-1; i>4; i--) {
				sumW +=  levels[i];
			}
			meanWhite = sumW/3;

			if(meanBlack<mean/4 && meanWhite>mean/4) {
				Vm = 256;
				vm = 96;
			}
			if(meanBlack>mean/4 && meanWhite<mean/4) {
				Vm = 160;
				vm = 0;
			}
			if(meanBlack<mean/4 && meanWhite<mean/4) {
				Vm = 160;
				vm = 96;
			}
			if(meanBlack>mean/2 && meanWhite>mean/2) {
				Vm = 256;
				vm = 0;
			}
			for(int b = 0; b<bands;b++) {
				for(int x = 0; x < image.getWidth(); x++) {
					for(int y = 0; y < image.getHeight(); y++) {
						int g = image.getRaster().getSample(x, y, b);
						modifiedImage.getRaster().setSample(x, y, b, Math.max(0, Math.min(255, 255*(g - vm)/(Vm - vm))));
					}
				}
			}

		}
		return modifiedImage;
	}

}
