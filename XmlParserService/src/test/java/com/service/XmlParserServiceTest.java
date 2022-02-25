package com.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlValidatorNotPresentException;
import com.service.services.interfaces.FileStorageService;
import com.service.services.interfaces.XmlParserService;

import xmlparser.XMLValidator;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlParserServiceTest {

	@Autowired
	XmlParserService service;

	@Autowired
	FileStorageService fileService;

	@Autowired
	RedisTemplate<String, Object> redis;

	@Test
	public void xmlValidationTest() throws XmlValidationException, XmlReadException, IOException, InterruptedException,
			XmlValidatorNotPresentException {

		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		service.loadValidator(xsd.getAbsolutePath(), "test");
		Thread.sleep(5000);
		service.readXMLFileToString(input.getAbsolutePath(), "test");
		assertNotNull(redis.opsForHash().get("xmlparser", "test"));
	}

	@Test
	public void addNewEmployeesTest() throws IOException, XmlValidationException, XmlAddExployeeException,
			InterruptedException, XmlValidatorNotPresentException {
		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		service.loadValidator(xsd.getAbsolutePath(), "test");
		Thread.sleep(5000);
		String output = service.addElements("test", Constants.ADD_EMPLOYEES);
		Assert.assertTrue("User not added", output.contains("sriram"));
	}

	@Test
	public void addEmployeesWithMandatoryAddressTest()
			throws IOException, XmlValidationException, InterruptedException, XmlValidatorNotPresentException {
		boolean validationFailed = false;
		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD_WITH_MANDATORY_ADDRESS, StandardCharsets.UTF_8);

		service.loadValidator(xsd.getAbsolutePath(), "test");
		Thread.sleep(5000);
		String output = "";
		try {
			output = service.addElements("test", Constants.ADD_EMPLOYEES);
		} catch (XmlValidationException | XmlAddExployeeException e) {
			validationFailed = true;
		}
		assertTrue(validationFailed);
	}

	@Test
	public void RemoveEmployeTest() throws IOException, XmlValidationException, XmlAddExployeeException,
			XmlRemoveEmployeeException, InterruptedException {
		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		service.loadValidator(xsd.getAbsolutePath(), "test");
		Thread.sleep(5000);

		String output = service.removeElements("name", "sriram", "test");
		Assert.assertTrue(!output.contains("sriram"));
	}

}
