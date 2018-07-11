package com.template.common.bannedword;

import com.template.common.configuration.properties.ProjectData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterText {

    /**
     * 금칙어 최대 크기
     */
    public static final int FILTER_WORD_MAX_LENGTH = 10;
    /**
     * 금칙어 최소 크기
     */
    public static final int FILTER_WORD_MIN_LENGTH = 2;
    /**
     * 단어 구분자
     */
    public static final String DELIMITERS = " ,;/'\"\n\r.~!@#$%^&*()+|-=`[]";
    /**
     * 금칙어를 체크한다.
     * 입력된 단어 contents에 금칙어가 있는지 확인한다.
     * <p>
     * 1. 단어를 구분자를 기준으로 하여 세분화한다. (소문자로 변경하여 체크)
     * 2. 2개이상의 글자조합에 대한 모든 상황을 list에 저장한다.
     * 3. 금지어내에서 해당 단어가 금지어인지 확인한다. (HashMap 사용)
     * <p>
     * 2009/02/02 : 대소문자 구분없이 체크할 수 있도록 변경함 (tgkang)
     *
     * @param _filterWordList 금칙어 목록
     * @param _contents       입력된 글
     * @return
     * @Method : checkFilterWord
     * @Date : 2008. 08. 11
     * @Auther : taigeun.kang
     * @History :
     * @Comment :
     * @Locaton : checkFilterWord
     */
    public static String checkFilterWord4HashMap(HashMap _filterWordList, String _contents) {

        String content = null;
        HashMap<String, String> hash = new HashMap<String, String>();

        if (_contents == null) return null;
        content = _contents.toLowerCase();

        StringTokenizer st = new StringTokenizer(content, DELIMITERS, false);

        // 단어를 추출한다.
        while (st.hasMoreElements()) {
            String str = st.nextToken();
            int length = str.length();
            String substr;

            String tmpstr = hash.get(str);
            if (tmpstr != null) continue;

            for (int k = 0; k < length - FILTER_WORD_MIN_LENGTH + 1; k++) {
                for (int i = FILTER_WORD_MIN_LENGTH + k; i <= length && i < FILTER_WORD_MAX_LENGTH; i++) {
                    substr = str.substring(k, i);
                    if (_filterWordList.get(substr) != null)
                        return substr;
                }
            }

            hash.put(str, str);
        }

        return null;
    }

    /**
     * 금칙어를 체크한다.
     * 단어의 exact match 판단
     *
     * @param _filterWordList
     * @param _contents
     * @return
     * @Method : checkFilterWord
     * @Date : 2008. 08. 11
     * @Auther : taigeun.kang
     * @History :
     * @Comment :
     * @Locaton : checkFilterWord
     */
    public static String checkFilterWordExact4HashMap(HashMap _filterWordList, String _contents) {

        String content = null;
        HashMap<String, String> hash = new HashMap<String, String>();

        if (_contents == null) return null;
        content = _contents.toLowerCase();

        StringTokenizer st = new StringTokenizer(_contents, DELIMITERS, false);

        // 단어를 추출한다.
        while (st.hasMoreElements()) {
            String str = st.nextToken();

            if (str == null || "".equals(str))
                continue;

            if (_filterWordList.get(str) != null)
                return str;
        }

        return null;
    }

    public static String filterText(String bannedWordList,String sText) {
        //Pattern p = Pattern.compile("fuck|shit|개새끼|시발|존나", Pattern.CASE_INSENSITIVE);
        Pattern p = Pattern.compile(bannedWordList, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sText);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, maskWord(m.group()));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String maskWord(String word) {
        StringBuffer buff = new StringBuffer();
        char[] ch = word.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (i < 1) {
                buff.append(ch[i]);
            } else {
                buff.append("*");
            }
        }
        return buff.toString();
    }

}