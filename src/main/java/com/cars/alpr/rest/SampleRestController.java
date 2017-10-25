package com.cars.alpr.rest;

import com.cars.alpr.Main;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cars.framework.config.ConfigService;
import com.cars.framework.config.feature.FeatureFlag;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Sample REST service using Spring's REST API
 */
// @Restcontroller = @Controller + @ResponseBody
@RestController
public class SampleRestController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigService configService;

	@Autowired
	private FeatureFlag featureFlag;

	@Autowired
	Main main;

	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "text/plain")
	public String info() {
		if (logger.isDebugEnabled()) {
			logger.debug("/info called on " + getClass().getName());
		}

		return "This is the rest interface for the social-publishing-gateway1.0 application";
	}

	@RequestMapping(value = "/plate", method = RequestMethod.POST, produces = "application/json",consumes = {"multipart/form-data"})
	public String licensePlateRecognition(@RequestPart("file") MultipartFile image) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("/plate called on " + getClass().getName());
		}
		String fileName = "File_"+Math.random();
		File convFile = new File(fileName);
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(image.getBytes());
		fos.close();
		String plateNumber = main.main("us","/etc/license-plate-recognition/openalpr.conf","/usr/share/license-plate-recognition/runtime_data",fileName);
		convFile.delete();
		return "{\"plateNumber\":"+"\""+plateNumber+"\",\"state\":\"il\"}";
	}

	@RequestMapping(value = "/feature/{feature:.+}", method = RequestMethod.GET, produces = "text/plain")
	public ResponseEntity<String> feature(@PathVariable("feature") String feature) {
		if (logger.isDebugEnabled()) {
			logger.debug("/feature/" + feature + " called on " + getClass().getName());
		}
		if (featureFlag.isFeature(feature)) {
			return new ResponseEntity<String>(feature + " is enabled", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(feature + " is not enabled", HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/config/{config:.+}", method = RequestMethod.GET, produces = "text/plain")
	public ResponseEntity<String> config(@PathVariable("config") String config) {
		if (logger.isDebugEnabled()) {
			logger.debug("/config/" + config + " called on " + getClass().getName());
		}
		Object obj = configService.get(config);
		if (obj == null) {
			return new ResponseEntity<String>("Unable to find config property " + config, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
		}
	}

}
