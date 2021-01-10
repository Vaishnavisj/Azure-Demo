package com.example.azuredemo.service;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

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
	
	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=vjdemo;"+"AccountKey=ClHTkRkOmo6JBSAs7Zpv1fCfmXSW0+sF7JIOys83+ZKftz90mlo5JeH1VxiL5Bamp4NcntWxTVF+n3UA7Vnq/A==;"+"EndpointSuffix=core.windows.net;";
	
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
}
