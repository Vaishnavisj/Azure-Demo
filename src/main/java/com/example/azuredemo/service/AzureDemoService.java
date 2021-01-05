package com.example.azuredemo.service;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Component
public class AzureDemoService {
public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=vjdemo;"+"AccountKey=ClHTkRkOmo6JBSAs7Zpv1fCfmXSW0+sF7JIOys83+ZKftz90mlo5JeH1VxiL5Bamp4NcntWxTVF+n3UA7Vnq/A==;"+"EndpointSuffix=core.windows.net;";
	
	public long getTotalCountInContainer(String containerName)
	 {
		 BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
			/*
			 * for(BlobItem blobItem : containerClient.listBlobs()) { count++; }
			 */  
		 long count1 = containerClient.listBlobs().stream().count();	 
		 return count1;
	 }
	
	public BlobClient getBlobFromContainer(String blobName ,String containerName)
	{
		 BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(storageConnectionString).buildClient();
		 BlobContainerClient containerClient = blobClient.getBlobContainerClient(containerName);
		 BlobClient blob = containerClient.getBlobClient(blobName);
		 return blob;

	}
}
