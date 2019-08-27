package controller;


import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import config.Config;
import model.S3Model;
import service.S3ServiceImpl;

@RestController
public class S3UploadController {

	@RequestMapping(value="/upload",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public String imageUpload(@RequestBody S3Model model) throws IllegalStateException, IOException
	{
		S3ServiceImpl s3Services = new S3ServiceImpl();
		s3Services.s3ImageDownload(model.getFileName(),model.getFilePath());
		Config ConfigObject = new Config();
		String endpoint = ConfigObject.getS3Endpoint();
		String bucketName = ConfigObject.getS3Bucket();
		String url = "https://s3-"+endpoint+".amazonaws.com"+"/"+bucketName+"/"+model.getFileName();
		return url;	
	}

}
