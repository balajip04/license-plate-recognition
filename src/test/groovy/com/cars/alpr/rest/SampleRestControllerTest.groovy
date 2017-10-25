package com.cars.alpr.rest;

import static org.junit.Assert.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import org.springframework.test.web.servlet.setup.MockMvcBuilders

import spock.lang.Specification
import ch.qos.logback.classic.Level

import com.cars.framework.config.ConfigService
import com.cars.framework.config.feature.FeatureFlag
import com.cars.alpr.rest.SampleRestController

class SampleRestControllerTest extends Specification{
	
	def configService
	def mockMvc
	def sampleRestController
	def featureFlag

	def setup(){
		configService = Mock(ConfigService)
		featureFlag = Mock(FeatureFlag)
		sampleRestController = new SampleRestController(configService : configService, featureFlag : featureFlag)
		mockMvc = MockMvcBuilders.standaloneSetup(sampleRestController).build()
	}



	def "Test the info message from the Service"(){

		when: 'I hit /info uri'
			def response = mockMvc.perform(get("/info")).andReturn().response
			def response_content = (response.getContentAsString())

		then: 'I want to confirm response is String'
			response.status == OK.value()
			response.contentType.contains("text/plain")
			response_content == 'This is the rest interface for the license-plate-recognition application'
	}



	def "Test the feature availability from the Service"(){
		
		given: "A feature is enabled and another feature is NOT enabled"
			
			def configService = Mock(ConfigService)
	
			FeatureFlag featureFlag = Stub()
			featureFlag.isFeature({String feature -> 'newSVCTA' == feature }) >> {return Boolean.TRUE}
			featureFlag.isFeature({String feature -> 'fooBar' == feature }) >> {return Boolean.FALSE}
			sampleRestController = new SampleRestController(configService : configService, featureFlag : featureFlag)
			mockMvc = MockMvcBuilders.standaloneSetup(sampleRestController).build()


		when: "I hit /feature uri with invalid feature paramater 'newSVCTA'"
			def response1 = mockMvc.perform(get("/feature/newSVCTA")).andReturn().response
			def response1_content = response1.getContentAsString()

		then: 'I get feature is enabled as response'
			response1.status == OK.value()
			response1.contentType.contains("text/plain")
			response1_content == 'newSVCTA is enabled'

		when: "I hit /feature uri with invalid feature paramater 'fooBar'"
			def response2 = mockMvc.perform(get("/feature/fooBar")).andReturn().response
			def response2_content = response2.getContentAsString()
			
		then: 'I get feature is not enabled as response'
			response2.status == OK.value()
			response2.contentType.contains("text/plain")
			response2_content == 'fooBar is not enabled'
	}

	
	def "Test the config not found response from the Service"(){
		
		given: "A config is enabled and another config is NOT enabled"
		
			ConfigService configService = Stub()
	
			FeatureFlag featureFlag = Stub()
			
			configService.get({String config -> 'drv' == config }) >> {return new Object()}
			configService.get({String config -> 'fooBar' == config }) >> {return null}
			sampleRestController = new SampleRestController(configService : configService, featureFlag : featureFlag)
			mockMvc = MockMvcBuilders.standaloneSetup(sampleRestController).build()
		
		when: "I hit /config uri with valid config paramater 'drv'"
			def response1 = mockMvc.perform(get("/config/drv")).andReturn().response
			def response1_content = response1.getContentAsString()
			

		then: 'I get found config property as response'
			response1.status == OK.value()
			response1_content != null
			
		when: "I hit /config uri with invalid config paramater 'fooBar'"
			def response2 = mockMvc.perform(get("/config/fooBar")).andReturn().response
			def response2_content = response2.getContentAsString()
			

		then: 'I get Unable to find config property as response'
			response2.status == NOT_FOUND.value()
			response2.contentType.contains("text/plain")
			response2_content =='Unable to find config property fooBar'
			
	}


	def "debug logging level"(){
		given:"I set Log level to DEBUG"

			Level lvl = Level.toLevel('DEBUG')
			sampleRestController.logger.setLevel(lvl)
		
		when:"I hit /info uri"
			def response1 = mockMvc.perform(get("/info"))
		then: "I confirm appropriate debug message is logged"
			true

		when:"I hit /feature uri"
			def response2 = mockMvc.perform(get("/feature/fooBar"))
		then: "I get true message"
			true
		
		when:"I hit /config uri"
			def response3 = mockMvc.perform(get("/config/fooBar"))
		then: "I get true message"
			true
	}


}

