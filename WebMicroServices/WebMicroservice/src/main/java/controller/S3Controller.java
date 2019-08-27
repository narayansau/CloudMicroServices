package controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import config.Config;
import model.RegisterModel;
import model.S3Model;



@Controller
public class S3Controller {
	@Autowired
    private HttpServletRequest request;
	
	@RequestMapping(value="/uploadform",method = RequestMethod.POST)
	public String registerUse(@RequestParam("imageFile") MultipartFile imageFile,Model model) throws IllegalStateException, IOException
	{
		
		
		String uploadsDir = "/uploads/";
        String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
        if(! new File(realPathtoUploads).exists())
        {
            new File(realPathtoUploads).mkdir();
        }

        System.out.println("realPathtoUploads"+realPathtoUploads);
        

        String orgName = imageFile.getOriginalFilename();
        String filePath = realPathtoUploads + orgName;
        File dest = new File(filePath);
        imageFile.transferTo(dest);
        System.out.println(filePath + orgName);
        
        S3Model data = new S3Model();
		data.setFilePath(filePath);
		data.setFileName(orgName);
     
		//		todo list
//		1. create Httpheaders class object and set the type of media
//		2. create RestTemplate object and set the MessageConverter
//		3. create HttpEntity object and set the S3Model and headers to it
//		4. send data to back-end S3UploadMicroservices application using RestTemplate's postForObject method 
//     	5. get response from S3Microservices
//  	6. pass result into S3model's setFilePath method and set the S3model in model.addAttribute mathod 
//		   to get the filepath in html template  
	
	    
		return "S3Processing";
		
	}
}
