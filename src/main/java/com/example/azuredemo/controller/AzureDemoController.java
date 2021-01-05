package com.example.azuredemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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
		  StringBuilder sb = new StringBuilder(fileName);
		  sb.append("v"+count);
		  BlobClient blob = azureDemoService.getBlobFromContainer(sb.toString(), containerName);
		  blob.uploadFromFile("/Users/vaishnavi/Downloads/azure-demo/src/main/resources/WelcomeABC.rtf");
	 }
	 
	 @RequestMapping("retrieveOldGdg/{fileName}/{index}")
	 public void retrieveOldFile(@PathVariable String containerName,@PathVariable String fileName,@PathVariable long index)
	 {
		 long count = azureDemoService.getTotalCountInContainer("demo");
		 long versionToRetrieve = count-index;
		 StringBuilder sb = new StringBuilder(fileName);
		 sb.append("v"+versionToRetrieve);
		 BlobClient blob = azureDemoService.getBlobFromContainer(sb.toString(), "demo");
		 blob.downloadToFile("/Users/vaishnavi/Downloads/azure-demo/src/main/resources/newoutput");
	 }
}
