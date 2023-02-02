package test.jaguar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CleanDotFromQuery {

	@Test
	void test() {
		String test="(((((??????.)  ( .???))  ( .?????????????.))))";
		String exp = "(((((??????)  ( ???))  ( ?????????????))))";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	@Test
	void test2() {
		String test="((.בית.))";
		String exp = "((בית))";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}

	@Test
	void test3() {
		String test=".בית.";
		String exp = "בית";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test4() {
		String test="בית.";
		String exp = "בית";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}

	@Test
	void test5() {
		String test="(בית.)";
		String exp = "(בית)";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test6() {
		String test="(בית)";
		String exp = "(בית)";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	
	@Test
	void test7() {
		String test="(בי.ת)";
		String exp = "(בי.ת)";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test8() {
		String test="(((((????.??.)  ( .?.??))  ( .??.???.???????.?.))))";
		String exp = "(((((????.??)  ( ?.??))  ( ??.???.???????.?))))";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test9() {
		String test="בית";
		String exp = "בית";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test10() {
		String test="";
		String exp = "";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test11() {
		String test="א";
		String exp = "א";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test12() {
		String test=".";
		String exp = "";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	@Test
	void test13() {
		String test="..";
		String exp = "";
		String res=trunkDOTInQuery(test);
		assertEquals(exp, res);
	}
	
	
	/**
	 * Clean query from DOT in begin or end of terms
	 * @param term
	 * @return query without . in start or end of terms
	 */
	public static String trunkDOTInQuery(String term) {
		if(term.length()<1) return term;
		StringBuilder newTerm=new StringBuilder();
		int i=0;
		char cm=term.charAt(0);
		if(cm!='.') newTerm.append(cm);
		if(term.length()<2) return newTerm.toString();
		for(i++;i<term.length()-1;i++) {
			cm=term.charAt(i-1);
			char c=term.charAt(i);
			char cp=term.charAt(i+1);
			if(c=='.' && (cm=='('||cm==' '||cp==')'||cp==' '||i+2>=term.length())) {
				
			}else {
				newTerm.append(c);
			}
			
		}
		char c=term.charAt(i);
		if(c!='.') newTerm.append(c);
		return newTerm.toString();
		
	}
		
}
