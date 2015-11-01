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

	/* ************************************************************************************** *\
	 * 																						  *\
	 * Returns the count of components in the image given (Count of objects + background)	* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public int countComponent() throws Exception 
	{
		//Checks if picture has been binarised
		if (!pictureBinarised) 
		{
			throw new Exception("Picture is has not been binarised! Please call the binaryComponentImage() first!");			
		}

		int currentPixel = 0;

		//Goes from bottom right to top left, column by column
		for (int x = width - 1; x >= 0; x--) 
		{
			for (int y = height - 1; y >= 0; y--) 
			{
				int thisSite = currentPixel;
				Color thisColor = pic.get(x, y);
				//Checks if the pixel to the left has the same color as the current pixel
				if (y != 0 && pic.get(x, y - 1).equals(thisColor)) 
				{
					int siteToUnion = currentPixel + 1;
					uf.union(thisSite, siteToUnion);

				}
				//Checks if the pixel below has the same color as the current pixel
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



	/* ************************************************************************************** *\
	 * 																						  *\
	 * Returns a binarised picture of the greyscaled picture								* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public Picture binaryComponentImage(int threshold) throws Exception 
	{
		//Checks if picture has been converted to a greyscale image and if threshold values are within the allowed range
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

		//Makes a copy of the image to work on
		Picture binaryPicture = new Picture(pic);
		int thresholdValue = threshold;

		//Works from Top left to bottom right
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
		binaryPicture.show();
		pic = binaryPicture;
		pictureBinarised = true;
		return binaryPicture;
	}

	/* ************************************************************************************** *\
	 * 																						  *\
	 * Initialises the image. Gets height and width											* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public void componentImage(String fileLocation)
	{
		pic = new Picture(fileLocation);
		width = pic.width();
		height = pic.height();
		uf = new WeightedQuickUnionUF(pic.width() * pic.height());
		pictureGreyscaled = false;
		pictureBinarised = false;
		componentsConnected = false;
		//pic.show();
	}


	/* ************************************************************************************** *\
	 * 																						  *\
	 * Test method. Used to see if componentImage() method works							* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public Picture returnImage()
	{
		return pic;
	}


	/* ************************************************************************************** *\
	 * 																						  *\
	 * Changes the original picture to a greyscale											* *\
	 * 																						  *\
	 * ************************************************************************************** */
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
		pic.show();
		pictureGreyscaled = true;
	}


	/* ************************************************************************************** *\
	 * 																						  *\
	 * Returns an image with components colored												* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public Picture colourComponentImage() throws Exception 
	{
		if (!componentsConnected ) 
		{
			throw new Exception("Picture is has not been connected! Please call the countComponent() first!");
		}

		Picture colorComponentPicture = new Picture(pic);
		Color[] colors = new Color[uf.count()];
		int usedColors = 0;
		//Randomizes x colors where x is the number of components in the picture
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

				/*
				 * For loop is a little bit special, on first pixel it won't be run as slotsUsedInComponents is zerom thus 
				 * hasConnection boolean is false and the if statement is run.
				 * 
				 * When it is run it checks for each first pixel in a component we've met if the current pixel 
				 * is connected with it and if they are, make it the same color
				 */
				for (int i = 0; i < slotsUsedInComponents; i++) 
				{
					if (uf.connected(currentPixel, components[i])) 
					{
						Color col = colorComponentPicture.get(getX(components[i]), getY(components[i]));
						colorComponentPicture.set(x, y, col);
						hasConnection = true;
					}
				}

				//Changes color depending on the value of usedColors integer. Adds the new components pixel to the components array
				if (!hasConnection) {
					components[slotsUsedInComponents++] = currentPixel;
					if (x != width-1 && y != height-1) 
					{
						colorComponentPicture.set(x, y, colors[usedColors++]);
					}
					else 
					{
						/*
						 * Set the FIRST pixel encountered to the original color.
						 * 
						 * It is assumed the first pixel is the background this however might not always be the case.
						 */
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


	/* ************************************************************************************** *\
	 * 																						  *\
	 * Returns a picture with components highlighted with red squares						* *\
	 * 																						  *\
	 * ************************************************************************************** */
	public Picture highlightComponentImage() throws Exception 
	{
		if (!componentsConnected) 
		{
			throw new Exception("Picture is has not been connected! Please call the countComponent() first!");
		}

		//Initialising four arrays to hold the highest/lowest values
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
						//Finding the highest and lowest values for each component
						hasConnection = true;
						if (x >getX(highX[i])+1) 
						{

							highX[i] = currentPixel;


						}
						else if (x<getX(lowX[i])) 
						{

							lowX[i] = currentPixel;

						}

						if (y>getY(highY[i])) 
						{

							highY[i] = currentPixel;

						}
						else if (y <  getY(lowY[i])) 
						{

							lowY[i] = currentPixel;

						}
					}
				}
				if (!hasConnection) 
				{
					//Adds the new component to all of the arrays.
					highX[slotsUsedInComponents] = currentPixel;
					lowX[slotsUsedInComponents] = currentPixel;
					highY[slotsUsedInComponents] = currentPixel;
					lowY[slotsUsedInComponents++] = currentPixel;
				}
				currentPixel++;
			}
		}	


		//Coloring the adjacent
		currentPixel = 0;
		for (int x = width-1; x >=0; x--) 
		{
			for (int y = height-1; y>=0; y--) 
			{
				for (int i = 0; i < lowX.length; i++) 
				{
					if (x == getX(lowX[i]+1)) 
					{
						if (getY(lowY[i]) <= y && y <= getY(highY[i])) 
						{
							higlightComponentPicture.set(x, y, Color.red);
						}
					}
					if (x == getX(highX[i]-1)) 
					{
						if (getY(lowY[i]) <= y && y <= getY(highY[i])) 
						{
							higlightComponentPicture.set(x, y, Color.red);
						}
					}
					if (y == getY(lowY[i]+1)) 
					{
						if (getX(lowX[i]) <= x && x <= getX(highX[i])) 
						{
							higlightComponentPicture.set(x, y, Color.red);
						}
					}
					if (y == getY(highY[i]-1)) 
					{
						if (getX(lowX[i]) <= x && x <= getX(highX[i])) 
						{
							higlightComponentPicture.set(x, y, Color.red);
						}
					}

				}

				currentPixel++;
			}
		}
		higlightComponentPicture.show();
		return higlightComponentPicture;
	}



	private int getY(int i) {

		return (height-1) - i % height;
	}

	private int getX(int i) {


		return (width-1) - (i-(i%height))/height;
	}
	
	public static void main(String[] args) {
		ComponentImage img = new ComponentImage();
		img.componentImage("Pictures/bacteria.bmp");
		img.greyScaleImage();
		try {
			img.binaryComponentImage(128);
			img.countComponent();
			img.highlightComponentImage();
			img.colourComponentImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
