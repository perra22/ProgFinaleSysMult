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
		int sum = 0;
		int sumLevel = 0;
		int totPixel = 0;
		double mean = 0;
		//immagine in B/W
		if(bands==1) {
			int[] levels = new int[8];
			for(int i = 1; i <= grey.length ; i++) {
				sum +=  i*grey[i-1];
				sumLevel +=  i*grey[i-1];
				totPixel += grey[i-1];
				if(i % 32 == 0) {
					levels[(i/32)-1] = sumLevel/totPixel;
					sumLevel = 0;
				}
			}
			mean = sum/totPixel;
			double meanWhite, meanBlack;
			int sumW = 0, sumB = 0;
			double vm = 0;
			double Vm = 256;
			for(int i = 0; i<3; i++) {
				System.out.println(levels[i]+" ");
				sumB +=  levels[i];
			}
			meanBlack = sumB/3;
			for(int i = levels.length-1; i>4; i--) {
				System.out.println(levels[i]+" ");
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
			System.out.println("Vm: " + Vm);
			System.out.println("vm: "+ vm);

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
				System.out.println(levels[i]+" ");
				sumB +=  levels[i];
			}
			meanBlack = sumB/3;
			
			
			for(int i = levels.length-1; i>4; i--) {
				System.out.println(levels[i]+" ");
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
			System.out.println("meanBlack: " + meanBlack);
			System.out.println("meanWhite: " + meanWhite);
			System.out.println("mean/4: " + mean/4);
			System.out.println("Vm: " + Vm);
			System.out.println("vm: "+ vm);
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
	
	//funzione gamma
	public double gamma(Double colorValue, Double p) {
		double x = colorValue;
		double y = Math.pow(x, p);
		return y;
	}
	
	//applica la funzione gamma ad ogni pixel dell'immagine
	public BufferedImage gammaCorrection(Double p) {
		
		BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				for(int b = 0; b<bands;b++) {
					int g = image.getRaster().getSample(x, y, b);
					double pixel = gamma((double)g/255, p);
					modifiedImage.getRaster().setSample(x, y, b, Math.round(pixel*255));

				}
			}	
		}
		return modifiedImage;
	}
	
}
