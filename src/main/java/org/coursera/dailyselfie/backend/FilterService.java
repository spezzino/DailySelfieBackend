package org.coursera.dailyselfie.backend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.KaleidoscopeFilter;

@Service
public class FilterService {
	@Async
	public Future<ByteArrayOutputStream> applyFilter(MultipartFile file,
			String effect) throws InterruptedException {

		if (!file.isEmpty() && file.getContentType().equals("image/jpeg")) {
			BufferedImage sourceImg = null;
			try {
				sourceImg = ImageIO.read(file.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedImage destImg = null;

			if (effect.toUpperCase().equals("GRAYSCALE")) {
				GrayscaleFilter grayscalefilter = new GrayscaleFilter();
				destImg = grayscalefilter.createCompatibleDestImage(sourceImg, sourceImg.getColorModel());
				grayscalefilter.filter(sourceImg, destImg);
				System.out.println("Grayscale filter applied");
			} else if (effect.toUpperCase().equals("INVERT")) {
				InvertFilter invertFilter = new InvertFilter();
				destImg = invertFilter.createCompatibleDestImage(sourceImg, sourceImg.getColorModel());
				invertFilter.filter(sourceImg, destImg);
				System.out.println("Invert filter applied");
			} else if (effect.toUpperCase().equals("KALEIDOSCOPE")) {
				KaleidoscopeFilter kaleidoscopeFilter = new KaleidoscopeFilter();
				kaleidoscopeFilter.setAngle((float) ((Math.random()*1000)%360));
				kaleidoscopeFilter.setAngle2((float) ((Math.random()*1000)%360));
				destImg = kaleidoscopeFilter.createCompatibleDestImage(sourceImg, sourceImg.getColorModel());
				kaleidoscopeFilter.filter(sourceImg, destImg);
				System.out.println("Kaleidoscope filter applied");
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(destImg, "jpg", outputStream);
				System.out.println("returning modified image");
				return new AsyncResult<ByteArrayOutputStream>(outputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new AsyncResult<>(null);
	}
}
