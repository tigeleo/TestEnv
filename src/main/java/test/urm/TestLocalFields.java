package test.urm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


public class TestLocalFields {

    public static final String LOCAL_FIELD_9XX = "local_field_9";
    public static final String LOCAL_FIELD_LOCAL_EXTENSION_9XX = "local_field_lex_9";
    public static Pattern LOCAL_FIELD_NAME_PATTEN = Pattern.compile("(^.*?)(\\d+)");
    public static final String LOCAL_EXTENSION_IDENTIFIER = "lex_";
    public static Set<String> localExtensionPrefixesSet = new HashSet<String>() {

        {
            add("local_note_field");
            add("local_call_number");
            add("local_subject_dispaly");
            add("local_subjects_display");
            add("local_field");
        }
    };
    public static final String LSR_PREFIX = "lsr_";
    public static final String LDS = "lds";
    private static final String COMMA = ",";
	
    public static String discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(String localFields) {
        if (StringUtils.isEmpty(localFields)) {
            return localFields;
        }
        List<String> localFieldsList = new ArrayList<>();
        localFieldsList.addAll(Arrays.asList(localFields.split(",")));
        List<String> localFieldsWithLocalExtensionsList = new ArrayList<>();
        List<String> lsrFields = new ArrayList<>();
        for (String localField : localFieldsList) {
            Matcher m1 = LOCAL_FIELD_NAME_PATTEN.matcher(localField);
            if (m1.matches()) {
                String prefix = m1.group(1);
                String number = m1.group(2);
                if (!prefix.contains(LSR_PREFIX) && isLocalExtensionFieldPrefix(prefix)) {
                    String locExtensionField = prefix + LOCAL_EXTENSION_IDENTIFIER + number + "_"
                            + getInstitutionIdNoException();
                    localFieldsWithLocalExtensionsList.add(locExtensionField);
                } else if (prefix.contains(LSR_PREFIX)) {
                    String lsrFieldWithInstitutionSeffix = prefix + number + "_"
                            + getInstitutionIdNoException();
                    localFieldsWithLocalExtensionsList.add(lsrFieldWithInstitutionSeffix);
                    lsrFields.add(localField);
                }
            }

        }
        localFieldsList.removeAll(lsrFields);
        String localFieldsWithLocalExtensionsString = (String) CollectionUtils
                .union(localFieldsList, localFieldsWithLocalExtensionsList).stream().collect(Collectors.joining(","));
        return localFieldsWithLocalExtensionsString;
    }

    public static Long getInstitutionIdNoException() {
        try {
            return 100000L;
        } catch (Exception e) {
            return null;
        }
    }	
    
    public static boolean isLocalExtensionFieldPrefix(String prefix) {
        return localExtensionPrefixesSet.stream().anyMatch(prefix::contains);

    }	
	
    private static Pair<Boolean, String[]> checkIfNeedToSwapLsrAndLocalDCOrderInArray(String actualIndexFields) {
        String[] actualIndexFieldsArr = actualIndexFields.split(",");
        Pair<Boolean, String[]> pairReturnValue = new Pair<>();
        if (actualIndexFieldsArr.length > 2) {
            if (actualIndexFields.contains("lsr_") && actualIndexFields.contains("local_fields_dc")) {
                if (actualIndexFieldsArr[0].startsWith("local_fields_dc") && actualIndexFieldsArr[1].startsWith(LSR_PREFIX)) {
                    // need to merge similar values if their
                    // normalized form is equal, the lsr must be
                    // first since it is indexed unnormalized
                    // and the desire is that it's value will be the
                    // representative (the first in line is the
                    // representative), so in case the order is
                    // other way around -> swap
                    String helpStr = actualIndexFieldsArr[1];
                    actualIndexFieldsArr[1]=actualIndexFieldsArr[0];
                    actualIndexFieldsArr[0]=helpStr;

                } 
                pairReturnValue.setFirst(true);
                pairReturnValue.setSecond(actualIndexFieldsArr);
                return pairReturnValue;
            }
        }
        pairReturnValue.setFirst(false);
        pairReturnValue.setSecond(actualIndexFieldsArr);
        return pairReturnValue;
    }
    
	public static void main(String[] args) {
		String l="unimarc_local_note_fields_301,unimarc_local_note_fields_303,unimarc_local_note_fields_304,unimarc_local_note_fields_305,unimarc_local_note_fields_306,unimarc_local_note_fields_307,unimarc_local_note_fields_308,unimarc_local_note_fields_310,unimarc_local_note_fields_312,unimarc_local_note_fields_314,unimarc_local_note_fields_315,unimarc_local_note_fields_321,unimarc_local_note_fields_322,unimarc_local_note_fields_323,unimarc_local_note_fields_324";
		String s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		Pair<Boolean, String[]> p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));
		
		l="local_field_901,local_field_902,local_field_903,local_field_904,local_field_905,local_field_906";
		s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));
		
		l="local_fields_dc_004,lsr_04";
		s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));

		
		l="lsr_04";
		s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));
		
		l="local_note_field_526,local_fields_dc_004";
		s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));
		
		l="local_fields_dc_003,lsr_03";
		s=discoveryAddLocalExtensionsAndMakeLSRInstitutionAware(l);
		System.out.println("discoveryAddLocalExtensionsAndMakeLSRInstitutionAware:" + s);
		p=checkIfNeedToSwapLsrAndLocalDCOrderInArray(s);
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + p.getFirst());
		System.out.println("checkIfNeedToSwapLsrAndLocalDCOrderInArray:" + Arrays.toString(p.getSecond()));
		
		
	}

}
