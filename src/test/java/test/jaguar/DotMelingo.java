package test.jaguar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DotMelingo {

	@Test
	void testDotRemoved() {
		String test1=".בית ספר לקרימינולוגיה";
		String test1Exp="בית ספר לקרימינולוגיה";
		String test=removeDotToken(test1, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test1Exp, test);
		
		String test2=".ת.נ.צ.ב.ה.";
		String test2Exp="ת.נ.צ.ב.ה";
		test=removeDotToken(test2, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test2Exp, test);
		
		
		String test3="לקרימינולוגיה.";
		String test3Exp="לקרימינולוגיה";
		test=removeDotToken(test3, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test3Exp, test);

		String test4="dotPlaceHolderבית ספר לקרימינולוגיה.";
		String test4Exp="בית ספר לקרימינולוגיה";
		test=removeDotToken(test4, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test4Exp, test);
		
		String test5=".בית ספר לקרימינולוגיה.";
		String test5Exp="בית ספר לקרימינולוגיה";
		test=removeDotToken(test5, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test5Exp, test);
		
		String test6="dotPlaceHolderבית ספר לקרימינולוגיה";
		String test6Exp="בית ספר לקרימינולוגיה";
		test=removeDotToken(test6, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test6Exp, test);
		
		String test7="..";
		String test7Exp="";
		test=removeDotToken(test7, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test7Exp, test);
		
		String test8=".";
		String test8Exp="";
		test=removeDotToken(test8, false);
		System.out.println("T: "+test);
		test=removeDotToken(test, true);
		System.out.println("T: "+test);
		assertEquals(test8Exp, test);
		

	}
	
    private static final String DOT_PLACEHOLDER = "dotPlaceHolder";
    private static final String DOT =  ".";
    
    public String removeDotToken(String term, boolean removePlaceHolder) {

        term = removeTrunkByString(term, DOT_PLACEHOLDER);
        term = removeTrunkByString(term, DOT);
        if (term != null) {
            if (removePlaceHolder) {
                term = term.replace(DOT_PLACEHOLDER, DOT);
            } else {
                term = term.replace(DOT , DOT_PLACEHOLDER);
            }
        }

        return term;
    }
    
    /**
     * Trunk term if it start or end rem string
     * @param term
     * @param rem
     * @return trunked string
     */
    public static String removeTrunkByString(String term, String rem) {
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
    
}
