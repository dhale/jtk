/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;

import java.io.CharArrayWriter;
import java.io.Reader;
import java.util.ArrayList;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses a ParameterSet from an XML-formatted character reader.
 *
 * @see edu.mines.jtk.util.ParameterSetFormatException
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 07/10/2000, 08/24/2006.
 */
class ParameterSetParser extends DefaultHandler {

  public ParameterSetParser() {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      _parser = factory.newSAXParser();
    } catch(ParserConfigurationException e) {
      throw new RuntimeException("cannot create XML parser: "+e);
    } catch(SAXException e) {
      throw new RuntimeException("cannot create XML parser: "+e);
    }
  }

  public void parse(Reader reader, ParameterSet ps) 
    throws ParameterSetFormatException 
  {
    _ps_root = ps;
    _ps = null;
    _p = null;
    _ptype = Parameter.NULL;
    _pdata.reset();
    try {
      _parser.parse(new InputSource(reader),this);
    } catch (ParameterSetFormatException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace(System.err);
      throw new ParameterSetFormatException(
        "ParameterSetParser.parse: unknown error: " +
        e.getMessage()+":"+e.toString());
    }
  }


  ///////////////////////////////////////////////////////////////////////////
  // HandlerBase.
  public void startElement(String uri, String localName, String qName, 
      Attributes attributes) 
    throws ParameterSetFormatException 
  {
    if (qName.equals("parset")) {
      startParameterSet(attributes);
    } else if (qName.equals("par")) {
      startParameter(attributes);
    } else {
      throw new ParameterSetFormatException(
        "ParameterSetParser.startElement: unrecognized XML element \"" +
        qName+"\""+".");
    }
  }

  public void endElement(String uri, String localName, String qName)
    throws ParameterSetFormatException 
  {
    if (qName.equals("parset")) {
      endParameterSet();
    } else if (qName.equals("par")) {
      endParameter();
    } else {
      throw new ParameterSetFormatException(
        "ParameterSetParser.endElement: unrecognized XML element \"" +
        qName+"\""+".");
    }
  }

  public void characters(char ch[], int start, int length) {
    if (_p==null) return;
    _pdata.write(ch,start,length);
  }

  public void error(SAXParseException e)
    throws ParameterSetFormatException 
  {
    String message = e.getMessage();
    int line = e.getLineNumber();
    int column = e.getColumnNumber();
    throw new ParameterSetFormatException(
      "ParameterSetParser.error: "+message +
      " at line "+line+", column "+column+"."); 
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private SAXParser _parser = null; // parser that calls us back
  private ParameterSet _ps_root = null; // parset used to initialize parser
  private ParameterSet _ps = null; // current parset
  private Parameter _p = null; // current par
  private int _ptype = Parameter.NULL; // current par type
  private CharArrayWriter _pdata = new CharArrayWriter(256); // par data values

  private void startParameter(Attributes attributes)
    throws ParameterSetFormatException {
    String name = attributes.getValue("name");
    if (name==null)
      throw new ParameterSetFormatException("parameter with no name");
    if (_ps==null)
      throw new ParameterSetFormatException(
        "<par name=\""+name+"\" ...>" +
        " must be specified inside a parameter set.");
    _p = _ps.addParameter(name);
    String units = attributes.getValue("units");
    _p.setUnits(units);
    String type = attributes.getValue("type");
    if (type!=null) {
      if (type.equals("boolean")) {
        _ptype = Parameter.BOOLEAN;
      } else if (type.equals("int")) {
        _ptype = Parameter.INT;
      } else if (type.equals("long")) {
        _ptype = Parameter.LONG;
      } else if (type.equals("float")) {
        _ptype = Parameter.FLOAT;
      } else if (type.equals("double")) {
        _ptype = Parameter.DOUBLE;
      } else if (type.equals("string")) {
        _ptype = Parameter.STRING;
      } else {
        _ptype = Parameter.STRING;
      }
    } else {
      _ptype = Parameter.STRING;
    }
  }

  private void endParameter() {
    _p.setStrings(parseValues());
    _p.setType(_ptype);
    _p = null;
    _ptype = Parameter.NULL;
    _pdata.reset();
  }

  private void startParameterSet(Attributes attributes) {
    String name = attributes.getValue("name");
    if (name==null)
      throw new ParameterSetFormatException("parameter set with no name");
    if (_ps==null) {
      _ps_root.setName(name);
      _ps = _ps_root;
    } else {
      _ps = _ps.addParameterSet(name);
    }
  }

  private void endParameterSet() {
    if (_ps!=_ps_root) {
      _ps = _ps.getParent();
    }
  }

  private String[] parseValues() {
    StringParser sp = new StringParser(_pdata.toString());
    ArrayList<String> vtemp = new ArrayList<String>(8);
    while (sp.hasMoreStrings())
      vtemp.add(sp.nextString());
    String[] values = new String[vtemp.size()];
    for (int i=0; i<values.length; ++i)
      values[i] = vtemp.get(i);
    return values;
  }
}
