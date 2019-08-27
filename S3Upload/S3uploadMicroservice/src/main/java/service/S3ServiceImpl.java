package service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.stereotype.Service;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;


import config.Config;

@Service
public class S3ServiceImpl {

	private AmazonS3 s3;
	String bucketName;
	public S3ServiceImpl() throws IOException
    {
    super();
     s3 = new AmazonS3Client();
    
     }

		public String s3ImageDownload(String imageName, String filePath)	{

		Config dynamoDBConfigObject = new Config();
		s3 = dynamoDBConfigObject.amazonS3();
		bucketName = dynamoDBConfigObject.getS3Bucket();

		try { 
			
			S3ServiceImpl.startCompress(bucketName,imageName,filePath,s3);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageName;
	}
	public static  void startCompress(String srcBucket,String srcKey, String filePath, AmazonS3 s3)
	{
		try
		{
			
			System.out.println("S3Object: "+srcKey);
			File compressedfile=S3ServiceImpl.convert(filePath, srcKey);	
			S3ServiceImpl.pushobject(srcBucket,srcKey, compressedfile,s3);
		}
		catch(Exception e)
		{
			System.out.println("1. Exception occured with trace"+e.getMessage());
		}
	}
	 
	 public static File convert( String filePath,String filename)
     {
     	  File compressedImageFile = null;
     	try{

//    
    		    File input = new File(filePath);
     		      BufferedImage image = ImageIO.read(input);
     		      compressedImageFile = new File(filePath);
     		      OutputStream os =new FileOutputStream(compressedImageFile);

     		      Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
     		      ImageWriter writer = (ImageWriter) writers.next();

     		      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
     		      writer.setOutput(ios);

     		      ImageWriteParam param = writer.getDefaultWriteParam();
     		      
     		      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
     		      param.setCompressionQuality(0.05f);
     		      writer.write(null, new IIOImage(image, null, null), param);
     		      
     		      os.close();
     		      ios.close();
     		      writer.dispose();
     	}catch(IOException e){
     		System.out.println(e);
     	}
			return compressedImageFile;
     }
	 public static void pushobject(String srcBucket,String filename,File compressedImageFile, AmazonS3 s3)
     {
     try
     {
       System.out.println("Uploadbucket: "+srcBucket);
       ClientConfiguration ct = new ClientConfiguration();
       ct.setConnectionTimeout(500);
     
       s3.putObject(new PutObjectRequest(srcBucket,filename,compressedImageFile).withCannedAcl(CannedAccessControlList.PublicRead));
       
     }

     catch(Exception e)
     {
     	System.out.println("4.Exception occured with trace\n"+e+"\n"+e.getMessage());
       
     }

  }


	}
