package com.cars.alpr.rest

import static org.junit.Assert.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.StringUtils

import spock.lang.Specification

import com.cars.framework.config.ConfigService
import com.cars.alpr.rest.ConfigController

class ConfigControllerTest extends Specification {

	def mockMvc
	def configController
	def configService
	
	def setup(){
			configService = Mock(ConfigService)
			configController = new ConfigController(configService : configService)
			mockMvc = MockMvcBuilders.standaloneSetup(configController).build()
	}
	
	def "refresh config Service"() {
		when: 'I hit /config/refresh uri'
			def response = mockMvc.perform(get("/config/refresh")).andReturn().response
		then: 'I want to confirm response nothing'
			response.contentType == null
	
	}
	
	
	def "Test /config Service availability from Service 1of2"() {
		
		given: 'ConfigService is NOT Empty'
			ConfigService configService = Stub()
			configService.asJson() >> {return "someNonEmptyValue"}
			
			configController = new ConfigController(configService : configService)
			mockMvc = MockMvcBuilders.standaloneSetup(configController).build()
		
		when: 'I hit /config uri'
			def response = mockMvc.perform(get("/config")).andReturn().response
			def response_content = response.getContentAsString()
	
		then: 'I want to confirm response is received as TEXT and is OK'
			response.status == OK.value()
			response_content == 'someNonEmptyValue'
			
	
	}
	
	def "Test /config Service availability from Service 2of2"(){
		
		given: 'ConfigService is  Empty'
			ConfigService configService = Stub()
			configService.asJson() >> {return ""}
			
			configController = new ConfigController(configService : configService)
			mockMvc = MockMvcBuilders.standaloneSetup(configController).build()
			
			
		when: 'I hit /config uri'
			def response = mockMvc.perform(get("/config")).andReturn().response
			def response_content = response.getContentAsString()
	
		then: 'I want to confirm response is received as Text and Status is NOT_FOUND'
			response.status == NOT_FOUND.value()
			response.contentType.contains("text/plain")
			response_content == 'This application has no config setup'
			
			
	}


}
