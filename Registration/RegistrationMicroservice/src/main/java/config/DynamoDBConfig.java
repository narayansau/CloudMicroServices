package config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

@Component
@PropertySource("application.properties")

public class DynamoDBConfig {
 
   
    private static String amazonDynamoDBEndpoint;
  
    private static String amazonAWSAccessKey;
   
    private static  String amazonAWSSecretKey;
    private static String dynamoDBTableName;
    
    
    
    @Value("${secretkey}")
    public void setSecretKey(String secretkey) {
    	
    	amazonAWSSecretKey = secretkey;
    	System.out.println(amazonAWSSecretKey);
    	
    }
    @Value("${accesskey}") 
    public void setAccessKey(String accesskey) {
    	
    	amazonAWSAccessKey = accesskey;
    	System.out.println(amazonAWSAccessKey);
    	
    }
    @Value("${endpoint}")
    public void setRegion(String region) {
    	
    	amazonDynamoDBEndpoint = region;
    }
    @Value("${dynamodbtable}")
    public void setTableName(String tableName) {
    	
    	dynamoDBTableName = tableName;
    }
public String getTableName() {
    	
    	return dynamoDBTableName;
    }
 
    public AmazonDynamoDBClient amazonDynamoDB() {
    		AmazonDynamoDBClient amazonDynamoDB 
          = new AmazonDynamoDBClient(amazonAWSCredentials());
         
    		String endpoint = "dynamodb."+amazonDynamoDBEndpoint+".amazonaws.com";
    		amazonDynamoDB.setEndpoint(endpoint);
         
        return amazonDynamoDB;
    }
    
    
	private AWSCredentials amazonAWSCredentials() {
    	return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}
