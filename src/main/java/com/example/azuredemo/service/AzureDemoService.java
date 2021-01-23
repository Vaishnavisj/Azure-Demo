package com.example.azuredemo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

@Component
public class AzureDemoService {
	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=vjdemo;"+"AccountKey=nq/A==;"+"EndpointSuffix=core.windows.net;";
	
	public long getTotalCountInContainer(String containerName)
	 {
		 BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
		 long count=0;
			  for(BlobItem blobItem : containerClient.listBlobs()) 
			  { 
				  count++; 
			  
			  }
			 return count;
		 
		
	 }
	
	public BlobClient getBlobFromContainer(String blobName ,String containerName)
	{
		 BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
		 BlobClient blob = containerClient.getBlobClient(blobName);
		 return blob;

	}
	public CloudBlobContainer getCloudBlobContainer(String containerName) throws InvalidKeyException, URISyntaxException, StorageException
	{
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		   CloudBlobContainer container = blobClient.getContainerReference(containerName);
		   return container;
	}
	
	public boolean checkGdgIsValid(String containerName , String gdgToBeUploadedName) throws InvalidKeyException, URISyntaxException
	{
		 List<String> blobNames = new ArrayList<String>();
		BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
		 for(BlobItem blobItem : containerClient.listBlobs()) 
		  { 
			 	blobNames.add(blobItem.getName());
		  }
		 if(blobNames.contains(gdgToBeUploadedName))
		 {
			 return true;
		 }
		   return false;
	}
	
	public List<String> getListOfBlobsInContainer()
	{
		 BlobServiceClient blobClient1 = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient1.getBlobContainerClient("demo");
		 List<String> blobNames = new ArrayList<String>();

		 for(BlobItem blobItem : containerClient.listBlobs())
		    {
			 	blobNames.add(blobItem.getName());
		    }
		 return blobNames;
	}
	
	public long getTotalCountUsingVersionFile(String containerName) throws IOException
	{
		 long count=0;
		 File gdgVersion = new File("/Users/vaishnavi/Downloads/poc/src/main/resources/gdgVersion");
		 BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
		 BlobClient blob = containerClient.getBlobClient("gdgVersion");
		 if(!blob.exists())
		 {
			 count=1;
			 if(gdgVersion.createNewFile())
			 {
				 System.out.println("gdgVersion file created ");
			 }
			 return count;
			 
		 }
		 else
		 {
			 blob.downloadToFile("/Users/vaishnavi/Downloads/poc/src/main/resources/gdgVersion",true);
			 FileReader fr=new FileReader(gdgVersion); 
			 BufferedReader br = new BufferedReader(fr);  
			 String line;  
			 while((line=br.readLine())!=null)
			 {
				 count = Long.parseLong(line);
	 
			 }
		 }
		return count;
	}
	
	public boolean updateGdgVersion() throws IOException
	{
		 BlobClient blobGdgVersion = getBlobFromContainer("gdgVersion", "demo");
		 File gdgVersion = new File("/Users/vaishnavi/Downloads/poc/src/main/resources/gdgVersion");
		 gdgVersion.setWritable(true);
		 FileWriter fileWriter = new FileWriter(gdgVersion);
		 long count = getTotalCountUsingVersionFile("demo");
		 String strcount = String.valueOf(count);
		 BufferedWriter writer = new BufferedWriter(fileWriter);
		 fileWriter.write(strcount);
		 fileWriter.flush();
		 fileWriter.close();
		 writer.close();
		 blobGdgVersion.uploadFromFile("/Users/vaishnavi/Downloads/poc/src/main/resources/gdgVersion",true); 
		 return true;

	}
	
}
