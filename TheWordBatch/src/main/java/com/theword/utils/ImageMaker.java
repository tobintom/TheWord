package com.theword.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.AttributedString;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageMaker {

	public byte[] getContentImage(String text) throws Exception{
		  	    
		    
		   //BufferedImage image = ImageIO.read(new File("./images/6.jpg"));	//7 8 14 15 16
		   BufferedImage image = ImageIO.read(getClass().getResource("/images/"+getRandomNumber(1, 11)+".jpg")); 
		   Graphics2D  g = (Graphics2D)image.getGraphics();
		    
		    Font font = new Font("Arial", Font.BOLD, 50);
		    
		    FontMetrics metrics = g.getFontMetrics(font);
		    List<String> textList=StringUtils.wrap(text, metrics, image.getWidth());
		    int h = 0;
		    for(String vContent:textList) {
		    	AttributedString attributedText = new AttributedString(vContent);
			    attributedText.addAttribute(TextAttribute.FONT, font);
			    attributedText.addAttribute(TextAttribute.FOREGROUND, Color.white);
			    attributedText.addAttribute(TextAttribute.POSTURE,TextAttribute.POSTURE_OBLIQUE);
			    attributedText.addAttribute(TextAttribute.WEIGHT,TextAttribute.WEIGHT_EXTRABOLD);

		    
		    int positionX = (image.getWidth() - metrics.stringWidth(vContent)) / 2;
		    int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent()+h;

		    g.drawString(attributedText.getIterator(), positionX, positionY);
		    h =h +55;
		    }
		    ByteArrayOutputStream os = new ByteArrayOutputStream();
		    ImageIO.write(image, "jpg", os);                    // Passing: ​(RenderedImage im, String formatName, OutputStream output)
		    		    
		    return os.toByteArray();
	}
	
	
	private  int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
}
