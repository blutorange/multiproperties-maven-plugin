package com.github.blutorange.multiproperties_maven_plugin.parser;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.defaultIfEmpty;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.SAXException;

/**
 * Utility methods to parse multiproperties files.
 */
public final class MultipropertiesParser {
  /**
   * Parses a multiproperties file. This method can handle different versions of the multiproperties file format. It
   * returns a wrapper that abstracts over the different versions.
   * @param file A multiproperties file to parse.
   * @return The parsed multiproperties file.
   * @throws JAXBException When the file does not exist or does not conform to the multiproperties XML schema.
   * @throws ParserConfigurationException If not XML parser is available.
   * @throws IOException When the file could not be read.
   * @throws SAXException If the file is not a valid XML file.
   * @throws XPathExpressionException When an XPath could not be evaluated against the multiproperties XML document.
   */
  public static IMultiproperties parse(Path file) throws JAXBException, SAXException, IOException, ParserConfigurationException, XPathExpressionException {
    final var version = readVersion(file);
    switch (version) {
      case "1.0":
        return parseVersion_1_0(file);
      case "1.1":
        return parseVersion_1_1(file);
      case "1.2":
        return parseVersion_1_2(file);
      default:
        throw new JAXBException("Cannot parsed multiproperties file, unknown file version: " + version);
    }
  }

  private static XPathExpression compileXPath(String expression) throws XPathExpressionException {
    final var xPathFactory = XPathFactory.newInstance();
    final var xPath = xPathFactory.newXPath();
    return xPath.compile(expression);
  }

  private static IMultiproperties parseVersion_1_0(Path file) throws JAXBException {
    final var context = JAXBContext.newInstance( //
        com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_0.MultiProperties.class //
    );
    final var unmarshaller = context.createUnmarshaller();
    final var parsed = (com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_0.MultiProperties)unmarshaller.unmarshal(file.toFile());
    return new Multiproperties_1_0(parsed);
  }

  private static IMultiproperties parseVersion_1_1(Path file) throws JAXBException {
    final var context = JAXBContext.newInstance( //
        com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_1.MultiProperties.class //
    );
    final var unmarshaller = context.createUnmarshaller();
    final var parsed = (com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_1.MultiProperties)unmarshaller.unmarshal(file.toFile());
    return new Multiproperties_1_1(parsed);
  }

  private static IMultiproperties parseVersion_1_2(Path file) throws JAXBException {
    final var context = JAXBContext.newInstance( //
        com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_2.MultiProperties.class //
    );
    final var unmarshaller = context.createUnmarshaller();
    final var parsed = (com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_2.MultiProperties)unmarshaller.unmarshal(file.toFile());
    return new Multiproperties_1_2(parsed);
  }

  private static String readVersion(Path file) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
    final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
    final var builder = documentBuilderFactory.newDocumentBuilder();
    final var document = builder.parse(file.toFile());
    final var version = compileXPath("MultiProperties/Version/text()").evaluate(document);
    return defaultIfEmpty(version ,"");
  }
}
