

package com.exlibris.jaguar.infra.utils.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;
import java.math.BigInteger;
import java.nio.charset.Charset;


/**
 * put your documentation comment here
 */
public class StringUtils {

public static final int MAX_VARCHAR_FIELD_LENGTH = 4000;

private static Pattern PUNCTUATION = Pattern.compile("\\p{Punct}");

private static final String HTML_URL_PATTERN = 	"(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";	
private static final String HTML_HREF_TAG_PATTERN = 	"\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";	
private static final String HTML_SRC_TAG_PATTERN = 	"\\s*(?i)src\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";	
private static final String HTML_URL_PREFIX = "(https?://www\\.|https?://|www\\.)";

public final static Set<Character> HYPHENS = new HashSet<Character>();

//Additional analog list exists in: com.exlibris.jaguar.search.RegexPatterns and in /jaguar-analysis/src/main/resources/JaguarStandardTokenizerImpl.jflex
static {
	HYPHENS.add(Character.valueOf('\u002d'));
	HYPHENS.add(Character.valueOf('\u2010'));
	HYPHENS.add(Character.valueOf('\u2011'));
	HYPHENS.add(Character.valueOf('\u05be'));
}
private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

/**
 * Convert string to hex string
 * @param arg
 * @return numbers in hex
 */
public static String toHex(String arg) {
	if(arg!=null) {
		return String.format("%x", new BigInteger(1, arg.getBytes(UTF8_CHARSET)));
	}else {
		return "";
	}
}


  /** remove all <> tags from a string.
   */
  public static String replaceTags(String str, String repl) {
    String regex = "<[^>]*>";
    return str.replaceAll(regex, repl);
  }

  /** remove blanks from a string.
   */
  public static String remove_blanks (String str) {
    StringBuffer buf = new StringBuffer(str.length());
    for (int i = 0; i < str.length(); i++)
      if (str.charAt(i) != ' ')
        buf.append(str.charAt(i));
    return  buf.toString();
  }

  /** pack blanks from a string.
   */
  public static String pack_blanks (String str) {
    StringBuffer buf = new StringBuffer(str.length());
    for (int i = 0; i < str.length()-1; i++)
      if (!(str.charAt(i) == ' ' && str.charAt(i+1) == ' '))
        buf.append(str.charAt(i));
    return  buf.toString().trim();
  }

  /** return suffix of str, from and including last c; empty string if not found.
   */
  public static String suffix (String str, char c) {
    int i = str.lastIndexOf(c);
    if (i < 0)
      return  "";
    else
      return  str.substring(i);
  }

  /** return base of str, up to and not including last c; everything if no c found.
   */
  public static String base (String str, char c) {
    int i = str.lastIndexOf(c);
    if (i < 0)
      return  str;
    else
      return  str.substring(0, i);
  }

  /**
   * put your documentation comment here
   * @param Value
   * @return
   */
  public static boolean isEmptyString (String Value) {
    if (Value == null) {
      return  true;
    }
    else {
      char[] chars = Value.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        if (chars[i] > 0 && !Character.isSpaceChar(chars[i]))
          return  false;
      }
      return  true;
    }
  }

  /**
   *
   * @param value1
   * @param value2
   * @return
   */
  public static boolean isEqual(String value1, String value2) {
    if (value1 == null) {
      return value2 == null;
    }
    if (value2 == null) {
      return false;
    }
    return value1.equals(value2);
  }

  /**
   * Replace substrings of one string with another string and return altered string.
   *
   * @param original input string
   * @param oldString the substring section to replace
   * @param newString the new substring replacing old substring section
   * @return converted string
   */
  public static String replaceSubString (final String original, final String oldString,
      final String newString) {
    final StringBuffer sb = new StringBuffer();
    int end = original.indexOf(oldString);
    int start = 0;
    final int stringSize = oldString.length();
    while (end != -1) {
      sb.append(original.substring(start, end));
      sb.append(newString);
      start = end + stringSize;
      end = original.indexOf(oldString, start);
    }
    end = original.length();
    sb.append(original.substring(start, end));
    return  sb.toString();
  }

  /**
   * put your documentation comment here
   * @param s
   * @return
   */
  public static boolean isDigitsString (String s) {
    if (s == null || s.length() == 0) {
      return  false;
    }
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isDigit(s.charAt(i))) {
        return  false;
      }
    }
    return  true;
  }

  /**
   * Convert each non alpanumeric char into the underscore
   * @param str
   * @return
   */
  static public String toAlphaNumeric(String str) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (Character.isLetterOrDigit(c)) {
        buf.append(c);
      } else {
        buf.append('_');
      }
    }
    return buf.toString();
  }

  /**
   * extract the parameter in the specified string. The format of the string
   * should be key1=value
   * @param str
   * @param result
   */
  static public void extractParameter(String str, String[] result) {
    // extract key and value
    int pos = str.indexOf("=");
    if (pos == -1) {
      return;
    }
    result[0] = str.substring(0, pos);
    result[1] = str.substring(pos+1);
  }

  /**
   * extract the values in the specified string. The format of the string
   * should be value1;value2;...
   * @param str
   * @param delim
   * @return  valuesVector
   */
  static public Vector<String> extractValues(String str, String delim) {
    Vector<String> valuesVector = new Vector<>();
    if (str == null) {
      return valuesVector;
    }
    // create string tokenizer
    StringTokenizer tokenizer = new StringTokenizer(str, delim);
    String value = null;
    while (tokenizer.hasMoreTokens()) {
      // get next token
      value = tokenizer.nextToken();
      // add to vector
      valuesVector.add(value);
    }
    return valuesVector;
  }

  /**
   *
   * @param paramater
   * @return
   */
  static public String makePathParameter(String paramater) {
    return "\"" + paramater + "\"";
  }

  /**
   * @param params
   * @return
   */
  static public String getParamsAsString (String[] params) {
    return  getParamsAsString(params, " ");
  }

  /**
   * @param params
   * @return
   */
  static public String getParamsAsString (String[] params, String delim) {
    StringBuffer buf = new StringBuffer(100);
    for (int i = 0; i < params.length; i++) {
      if (i > 0) {
        buf.append(delim);
      }
      buf.append(params[i]);
    }
    return  buf.toString();
  }

  /**
   * @param params
   * @return
   */
  static public String getParamsAsString (Set params, String delim) {
    StringBuffer buf = new StringBuffer(100);
    Iterator itr = params.iterator();
    int i = 0;
    while (itr.hasNext()) {
      if (i > 0) {
        buf.append(delim);
      }
      buf.append(itr.next());
      i++;
    }
    return  buf.toString();
  }

  /**
   * @param params
   * @return
   */
  static public String getParamsAsString (Vector<String> params, String delim) {
    if (params == null) {
      return null;
    }
    String[] result = new String[params.size()];
    result = (String[])params.toArray(result);
    return  getParamsAsString(result, delim);
  }

  /**
   * return the class name of the given Class.
   */
  public static String getClassName (Class cls) {
    String className = cls.getName();
    int pos = className.lastIndexOf('.');
    if (pos == -1) {
      return className;
    }
    else {
      return className.substring(pos + 1);
    }
  }

  /**
   * Fills the given string with another string from its left or right. It does
   * it X number of times, where X is given as input.
   * @param str2Fill - String to fill.
   * @param fillWithWhat - String to add to the previous string from its left or right.
   * @param howManyTimes - Number of times 'fillWithWhat' should be added to 'str2Fill'.
   * @param shouldFillFromLeft - Indicates whether the 'fillWithWhat' should be added
   * from the left (=true) or right (=false).
   * @return the filled String.
   */
  public static String fillString(String str2Fill, String fillWithWhat, int howManyTimes, boolean shouldFillFromLeft) {

    for (int i=0 ; i < howManyTimes ; i++) {
      if (shouldFillFromLeft) {
        str2Fill = fillWithWhat + str2Fill;
      }
      else {
        str2Fill += fillWithWhat;
      }
    }
    return str2Fill;
  }

  public static String readToString(InputStream in){
    StringBuffer sb = new StringBuffer();
    try{
        BufferedReader inbr = new BufferedReader(new InputStreamReader(in));
        String line;
        while((line = inbr.readLine() )!=null){
          sb.append(line);
        }
        return sb.toString();
    }catch(IOException e){
        return null;
    }
  }

  /**
   * Converts a string taken from database to string that should be displayed to the user.
   * Assumption: if database string contains several words, these words are seperated
   * according to the given WordSeparator rule.
   * Example: input: "file_definition_type", WordSeparator.BY_SPACE_OR_UNDERSCORE
   *          output: "File Definition Type".
   * @param str2Convert - string to convert.
   * @param separateByWhat - the rule accordint to which the words are separated.
   * @return the converted string.
   */
  public static String convertDBStr2GUIStr(String str2Convert, WordSeparator separateByWhat) {

    if (str2Convert == null) {
      return null;
    }
    if (separateByWhat == null) {
      return str2Convert;
    }

    String toReturn = null;
    if (separateByWhat == WordSeparator.BY_SPACE_OR_UNDERSCORE) {
      toReturn = str2Convert.replace('_', ' ');
      toReturn = toReturn.toUpperCase().charAt(0) + toReturn.substring(1, toReturn.length());

      for (int i=0 ; i < toReturn.length() ; i++) {
        if (toReturn.charAt(i) == ' ') {
          if (i < toReturn.length()-2) {
            toReturn = toReturn.substring(0, i+1) + toReturn.toUpperCase().charAt(i+1) + toReturn.substring(i+2, toReturn.length());
          }
        }
      }
    }
    else if (separateByWhat == WordSeparator.BY_CAPITAL_LETTER) {
      toReturn = str2Convert;
      for (int i=1 /* Start from the 2nd character */ ; i < toReturn.length() ; i++) {
        if ((toReturn.charAt(i) >= 'A') && (toReturn.charAt(i) <= 'Z')) {
          toReturn = toReturn.substring(0, i) + ' ' + toReturn.substring(i);
          i++; // Since 'toReturn' is now 1 character longer
        }
      }
    }
    return toReturn;
  }

  public static String removeAllCharOccurrences(char char2Remove, String str2RemoveFrom) {

    if (str2RemoveFrom == null) {
      return null;
    }

    String toReturn = "";
    for (int i=0 ; i < str2RemoveFrom.length() ; i++)  {
      if (str2RemoveFrom.charAt(i) != char2Remove) {
        toReturn += str2RemoveFrom.charAt(i);
      }
    }
    return toReturn;
  }

  /**
   * returns an array containing the values in <i>valueToSplit</i> which were separated by <i>delimiter</i>
   * values can be enclosed in quotes and then can contain <i>delimiter</i>.
   * @param valueToSplit
   * @param delimiter
   * @return array of string values
   */
  public static String[] splitValueDelimited(String valueToSplit, String delimiter) {
    //regex pattern is a slight modification to a pattern i found by Tom Archer from the book 'Extending MFC Applications with the .NET Framework'.(Ben)
    String pattern = delimiter+"(?=(?:[^\"]*\"[^\"]*\")*(?![^\"]*\"))";
    String[] res = valueToSplit.split(pattern);
    return res;
  }

  /**
   * Convinience method for parsing comma separated values.
   * Uses <i>splitValueDelimited</i> with "," as a delimiter.
   * Returns the values without the first enclosing quotes, changes double quotes to a single one and trimmed.
   * This format is best suitable for standart CSV as used by Excel.
	 * Fields are enclosed in quotes if they contain commas or quotes.
	 * Embedded quotes are doubled.
	 * The last field on the line is not followed by a comma.
   * @param line
   * @return List of the values
   */
  public static List<String> splitCSV(String line) {
    List<String> lst = new ArrayList<>();
    if(line==null || line.trim().length()==0) {
      return lst;
    }
    String[] res = splitValueDelimited(line, ",");
    String frmtd;
    for (int i = 0; i < res.length; i++) {
      frmtd = res[i].trim();
      if(frmtd.startsWith("\"")) {
        frmtd = frmtd.replaceFirst("\"", "");
        if(frmtd.endsWith("\"")) {
          frmtd = frmtd.substring(0,frmtd.length()-1);
        }
      }
      frmtd = frmtd.replaceAll("\"\"", "\"");
      lst.add(frmtd.trim());
    }
    return lst;
  }

  /**
   * 24bit hashing, hopefully find a 30bit (string hashcode is 32, too much need spare 2 bits for prefixes)
   * @param string
   * @return
   * @throws UnsupportedEncodingException
   */
  public static int hashCode(String string) throws UnsupportedEncodingException{
      char []str = string.toCharArray();
      int crc = 0xb704ce;
      for (int j = 0; j < str.length; j++) {
        char c = str[j];
        crc^=c<<16;
        for (int i = 0; i < 8; i++){
          crc<<=1;
          if((crc&0x1000000)>0)
            crc^=0x1864cfb;
        }
      }
      return (crc&0xffffff);

  }

  public static String safeClobToString(Clob aClob) throws SQLException {

	  //Get the clob field we are interested in from the result set
	  Clob clob = aClob;

	  if (clob == null)
	    return null;

	  String unicodeClob = clob.getSubString(1, (int) clob.length());

	  try {
	    byte[] utf8Bytes = unicodeClob.getBytes("UTF-8");

	    String utf8Clob = new String(utf8Bytes,"UTF-8");

	    return utf8Clob;

	  } catch (UnsupportedEncodingException ex) {
	    throw new SQLException(ex.getMessage());
	  }
  }

  /*
   * Backslash regular expression special characters to have them treated as literals
   */
  public static String escapeRESpecialCharacters(String value) {
	  StringBuffer sb = new StringBuffer();
	  if (value != null) {
	  for (int i = 0; i < value.length(); i++) {
		  char c = value.charAt(i);
	      if (c == '.' || c == '*' || c == '+' || c == '?' || c == '['
	          || c == ']' || c == '-' || c == '^' || c == '\"' || c == '$'
	          || c == '{' || c == '}' || c == '\\' || c == '|' || c == '&'
	          || c == '(' || c == ')') {
	    	  sb.append('\\');
	       }
	        sb.append(c);
	      }
	      value = sb.toString().trim();
	    }
	  return value;
  }

  public static String[] split(String string, String separator) {
	  List<String > result = new ArrayList<String>();
	  final StringTokenizer st = new StringTokenizer(string, separator, true);
	  String token = null;
	  String lastToken = separator; //if first token is separator
	  while (st.hasMoreTokens()) {
		  token = st.nextToken();
		  if (token.equals(separator)) {
			  if (lastToken.equals(separator)) { //no value between 2 separators?
				  result.add("");
			  }
		  } else {
			  result.add(token);
		  }
		  lastToken = token;
	  }//next token
	  return result.toArray(new String[] {});
  }



	public static String join(String[] arr, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
        	buffer.append(arr[i]);
        	if (i != arr.length - 1) {
        		buffer.append(delimiter);
        	}
        }
        return buffer.toString();
    }

	public static String normSearchQuery(String str){
		return str.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\"","").trim().toLowerCase();
	}

	/**
	* ****************************************
	* Hyphen functions
	*
	*/


	public static boolean isHyphen(char c){
		for (Character ch : HYPHENS){
			if (ch == c){
				return true;
			}
		}
		return false;
	}

	public static boolean containsHyphen(String str){
		int size = str.length();
		for (int i = 0; i < size; i++) {
			if (isHyphen(str.charAt(i))){
				return true;
			}
		}
		return false;
	}

	public static String removeHyphens(String str,boolean removeHyphens){
		//if no hyphens - return original string
		if (!containsHyphen(str)){
			return str;
		}
		String[] splittedStr = str.split("\\s+");

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < splittedStr.length; i++) {
			String part = splittedStr[i];
			if (containsDigit(part) || removeHyphens){
				//we always want to remove hyphens from strings with digits,
				//or if we remove hyphens by a setting
				part = _removeHyphens(part);
			}
			//else we do nothing - in the case that there was no digit, and we don't remove hyphens
			buf.append(part).append(" ");
		}
		//removing last space and returning the new value
		return (buf.deleteCharAt(buf.length()-1).toString());
	}

	public static boolean containsDigit(String str){
		int size = str.length();
		for (int i = 0; i < size; i++) {
			if (Character.isDigit((str.charAt(i)))){
				return true;
			}
		}
		return false;
	}

	private static String _removeHyphens(String str){
		//if no hyphens - return original string
		if (!containsHyphen(str)){
			return str;
		}
		int size = str.length();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < size; i++) {
			char c = str.charAt(i);
			if (!isHyphen(c)){
				buf.append(c);
			} else {
				buf.append(' ');
			}
		}
		return buf.toString();
	}

	/**
	* end - hyphen functions
	*
	*/



  public static class WordSeparator {

    public static WordSeparator BY_SPACE_OR_UNDERSCORE  = new WordSeparator("Space or underscore");
    public static WordSeparator BY_CAPITAL_LETTER       = new WordSeparator("Capital letter");

    private String description;

    private WordSeparator(String description) {
      this.description = description;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
      return description;
    }
  }

  public static boolean isNumeric(String str) {
      if (str == null) {
          return false;
      }
      int sz = str.length();
      for (int i = 0; i < sz; i++) {
          if (Character.isDigit(str.charAt(i)) == false) {
              return false;
          }
      }
      return true;
  }

	/**
	 * Splits th
	 * @param prefixName
	 * @param prefixEndIndex
	 * @return
	 */
	public static String[] splitByindex(final String prefixName, int prefixEndIndex) {
		String[] splitAuthor = new String[2];
		final String prefix = prefixName.substring(0, prefixEndIndex);
		final String onlyName = prefixName.substring(prefixEndIndex, prefixName.length());

		splitAuthor[0]=prefix;
		splitAuthor[1]=onlyName;
		return splitAuthor;
	}

	public static String splitParamsForSQL(Collection<String> params) {

		String s = org.apache.commons.lang.StringUtils.join(params.iterator(), "','");

		return "'" + s + "'";

		}

	/**
	 * @param v
	 * @return
	 */
	public static String vectorToString(Vector v) {
		StringBuilder sb = new StringBuilder();
		int length = v.size();
		for (int i = 0; i < length; i++) {
			sb.append(v.get(i) + " ");
		}
		String retString = sb.toString().trim();
		return retString;
	}

	/**
	 * @param str
	 * @return
	 */
	public static Vector<String> stringToVector(String str) {
		Vector<String> v = new Vector();
		v.addAll(Arrays.asList(str.split("\\s+")));
		return v;
	}

	public static String getFirstWord(String text) {
	    if (text.indexOf(' ') > -1) { // Check if there is more than one word.
	      return text.substring(0, text.indexOf(' ')); // Extract first word.
	    } else {
	      return text; // Text is the first word itself.
	    }
	  }

	public static String normalizeString(String str) {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(str)) {
			str = str.toLowerCase().trim();
			str = PUNCTUATION.matcher(str).replaceAll("");
		}
		return str;
	}

	public static String truncate(String value, int limit) {
		if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
			return value;
		}

		int length = value.length();

		if (length < limit) {
			return value;
		}

		value = value.substring(0, limit);

		int indexOfSace = value.lastIndexOf(' ');

		if (indexOfSace > -1) {
			value = value.substring(0, indexOfSace);
		}

		return value;
	}

    /**
     * Trunk term if it start or end rem string
     * @param term
     * @param rem
     * @return trunked string
     */
    public static Reader trunkByString(Reader term, String rem) throws IOException{
        String targetString = "";
        int intValueOfChar;
        while ((intValueOfChar = term.read()) != -1) {
            targetString += (char) intValueOfChar;
        }
        term.close();
        
        targetString=leftTrunkByString(targetString,rem);
        targetString=rightTrunkByString(targetString,rem);

    	return new StringReader(targetString);
    }
    /**
     * Trunk term if it start or end rem string
     * @param term
     * @param rem
     * @return trunked string
     */
    public static String trunkByString(String term, String rem) {
    	term=leftTrunkByString(term,rem);
    	term=rightTrunkByString(term,rem);
    	
    	return term;
    }
    /**
     * Trunk term if it end rem string
     * @param term
     * @param rem
     * @return trunked string
     */
    public static String leftTrunkByString(String term, String rem) {
    	if(term.endsWith(rem)) {
    		term=term.substring(0, term.length()-rem.length());
    	}
    	return term;
    	
    }
    /**
     * Trunk term if it start rem string
     * @param term
     * @param rem
     * @return trunked string
     */
    public static String rightTrunkByString(String term, String rem) {
    	if(term.startsWith(rem)) {
    		term=term.substring(rem.length(), term.length());
    	}
    	return term;
    	
    }
	
	
	public static String removeFirstAndLastCharacters(String value)
	{
		return value.substring(1,value.length()-1); 
	}

	/**
	 * converts a Throwable stack trace to a string.
	 * @param aThrowable
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}
	
	
	/**
	 * Find and clean all urls from text
	 * @param targetText
	 * @param exceptUrl
	 * @return
	 */
	public static String cleanURL(String targetText, String replaceBy) {
		replaceBy = (isEmptyString(replaceBy))?"#":replaceBy;
		
	    String regex = HTML_URL_PATTERN;

	    String nText = targetText.replaceAll(regex, replaceBy);
		
		return nText;
	}
	/**
	 * Find and clean all href from text
	 * @param targetText
	 * @param exceptUrl
	 * @return
	 */
	public static String cleanHREF(String targetText, String replaceBy) {
		replaceBy = (isEmptyString(replaceBy))?"href='#'":replaceBy;
		
		String regex = HTML_HREF_TAG_PATTERN;
		
		String nText = targetText.replaceAll(regex, replaceBy);
		
		return nText;
	}
	/**
	 * Find and clean all src from text
	 * @param targetText
	 * @param exceptUrl
	 * @return
	 */
	public static String cleanSRC(String targetText, String replaceBy) {
		replaceBy = (isEmptyString(replaceBy))?"src=''":replaceBy;
		
		String regex = HTML_SRC_TAG_PATTERN;
		
		String nText = targetText.replaceAll(regex, replaceBy);
		
		return nText;
	}
	
	/**
	 * Find and clean all url prefix from text
	 * @param targetText
	 * @param exceptUrl
	 * @return
	 */
	public static String cleanUrlPrefix(String targetText, String replaceBy) {
		replaceBy = (isEmptyString(replaceBy))? "" : replaceBy;
		
		String regex = HTML_URL_PREFIX;
		
		String nText = targetText.replaceAll(regex, replaceBy);
		
		return nText;
	}
}
