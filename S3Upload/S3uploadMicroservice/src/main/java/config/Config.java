package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

@Component
@PropertySource("application.properties")

public class Config {
 
    
    private static String AmazonS3Endpoint;
    private static String bucketName;
    private static String amazonAWSAccessKey;
    private static  String amazonAWSSecretKey;
    
    
    @Value("${secretkey}")
    public void setSecretKey(String secretkey) {
    	
    	amazonAWSSecretKey = secretkey;
    	
    }
    @Value("${accesskey}") 
    public void setAccessKey(String accesskey) {
    	
    	amazonAWSAccessKey = accesskey;
    	//System.out.println(amazonAWSAccessKey);
    	
    }
    @Value("${endpoint}")
    public void setRegion(String region) {
    	
    	AmazonS3Endpoint = region;
    	System.out.println(AmazonS3Endpoint);
    	
    }
    @Value("${s3bucket}")
    public void setBucket(String bucket) {
    	bucketName = bucket;
    }
 
    public AmazonS3Client amazonS3() {
    	AmazonS3Client amazonS3 = new AmazonS3Client(amazonAWSCredentials());
    	String endpoint = "s3."+AmazonS3Endpoint+".amazonaws.com";
    	amazonS3.setEndpoint(endpoint);

    	try {
    		if(amazonS3.doesBucketExist(bucketName)){
   			 System.out.println("S3 bucket already exits");
   		 }
   		 else{
   			amazonS3.createBucket(bucketName);
   			 System.out.println("S3 bucket created");
   		 }// Get location.
            String bucketLocation = amazonS3.getBucketLocation(new GetBucketLocationRequest(bucketName));
            System.out.println("bucket location = " + bucketLocation);

         } catch (AmazonServiceException ase) {
         System.out.println("bucket in some other region");
         }
    return amazonS3;
    }
	
    public String getS3Endpoint() {
    	return AmazonS3Endpoint;
    }
    public String getS3Bucket() {
    	return bucketName;
    }
    
	private AWSCredentials amazonAWSCredentials() {
    	return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}
