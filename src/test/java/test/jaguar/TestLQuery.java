package test.jaguar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.exlibris.jaguar.agent.search.queryParser.LQuery;

class TestLQuery {

	@Test
	void test() {
		String exp = "(((war) AND title:(\"peace\")  NOT (\"Black mamaba\" )) OR ( swstitle:(\"green*\") NOT sub:(sharm) AND usertag:(barmaglot vs alice ) AND (fuck vs tedy bear)) OR (number))";
		System.out.println("Orig Exp:"+exp);
		LQuery lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(((Green peace ) NOT (\"Black mamaba\" )) OR (sub:(Water flaw cintetic ) AND title:(Fingipul )))";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(war)";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(Green peace vs mamba)";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();

		exp = "(((日 報 ) NOT (民 )) OR (sub:(人 ) AND title:(報 )))";
		System.out.println("Orig Exp:"+exp);
		lq = new LQuery(exp);
		lq.print();
		lq.printTerms();
		lq.changeExpAndAddAsOr((s,l)->(s.toUpperCase()), "ENG");
		lq.print();
	}

}
