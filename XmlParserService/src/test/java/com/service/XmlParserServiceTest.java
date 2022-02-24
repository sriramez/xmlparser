package com.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.validation.constraints.AssertTrue;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.services.impl.FileStorageServiceImpl;
import com.service.services.impl.XMLParserServiceImpl;

import xmlparser.XmlValidatorUtil;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class XmlParserServiceTest {

	@Autowired
	XMLParserServiceImpl service;

	@Autowired
	FileStorageServiceImpl fileService;

	@Autowired
	RedisTemplate<String, Object> redis;

	@Test
	public void xmlValidationTest() throws XmlValidationException, XmlReadException, IOException {

		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		XmlValidatorUtil.setValidator(xsd.getAbsolutePath());

		service.readXMLFileToString(input.getAbsolutePath(), "test");
		assertNotNull(redis.opsForHash().get("xmlparser", "test"));
	}

	@Test
	public void addNewEmployeesTest() throws IOException, XmlValidationException, XmlAddExployeeException {
		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		XmlValidatorUtil.setValidator(xsd.getAbsolutePath());

		String output = service.addEmployees("test", Constants.ADD_EMPLOYEES);
		Assert.assertTrue("User not added", output.contains("sriram"));
	}

	@Test
	public void addEmployeesWithMandatoryAddressTest() throws IOException {
		boolean validationFailed = false;
		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD_WITH_MANDATORY_ADDRESS, StandardCharsets.UTF_8);

		XmlValidatorUtil.setValidator(xsd.getAbsolutePath());

		String output = "";
		try {
			output = service.addEmployees("test", Constants.ADD_EMPLOYEES);
		} catch (XmlValidationException | XmlAddExployeeException e) {
			validationFailed = true;
		}
		assertTrue(validationFailed);
	}

	@Test
	public void RemoveEmployeTest()
			throws IOException, XmlValidationException, XmlAddExployeeException, XmlRemoveEmployeeException {
		File input = new File(fileService.getOutputPath("test") + File.separator + "test.xml");
		FileUtils.writeStringToFile(input, Constants.INPUT, StandardCharsets.UTF_8);

		File xsd = new File(fileService.getOutputPath("test") + File.separator + "test.xsd");
		FileUtils.writeStringToFile(xsd, Constants.XSD, StandardCharsets.UTF_8);

		XmlValidatorUtil.setValidator(xsd.getAbsolutePath());

		String output = service.removeEmployee("name", "sriram", "test");
		Assert.assertTrue(!output.contains("sriram"));
	}

}
