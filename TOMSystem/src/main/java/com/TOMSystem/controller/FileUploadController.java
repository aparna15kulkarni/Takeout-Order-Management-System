package com.TOMSystem.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.TOMSystem.Item.Item;
import com.TOMSystem.service.ItemService;
import com.TOMSystem.service.ItemServiceImpl;

@Controller
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	@Autowired
	private ItemService itemService;
	/**
	 * Upload single file using Spring Controller
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String setupForm(Map<String, Object> map){
		Item item= new Item();
		map.put("item", item);
		map.put("itemList", itemService.getAllItems());
		return "addItem";
	}
	
	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("item_name") String item_name,
			@RequestParam("image") MultipartFile file, @RequestParam("category") String category, 
			@RequestParam("item_price") double item_price, @RequestParam("item_calories") double item_calories, 
			@RequestParam("item_prep_time") int item_prep_time, Map<String, Object> map) {
		
		//creating item pojo
		Item tempItem = new Item();
		
		if (!file.isEmpty()) {
			try {

				tempItem.setName(item_name);
				tempItem.setCalories(item_calories);
				tempItem.setCategory(category);
				tempItem.setPicture("");
				tempItem.setPrep_time(item_prep_time);
				tempItem.setUnit_price(item_price);
				
				//add item to table
				itemService.addItem(tempItem);
				
				//fetch the item_id and name the image by it.
				String imageName = tempItem.getId() + "";
				tempItem.setPicture(imageName);
				
				//update the item
				itemService.editItem(tempItem);
				
				//uploading image
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "images");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + imageName);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				logger.info("Server File Location=" + serverFile.getAbsolutePath());
				System.out.println("Server File Location=" + serverFile.getAbsolutePath());
				
				map.put("message", "Successful");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("message", "Exception Occured!");
				return "You failed to upload " + item_name + " => " + e.getMessage();
			}
		} else {
			map.put("message", "Empty image!");
		}
		map.put("item", tempItem);
		map.put("itemList", itemService.getAllItems());
		return "addItem";
	}	
}