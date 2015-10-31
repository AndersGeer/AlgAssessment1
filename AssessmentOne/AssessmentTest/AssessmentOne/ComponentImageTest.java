package AssessmentOne;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.introcs.Picture;

public class ComponentImageTest 
{
	ComponentImage img;

	@Before
	public void beforeTests()
	{
		img = new ComponentImage();
		img.componentImage("Pictures/shapes.bmp");
	}
	@Test
	public void constructorTest()
	{
		assertNotNull(img);
	}
	
	@Test
	public void componentImageTest() 
	{	
		img = null;
		assertNull(img);
		img = new ComponentImage();
		img.componentImage("Pictures/shapes.bmp");
		Picture expected = new Picture("Pictures/shapes.bmp");
		assertEquals(expected, img.returnImage());
	}
	
	
	
	//Handled by the Picture class
	@Test  (expected = NullPointerException.class)
	public void componentImageNotImageTest() 
	{		
		img.componentImage("Pictures/TextTest.txt");
	}
	
	
	@Test 
	public void countBeforeBinary()
	{
		try 
		{
			img.countComponent();
			fail("Should throw exception");
		} 
		catch (Exception e) 
		{
			assertTrue(true);
		}
	}
	@Test 
	public void binaryBeforeGreyscale()
	{
		try 
		{
			img.binaryComponentImage(128);
			fail("Should throw exception");
		} 
		catch (Exception e) 
		{
			assertTrue(true);
		}
	}
	
	@Test 
	public void binaryNegativeThreshold()
	{
		try 
		{
			img.greyScaleImage();
			img.binaryComponentImage(-1);
			fail("Should throw exception");
		} 
		catch (Exception e) 
		{
			assertTrue(true);
		}
	}
	
	@Test 
	public void binaryZeroThreshold()
	{
		try 
		{
			img.greyScaleImage();
			img.binaryComponentImage(0);
			assertTrue(true);
		} 
		catch (Exception e) 
		{
			fail("Shouldn't throw exception");			
		}
	}
	
	@Test 
	public void binary255Threshold()
	{
		try 
		{
			img.greyScaleImage();
			img.binaryComponentImage(255);
			assertTrue(true);
		} 
		catch (Exception e) 
		{
			fail("Shouldn't throw exception");
			
		}
	}
	
	@Test 
	public void binaryTooHighThreshold()
	{
		try 
		{
			img.greyScaleImage();
			img.binaryComponentImage(256);
			fail("Should throw exception");
		} 
		catch (Exception e) 
		{
			assertTrue(true);
		}
	}
	
	
	@Test 
	public void countCorrectOrder128T()
	{	
			img.greyScaleImage();
			try 
			{
				img.binaryComponentImage(128);
				int actual = img.countComponent();
				
				
				//Static 4
				assertEquals(4,actual);
			} 
			catch (Exception e) 
			{
				fail("Should not throw exception");
			}
		
	}
	
	@Test 
	public void countCorrectOrder127T()
	{	
			img.greyScaleImage();
			try 
			{
				Picture pic = img.binaryComponentImage(127);
				int actual = img.countComponent();
				
				
				//Static 2
				assertEquals(2,actual);
			} 
			catch (Exception e) 
			{
				fail("Should not throw exception");
			}
		
	}
	@Test
	public void colorsCorrectOrder127T()
	{	
			img.greyScaleImage();
			try 
			{
				Picture bin = img.binaryComponentImage(127);
				img.countComponent();
				Picture col =  img.colourComponentImage();
				
				//Static 2
				assertNotEquals(bin, col);
			} 
			catch (Exception e) 
			{
				fail("Should not throw exception");
			}
		
	}
	@Test
	public void highlightCorrectOrder127T()
	{	
			img.greyScaleImage();
			try 
			{
				Picture bin = img.binaryComponentImage(127);
				img.countComponent();
				Picture col =  img.highlightComponentImage();
				
				//Static 2
				assertNotEquals(bin, col);
			} 
			catch (Exception e) 
			{
				fail("Should not throw exception");
			}
		
	}

}
