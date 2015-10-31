package AssessmentOne;

import java.awt.Color;
import java.util.Random;

import edu.princeton.cs.introcs.Picture;


public class ComponentImage 
{
	private Picture pic;
	private int width;
	private int height;
	private WeightedQuickUnionUF uf;

	private boolean pictureBinarised = false;
	private boolean pictureGreyscaled = false;
	private boolean componentsConnected = false;

	public int countComponent() throws Exception 
	{

		if (!pictureBinarised) 
		{
			throw new Exception("Picture is has not been binarised! Please call the binaryComponentImage() first!");			
		}

		int currentPixel = 0;

		for (int x = width - 1; x >= 0; x--) 
		{
			for (int y = height - 1; y >= 0; y--) 
			{
				int thisSite = currentPixel;
				Color thisColor = pic.get(x, y);

				if (y != 0 && pic.get(x, y - 1).equals(thisColor)) 
				{
					int siteToUnion = currentPixel + 1;
					uf.union(thisSite, siteToUnion);

				}
				if (x != 0 && pic.get(x - 1, y).equals(thisColor)) 
				{
					int siteToUnion = currentPixel + (height);
					uf.union(thisSite, siteToUnion);
				}
				currentPixel++;
			}

		}
		componentsConnected = true;
		return uf.count();
	}

	public Picture binaryComponentImage(int threshold) throws Exception 
	{
		if (!pictureGreyscaled) 
		{
			throw new Exception("Picture is has not been greyscaled! Please call the binaryComponentImage() first!");			
		}

		if (threshold > 255) 
		{
			throw new Exception("Threshold cannot be more than 255");
		}
		else if (threshold < 0)
		{
			throw new Exception("Threshold cannot be less than 0");
		}
		Picture binaryPicture = new Picture(pic);
		int thresholdValue = threshold;
		for (int x = 0; x < width; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				Color color = pic.get(x, y);
				if (Luminance.lum(color) <= thresholdValue) 
				{
					binaryPicture.set(x, y, Color.BLACK);
				} else {
					binaryPicture.set(x, y, Color.WHITE);
				}

			}
		}
		//binaryPicture.show();
		pic = binaryPicture;
		pictureBinarised = true;
		return binaryPicture;
	}

	public void componentImage(String fileLocation)
	{
		pic = new Picture(fileLocation);
		width = pic.width();
		height = pic.height();
		uf = new WeightedQuickUnionUF(pic.width() * pic.height());
		pictureGreyscaled = false;
		pictureBinarised = false;
		//pic.show();
	}

	public Picture returnImage()
	{
		return pic;
	}

	public void greyScaleImage() 
	{
		for (int x = 0; x < width; x++) 
		{
			for (int y = 0; y < height; y++) 
			{
				Color col = pic.get(x, y);
				Color grey = Luminance.toGray(col);
				pic.set(x, y, grey);
			}
		}
		//pic.show();
		pictureGreyscaled = true;
	}

	public Picture colourComponentImage() throws Exception 
	{
		if (!componentsConnected ) 
		{
			throw new Exception("Picture is has not been connected! Please call the countComponent() first!");
		}
		
		Picture colorComponentPicture = new Picture(pic);
		Color[] colors = new Color[uf.count()];
		int usedColors = 0;
		for (int i = 0; i < colors.length; i++) {

			Random rand = new Random();

			int  r = rand.nextInt(255);
			int  g = rand.nextInt(255);
			int  b = rand.nextInt(255);
			colors[i] = new Color(r,g,b);
		}
		int[] components = new int[uf.count()];
		int slotsUsedInComponents = 0;
		int currentPixel = 0;
		for (int x = width-1; x >=0; x--) 
		{
			for (int y = height-1; y>=0; y--) 
			{

				Color startcolor = pic.get(x, y);
				boolean hasConnection = false;

				for (int i = 0; i < slotsUsedInComponents; i++) 
				{
					if (uf.connected(currentPixel, components[i])) 
					{
						System.out.println(getX(components[i]) + "   " +  getY(components[i]));
						Color col = colorComponentPicture.get(getX(components[i]), getY(components[i]));
						colorComponentPicture.set(x, y, col);
						hasConnection = true;
					}
				}
				if (!hasConnection) {
					components[slotsUsedInComponents++] = currentPixel;
					if (x != width-1 && y != height-1) 
					{
						colorComponentPicture.set(x, y, colors[usedColors++]);
					}
					else 
					{
						colorComponentPicture.set(x, y, startcolor);
					}
				}


				currentPixel++;
			
			}
		}
		
		colorComponentPicture.show();
		pic = colorComponentPicture;
		
		return colorComponentPicture;
	}



	public Picture highlightComponentImage() throws Exception 
	{
		if (!componentsConnected) 
		{
			throw new Exception("Picture is has not been connected! Please call the countComponent() first!");
		}
		
		Picture higlightComponentPicture = new Picture(pic);
		int[] highX = new int[uf.count()];
		int[] highY = new int[uf.count()];
		int[] lowY = new int[uf.count()];
		int[] lowX = new int[uf.count()];

		int slotsUsedInComponents = 0;
		int currentPixel = 0;
		for (int x = width-1; x >=0; x--) 
		{
			for (int y = height-1; y>=0; y--) 
			{
				boolean hasConnection = false;

				for (int i = 0; i < slotsUsedInComponents; i++) 
				{
					if (uf.connected(currentPixel, highX[i])) 
					{			
						hasConnection = true;
						if (x == getX(highX[i])+1) 
						{
							if (getY(lowY[i]) < y && y < getY(highY[i])) 
							{
								highX[i] = currentPixel;
							}

						}
						else if (x==getX(lowX[i])-1) 
						{
							if (getY(lowY[i]) < y && y < getY(highY[i])) 
							{
								lowX[i] = currentPixel;
							}
						}

						if (y==getY(highY[i])+1) 
						{
							if (getX(lowX[i]) < x && x < getX(highX[i])) 
							{
								highY[i] = currentPixel;
							}
						}
						else if (y ==  getY(lowY[i] - 1)) 
						{
							if (getX(lowX[i]) < x && x < getX(highX[i])) 
							{
								lowY[i] = currentPixel;
							}
						}
					}
				}
				if (!hasConnection) 
				{
					highX[slotsUsedInComponents] = currentPixel;
					lowX[slotsUsedInComponents] = currentPixel;
					highY[slotsUsedInComponents] = currentPixel;
					lowY[slotsUsedInComponents++] = currentPixel;
				}
				currentPixel++;
			}
		}	

		currentPixel = 0;
		for (int x = width-1; x >=0; x--) 
		{
			for (int y = height-1; y>=0; y--) 
			{
				for (int i = 0; i < lowX.length; i++) 
				{
					if (x == getX(lowX[i])) 
					{
						higlightComponentPicture.set(x, y, Color.red);
					}
					if (x == getX(highX[i])) 
					{
						higlightComponentPicture.set(x, y, Color.red);
					}
					if (y == getY(lowY[i])) 
					{
						higlightComponentPicture.set(x, y, Color.red);
					}
					if (y == getY(highY[i])) 
					{
						higlightComponentPicture.set(x, y, Color.red);
					}

				}

				currentPixel++;
			}
		}
		//higlightComponentPicture.show();
		return higlightComponentPicture;
	}



	private int getY(int i) {

		return (height-1) - i % height;
	}

	private int getX(int i) {


		return (width-1) - (i-(i%height))/height;
	}
	

}
