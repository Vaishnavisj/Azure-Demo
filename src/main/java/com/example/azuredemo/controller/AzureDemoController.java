package com.example.azuredemo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


@RestController
@RequestMapping("/azure/demo")
public class AzureDemoController {

	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=vjdemo;"+"AccountKey=ClHTkRkOmo6JBSAs7Zpv1fCfmXSW0+sF7JIOys83+ZKftz90mlo5JeH1VxiL5Bamp4NcntWxTVF+n3UA7Vnq/A==;"+"EndpointSuffix=core.windows.net;";

	
	@Autowired
	private AzureDemoService azureDemoService;
	
	 @RequestMapping("/addNewGdg/{containerName}/{fileName}/{index}")
	 public void addNewFile(@PathVariable String containerName,@PathVariable String fileName)
	 {
		 long count = azureDemoService.getTotalCountInContainer(containerName);
		 StringBuilder sb = new StringBuilder("WelcomeABC");
		 sb.append("v"+count);
		 //BlobClient blob = containerClient.getBlobClient(sb.toString());
		 BlobClient blob = azureDemoService.getBlobFromContainer(sb.toString(), "demo");
		 blob.setAccessTier(AccessTier.COOL);
		 blob.uploadFromFile("/Users/vaishnavi/Downloads/azure-demo/src/main/resources/WelcomeABC.rtf");
	 }
	 
	 @RequestMapping("retrieveOldGdg/{fileName}/{index}")
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
	 
	 @RequestMapping("getBlobVersions/{containerName}")
	 public  ResponseEntity<Object> getBlobVersions(@PathVariable String containerName)
	 {
		 List<String> versionlist = new ArrayList<String>();
		 BlobContainerClient containerClient = azureDemoService.getBlobContainerClient(containerName);
		 long count = containerClient.listBlobs().stream().count();  
		 for(BlobItem blobItem : containerClient.listBlobs())
		    {
		    	versionlist.add(blobItem.getVersionId());
		    	
		    }
		    return new ResponseEntity<Object> (versionlist, HttpStatus.OK);
	 }
}

