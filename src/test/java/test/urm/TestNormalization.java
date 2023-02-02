package test.urm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestNormalization {

	@Test
	void testNormalization() {
		String testTitle="s doble titulación grado en ingeniería agroalimentaria y del medio rural y grado en biotecnología 202";
		String compTitle="S-Doble titulación. grado en ingeniería agroalimentaria y del medio rural y grado en biotecnología 202";
		String ntestTitle = GroupedQueryResponseToSearchResponse.normalizeFacetValueVal(testTitle);
		String ncompTitle = GroupedQueryResponseToSearchResponse.normalizeFacetValueVal(compTitle);
		
		System.out.println(ntestTitle);
		System.out.println(ncompTitle);
		
		assertEquals(ntestTitle, ncompTitle );
	}

	@Test
	void testNormalization2() {
		String testTitle="s+doble     titulación grado en ingeniería agroalimentaria y del medio rural y grado en biotecnología 202";
		String compTitle="S-Doble titulación. grado en ingeniería agroalimentaria y del medio rural y grado en biotecnología 202";
		String ntestTitle = GroupedQueryResponseToSearchResponse.normalizeFacetValueVal(testTitle);
		String ncompTitle = GroupedQueryResponseToSearchResponse.normalizeFacetValueVal(compTitle);
		
		System.out.println(ntestTitle);
		System.out.println(ncompTitle);
		
		assertEquals(ntestTitle, ncompTitle );
	}
	
}
