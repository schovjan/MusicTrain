/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain.manager;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

/**
 *
 * @author schovanek
 */
public class ImageManager {

	public ImageManager() {
	}

	public List<BufferedImage> loadImages(File folder) throws IOException {
		List<BufferedImage> images = new ArrayList<>();
		List<File> list = new ArrayList();
		Collections.addAll(list, folder.listFiles());
		Collections.sort(list);
		for (File f : list) {
			String mimetype = new MimetypesFileTypeMap().getContentType(f);
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				BufferedImage img = ImageIO.read(f);
				images.add(img);
			}
		}
		return images;
	}

	public Dimension getScaleSize(BufferedImage img, Dimension maxDimension) {
		double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(img.getWidth(), img.getHeight()), maxDimension));
		int scaleWidth = (int) Math.round(img.getWidth() * scaleFactor);
		int scaleHeight = (int) Math.round(img.getHeight() * scaleFactor);
		return new Dimension(scaleWidth, scaleHeight);
	}

	private double getScaleFactor(int iMasterSize, int iTargetSize) {
		double dScale = 1;
		if (iMasterSize > iTargetSize) {
			dScale = (double) iTargetSize / (double) iMasterSize;
		} else {
			dScale = (double) iTargetSize / (double) iMasterSize;
		}
		return dScale;
	}

	private double getScaleFactorToFit(Dimension original, Dimension toFit) {
		double dScale = 1d;
		if (original != null && toFit != null) {
			double dScaleWidth = getScaleFactor(original.width, toFit.width);
			double dScaleHeight = getScaleFactor(original.height, toFit.height);
			dScale = Math.min(dScaleHeight, dScaleWidth);
		}
		return dScale;
	}
}
