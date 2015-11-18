package org.coursera.dailyselfie.backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FilterController {

	@Autowired
    FilterService filterService;
	
	@RequestMapping(value = "/applyFilter", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody void filter(@RequestParam("image") MultipartFile file, @RequestParam("effect") String effect,
			HttpServletResponse response) {
		
		Future<ByteArrayOutputStream> task;
		try {
			task = filterService.applyFilter(file, effect);
			ByteArrayOutputStream outputStream = task.get();
			if(outputStream != null){
				response.setStatus(HttpStatus.OK.value());
//				String base64 = Base64.encodeBase64String(outputStream.toByteArray());
//				return base64;
				response.setContentType("image/jpeg");
				response.getOutputStream().write(outputStream.toByteArray());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		response.setStatus(HttpStatus.BAD_REQUEST.value());
	}

}
