package com.service;

public class Constants {

	public static final String INPUT = "<employees>\r\n" + "<employee>\r\n" + " <name>Mohan</name>\r\n"
			+ " <age>25</age>\r\n" + " <designation>Developer</designation>\r\n" + " </employee>\r\n"
			+ " <employee>\r\n" + " <name>Anitha</name>\r\n" + " <age>40</age>\r\n"
			+ " <designation>Senior Developer</designation>\r\n" + " </employee>\r\n" + " </employees>";

	public static final String XSD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" attributeFormDefault=\"unqualified\"\r\n"
			+ "           elementFormDefault=\"qualified\">\r\n"
			+ "    <xs:element name=\"employees\" type=\"root\">\r\n" + "    </xs:element>\r\n" + "\r\n" + "\r\n"
			+ "    <xs:complexType name=\"root\">\r\n" + "        <xs:sequence>\r\n"
			+ "            <xs:element name=\"employee\" type=\"employee\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n"
			+ "        </xs:sequence>\r\n" + "    </xs:complexType>\r\n" + "\r\n"
			+ "    <xs:complexType name=\"employee\">\r\n" + "        <xs:sequence>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"name\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"age\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"designation\"/>\r\n"
			+ "			<xs:element type=\"address\" name=\"address\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n"
			+ "        </xs:sequence>\r\n" + "    </xs:complexType>\r\n" + "	\r\n"
			+ "	 <xs:complexType name=\"address\">\r\n" + "        <xs:sequence>\r\n"
			+ "             <xs:element type=\"xs:string\" name=\"doorNo\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"streetName\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"State\"/>\r\n" + "        </xs:sequence>\r\n"
			+ "    </xs:complexType>\r\n" + "</xs:schema>";

	public static final String XSD_WITH_MANDATORY_ADDRESS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" attributeFormDefault=\"unqualified\"\r\n"
			+ "           elementFormDefault=\"qualified\">\r\n"
			+ "    <xs:element name=\"employees\" type=\"root\">\r\n" + "    </xs:element>\r\n" + "\r\n" + "\r\n"
			+ "    <xs:complexType name=\"root\">\r\n" + "        <xs:sequence>\r\n"
			+ "            <xs:element name=\"employee\" type=\"employee\" maxOccurs=\"unbounded\" minOccurs=\"0\"/>\r\n"
			+ "        </xs:sequence>\r\n" + "    </xs:complexType>\r\n" + "\r\n"
			+ "    <xs:complexType name=\"employee\">\r\n" + "        <xs:sequence>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"name\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"age\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"designation\"/>\r\n"
			+ "			<xs:element type=\"address\" name=\"address\" maxOccurs=\"unbounded\" minOccurs=\"1\"/>\r\n"
			+ "        </xs:sequence>\r\n" + "    </xs:complexType>\r\n" + "	\r\n"
			+ "	 <xs:complexType name=\"address\">\r\n" + "        <xs:sequence>\r\n"
			+ "             <xs:element type=\"xs:string\" name=\"doorNo\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"streetName\"/>\r\n"
			+ "            <xs:element type=\"xs:string\" name=\"State\"/>\r\n" + "        </xs:sequence>\r\n"
			+ "    </xs:complexType>\r\n" + "</xs:schema>";

	public static final String ADD_EMPLOYEES = "<employees>\r\n" + "<employee>\r\n" + "<name>sriram</name>\r\n"
			+ "<age>26</age>\r\n" + "<designation>Senior Developer</designation>\r\n" + "<address>\r\n"
			+ "<doorNo>1</doorNo>\r\n" + "<streetName>test</streetName>\r\n" + "<State>tamil nadu</State>\r\n"
			+ "</address>\r\n" + "</employee>\r\n" + "<employee>\r\n" + "<name>varun</name>\r\n" + "<age>26</age>\r\n"
			+ "<designation>Senior Developer</designation>\r\n" + "</employee>\r\n" + "</employees>";

}
