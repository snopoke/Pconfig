The Pconfig (Parameterised configuration) framework is a simple set of Java classes that can be used to create a configuration consisting of a set of typed parameters and untyped properties. An example use is for creating report parameter configurations.

The code for the object model uses JAXB to allow serialization of the Java objects to XML.

Main classes
------------
* Pconfig
	This is the core class that represents a configuration. It contains a List of typed parameters and a map of properties. All properties are stored as String values with String keys.
* FilledPconfig
	This class is used to represent a Pconfig that has been 'filled' or 'generated'. It contains a Pconfig whose parameter values have been set. It also has additional fields to record when it was created and the location of the 'generated' resource e.g. a report PDF.
* ScheduledPconfig
	This class is similar to the FilledPconfig but represents a 'scheduled' configuration. It too contains an Pconfig with parameter values.
	
Parameter classes
-----------------
* BooleanParameter
	Represents a boolean value
* DateParameter
	Represents a date value
* IntegerParameter
	Represents an integer value
* StringParameter
	Represents a string value
* LabelParameter
	Is a string typed parameter that is intended to display as a label and not a field to the user. It can be used to give more information to the user.
* EntityParameter
	Represents a 'entity' value. An entity in this context is meant to mean an object stored in a database but could refer to any object that is retrievable based on a property name and value. e.g. an Organisation may be retrieved from a database by searching for a property called 'id' with a value of '63'.
	
Serialization of Pconfig
------------------------
There are two ways the Pconfig objects are serialised at present:

* To XML via JAXB (see MarshallingTest.java)
* To YAML (see YamlUtils.java)

Display of Pconfig
------------------

* PconfigDialog.java
* PconfigParamterFieldFactory.java
* PconfigScheduleWizard.java