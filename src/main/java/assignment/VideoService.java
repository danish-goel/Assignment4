package assignment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class VideoService {

	List<Video> videolist=new ArrayList<Video>();
	
	@RequestMapping(value = "/video/information", method = RequestMethod.GET)
	public @ResponseBody List<Video> getInformation() 
	{
		return this.videolist;
	}
	
	@RequestMapping(value = "/video/getfile", method = RequestMethod.GET)
	public boolean getVideoFile(HttpServletResponse response)//@RequestParam("position") int position) 
	{
		String filepath=videolist.get(0).getFilePath();
		
		Path path = Paths.get(filepath);
		byte[] contents=null;
		try {
			contents = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 File file = new File(filepath);
	     InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
	        response.setContentType("application/xlsx");
	        response.setHeader("Content-Disposition", "attachment; filename="+"download"+".pdf"); 

		        ServletOutputStream out = response.getOutputStream();
		        IOUtils.copy(in, out);
		        response.flushBuffer();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

		return true;
 
        /*FileCopyUtils.copy(file.getFile(), response.getOutputStream());
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "output.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;*/
	}
	
	@RequestMapping(value = "/video/upload", method = RequestMethod.POST)
//	@PreAuthorize("hasAnyRole('user','admin')") 
	public @ResponseBody Video uploadVideo(@RequestParam("file") MultipartFile file,Principal p) 
	{
		
		Video video=new Video();
		String orgName = file.getOriginalFilename();
		video.setName(orgName);
		video.setUploader(p.getName());
		File x=new File(orgName);
	            try {
	                byte[] bytes = file.getBytes();
	                BufferedOutputStream stream = 
	                        new BufferedOutputStream(new FileOutputStream(x));
	                stream.write(bytes);
	                stream.close();
	                String filepath = x.getAbsolutePath();
	                video.setFilePath(filepath);
	            } catch (Exception e) {
	            }

		videolist.add(video);
		return video;
	}

	
	@RequestMapping(value = "video/delete", method = RequestMethod.POST)
//	@PreAuthorize("hasRole('admin')")
	public @ResponseBody Boolean deleteVideo(@RequestParam("position") int position) 
	{
		System.out.println(position);
		videolist.remove(position);
		return true;
	}
	
	
	@RequestMapping(value = "user/information", method = RequestMethod.GET)
//	@PreAuthorize("hasRole('user') and #name == #principal.getName()")
	public @ResponseBody List<Video> userInformation(@RequestParam("name") String name,Principal principal) 
	{
		return this.videolist;
	}

}
