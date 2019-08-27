package controller;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.util.TableUtils.TableNeverTransitionedToStateException;
import config.DynamoDBConfig;
import model.DynamoDBModel;

@RestController
public class DynamoDBController {

	AmazonDynamoDB dynamoDBClient;
	DynamoDB dynamoDB;
	String tableName;
    private DynamoDBMapperConfig importProductDBConfig;
	@RequestMapping(value="/dynamodbregisterform",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public String registerUser(@RequestBody DynamoDBModel dynamoDBModel )
	{

		DynamoDBConfig dynamoDBConfigObject = new DynamoDBConfig();
		dynamoDBClient = dynamoDBConfigObject.amazonDynamoDB();
		tableName = dynamoDBConfigObject.getTableName();
		dynamoDB = new DynamoDB(dynamoDBClient);
		
		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
				.withKeySchema(new KeySchemaElement().withAttributeName("Email").withKeyType(KeyType.HASH))
				.withKeySchema(new KeySchemaElement().withAttributeName("Password").withKeyType(KeyType.RANGE))
				.withAttributeDefinitions(new AttributeDefinition().withAttributeName("Email").withAttributeType(ScalarAttributeType.S))
				.withAttributeDefinitions(new AttributeDefinition().withAttributeName("Password").withAttributeType(ScalarAttributeType.S))
				.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

		TableUtils.createTableIfNotExists(dynamoDBClient, createTableRequest);

		try {
			TableUtils.waitUntilActive(dynamoDBClient, tableName);
		} catch (TableNeverTransitionedToStateException | InterruptedException e) {
			e.printStackTrace();
		}

		insertRecord(dynamoDBModel.getEmail(),dynamoDBModel.getPassword());
		return "Success";		

	}
	
	@RequestMapping(value="/dynamodbloginform",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public String login(@RequestBody DynamoDBModel dynamoDBModel )
	{

		//Implement  Dynamo Db Table creation  code if table doesn't exist
		//If table exist then use the same table.

		DynamoDBConfig dynamoDBConfigObject = new DynamoDBConfig();
		dynamoDBClient = dynamoDBConfigObject.amazonDynamoDB();
		dynamoDB = new DynamoDB(dynamoDBClient);

		DynamoDBModel email = new DynamoDBModel();
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
		email.setEmail(dynamoDBModel.getEmail());

		DynamoDBQueryExpression<DynamoDBModel> queryExpression = new DynamoDBQueryExpression<DynamoDBModel>()
				.withHashKeyValues(email);

		List<DynamoDBModel> itemList = mapper.query(DynamoDBModel.class, queryExpression);
		if(itemList.get(0).getEmail().contains(dynamoDBModel.getEmail()))
		{
			System.out.println(itemList.get(0).getEmail());
			if(itemList.get(0).getPassword().contains(dynamoDBModel.getPassword()))
			{
				return "successfull";
			}
			else
			{
				return "Email id or Password Incorrect";
			}

		}
		else
		{
			return "Email id or Password Incorrect";
		}

	}


	public String insertRecord(String name, String pwd)
	{
		importProductDBConfig = new DynamoDBMapperConfig(new TableNameOverride(tableName));
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient,importProductDBConfig);		

		DynamoDBModel DynamoDBModelitem = new DynamoDBModel();
		DynamoDBModelitem.setEmail(name);

		DynamoDBModelitem.setPassword(pwd);

		mapper.save(DynamoDBModelitem);

		String ans = DynamoDBModelitem.getEmail() + DynamoDBModelitem.getPassword() ;
		return ans;	
	}

}

