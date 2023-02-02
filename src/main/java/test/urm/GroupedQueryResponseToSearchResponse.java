package test.urm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GroupedQueryResponseToSearchResponse {

    private static final String DISCOVERY_PREFERRED_FRBR_MT = "DiscoveryPreferredFrbr";
    private static final String PRIMO_VE_ENABLE_UNIQUE_COUNT_USING_JSON_FACETS_TMP_GP = "tmp_discovery_count_grouped_results_using_json_facet_query";
    private static final String COMMA = ",";
    private static final String USE_FACET_GROUPING_SOLR_PATCH = "use_facet_grouping_solr_patch";
    private static final String FACET_COUNT_DISTINCT = "facet_count_distinct";
    public static final String PRIMO_VE_GROUPED_RESPONSE_COUNT = "discovery_grouped_count";
    private static String punctuationList = "!#$%&'\"()*+,./:;<=>?@[\\]^_`{|}~" + "\u002d\u2010\u2011\u05be";
    public static final Pattern p = Pattern.compile("\\s+");
    private static final String FACET_LIMIT_CUSTOMER_PARAM = "discovery_facet_limit";
    private static final String FACET_KEY = "(?<=(prima_facet_|prima_unnormalized_facet_)).*";
    public static final String NEW_RECORDS_FACET_INTERVAL_FIELD_NAME = "new_records_facet";
    public static final String UNNORMALIZED_FACETS_FOR_9XX_FIELD = "discovery_9XX_facet_indexing_without_normalization";
    private static Pattern LSR_PATTERN = Pattern.compile("(lsr_\\d{2,3})");



    public static String getStringValue(Object str) {
        String strValue = String.valueOf(str);
        if (strValue == null || strValue.equals("null")) {
            return null;
        }
        return strValue;
    }

    public static Double getDoubleValue(Object str) {
        String strValue = String.valueOf(str);
        if (strValue == null || strValue.equals("null")) {
            return null;
        }
        return Double.valueOf(strValue);
    }

    public static Long getLongValue(Object longVal) {
        try {
            return (longVal != null) ? (Long) longVal : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar getCalendarValue(Object obj) {
        try {
            if (obj != null) {
                Date date = (Date) obj;
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTime());
                return cal;
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve Solr date " + obj);
        }
        return null;
    }


    private static Pair<Boolean, String[]> checkIfNeedToSwapLsrAndLocalDCOrderInArray(String actualIndexFields) {
        String[] actualIndexFieldsArr = actualIndexFields.split(",");
        Pair<Boolean, String[]> pairReturnValue = new Pair<>();
        if (actualIndexFieldsArr.length > 2) {
            if (actualIndexFields.contains("lsr_") && actualIndexFields.contains("local_fields_dc")) {
                if (actualIndexFieldsArr[0].startsWith("local_fields_dc")
                        && actualIndexFieldsArr[1].startsWith("lsr_")) {
                    // need to merge similar values if their
                    // normalized form is equal, the lsr must be
                    // first since it is indexed unnormalized
                    // and the desire is that it's value will be the
                    // representative (the first in line is the
                    // representative), so in case the order is
                    // other way around -> swap
                    String helpStr = actualIndexFieldsArr[1];
                    actualIndexFieldsArr[1] = actualIndexFieldsArr[0];
                    actualIndexFieldsArr[0] = helpStr;

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

    public static String normalizeFacetValueVal(String facetValueVal) {
        String normedVal = facetValueVal.toLowerCase();
        normedVal = removePunctuation(normedVal, ' ');
        normedVal = removePunctuation(normedVal, Character.MIN_VALUE);
        normedVal = p.matcher(normedVal).replaceAll(" ");
        normedVal = normedVal.trim();
        return normedVal;
    }

    private static boolean isPunctuation(char c, int i) {
        if (punctuationList.indexOf(c) >= 0) {
            return true;
        }
        return false;
    }

    private static String removePunctuation(String value, char replacement) {
        char[] input = value.toCharArray();
        int len = input.length;
        char[] output = new char[len];
        int outputPos = 0;
        for (int i = 0; i < len; i++) {
            if (!isPunctuation(input[i], i)) {
                output[outputPos++] = input[i];
            } else {
                if (replacement == Character.MIN_VALUE) {
                    continue;
                }
                output[outputPos++] = ' ';
            }
        }
        return new String(output);
    }



}
