
**XMLParser:**

XMLParser Project is a library which contains methods to read a xml file to string , add new nodes , remove nodes based on parameter and also write file to a directory.
The above mentioned functionalities are achived using XmlUtil.

The Validation is done using an xsd file which is passed as input with XmlValidatorUtil.setValidator() method.


The setValidator method can be called again and again to update the xsd file.
The XmlValidatorUtil.validate() method is used to validate the xml file against the xsd file which returns true is valid and false if invalid.

**XML Parser service:**


The xml parser service uses Redis Cache as backend storage.This approach is used so even when the application is scaled up or down the data can be stored in a centralized server. And also the output is returned in API form and also as xml file format.

The connection properties are found in application.properties.

xmlservice.storage.path=D:/temp

The above mentioned property in application properties is where the files will be stored.

xmlservice.parser=DOMParser

The above mentioned property is used to select the parser to be used in the microservice.

Once The application is started first .xsd file should be uploaded using the /validator API else other operations can be performed.

The XmlParserService consists of 5 APIs.

![image](https://user-images.githubusercontent.com/18284660/155827817-5bfc956a-7606-4c16-88ff-e31b0eaa6564.png)


The POST/elements APi is used for initializig the data using a xml file and a unique value should be use as key. The same key must be used by that sepcific user.

The PUT /elements API is used for adding new employees. An xml file has to be uploaded as input.

The DELETE /elements API is used to delete a employee from the server based on parameter param and its value and the unique key. If a user with name Aravind has to be removed from the xml then param=name and value=Aravind has to be passed as request parameters.

The POST /validator API is used to pass the .xsd validator file This file can be uploaded at any time to change the structure of xml file that is uploaded as input.
Once uploaded new employees created should follow the same structure else they will not be uploaded.

The GET /elements API is used to retrive the complete xml after all operations are completed. This file can be retreived from the 
xmlservice.storage.path location and inside which a directory with the unique key will contain all the files uploaded by the user.

The DELETE /elements/condition API is used to delete entries based on >,<,== conditions. In order to remove all values withing a certain range the lowerbound and upperbound values should be passed.in order to remove all values less that a certain value only lower bound must be passed and upperbound should be 0 and to remove all values greater than a certain value then only the upperbound value should be passed.



