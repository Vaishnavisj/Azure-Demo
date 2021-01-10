package com.example.azuredemo.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobItem;
import com.example.azuredemo.service.AzureDemoService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;


@RestController
@RequestMapping("/azure/demo")
public class AzureDemoController {

	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=vjdemo;"+"AccountKey=ClHTkRkOmo6JBSAs7Zpv1fCfmXSW0+sF7JIOys83+ZKftz90mlo5JeH1VxiL5Bamp4NcntWxTVF+n3UA7Vnq/A==;"+"EndpointSuffix=core.windows.net;";

	
	@Autowired
	private AzureDemoService azureDemoService;
	
	 @RequestMapping("/addNewGdg/{containerName}/{fileName}")
	 public ResponseEntity<Object> addNewFile(@PathVariable String containerName,@PathVariable String fileName) throws InvalidKeyException, URISyntaxException, StorageException
	 {
		 long count = azureDemoService.getTotalCountInContainer(containerName);
		 StringBuilder sb = new StringBuilder("WelcomeABC");
		 sb.append("v"+count);
		 //BlobClient blob = containerClient.getBlobClient(sb.toString());
		 BlobClient blob = azureDemoService.getBlobFromContainer(sb.toString(), "demo");
		 blob.setAccessTier(AccessTier.COOL);
		 blob.uploadFromFile("/Users/vaishnavi/Downloads/azure-demo/src/main/resources/WelcomeABC.rtf");
		 return new ResponseEntity<Object> (" upload successful for file " + sb.toString(), HttpStatus.OK);


	 }
	 
	 @RequestMapping("/retrieveOldGdg/{fileName}/{index}")
	 public void retrieveOldFile(@PathVariable String containerName,@PathVariable String fileName,@PathVariable long index)
	 {
		 long count = azureDemoService.getTotalCountInContainer("demo");
		 long versionToRetrieve = count-index;
		 StringBuilder sb = new StringBuilder(fileName);
		 sb.append("v"+versionToRetrieve);
		 File file = new File("Users/vaishnavi/Downloads/azure-demo/src/main/resources/newoutput");
		 BlobClient blob = azureDemoService.getBlobFromContainer(sb.toString(), "demo");
		 if(file.exists())
		 {
			 file.delete();
		 }
		 blob.downloadToFile("/Users/vaishnavi/Downloads/azure-demo/src/main/resources/newoutput");
	 }
	 
		/*
		 * @RequestMapping("/getBlobVersions/{containerName}") public
		 * ResponseEntity<Object> getBlobVersions(@PathVariable String containerName) {
		 * List<String> versionlist = new ArrayList<String>(); BlobContainerClient
		 * containerClient = azureDemoService.getBlobContainerClient(containerName);
		 * long count = containerClient.listBlobs().stream().count(); for(BlobItem
		 * blobItem : containerClient.listBlobs()) {
		 * versionlist.add(blobItem.getVersionId());
		 * 
		 * } return new ResponseEntity<Object> (versionlist, HttpStatus.OK); }
		 */
	 
	 @GetMapping("/{containerName}")
	 public ResponseEntity<Object> getContainerFromBlob(@PathVariable String containerName) throws InvalidKeyException, URISyntaxException, StorageException
	 {
		 CloudBlobContainer container = azureDemoService.getCloudBlobContainer(containerName);
		 container.createIfNotExists();
		 return new ResponseEntity<Object> (container.getName() + " is found/created", HttpStatus.OK);
	 }
	 
	 @GetMapping("/delete/{containerName}")
	 public ResponseEntity<Object> deleteContainer(@PathVariable String containerName) throws InvalidKeyException, URISyntaxException, StorageException
	 {
		 CloudBlobContainer container = azureDemoService.getCloudBlobContainer(containerName);
		 if(container.deleteIfExists())
		 {
			 return new ResponseEntity<Object> (container.getName() + " is deleted", HttpStatus.OK);
		 }
		 return new ResponseEntity<Object> (container.getName() + " is not found", HttpStatus.OK); 
	 }
	 
	 @GetMapping("/{containerName}/getListOfBlobs")
	 public ResponseEntity<Object> getListOfBlobs(@PathVariable String containerName) throws InvalidKeyException, URISyntaxException, StorageException
	 {
		 List<String> blobs = new ArrayList<String>();
		 CloudBlobContainer container = azureDemoService.getCloudBlobContainer(containerName);
		 for (ListBlobItem blobItem : container.listBlobs()) {
			 blobs.add(blobItem.getUri().toString());
		 }
		 return new ResponseEntity<Object> (blobs, HttpStatus.OK); 

	 }
}

