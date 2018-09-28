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
		BufferedImage modifiedImage = null;
		int sum = 0;
		int totPixel = 0;
		double mean = 0;
		//immagine in B/W
		if(grey.length > 0) {
			for(int i = 0; i<grey.length ; i++) {
				sum +=  i*grey[i];
				totPixel += grey[i];
			}
			mean = sum/totPixel;
			System.out.println("Media: " + Double.toString(mean));
			System.out.println("Somma: " + sum);
		}
		else {
			
		}
		return modifiedImage;
	}
	
}
