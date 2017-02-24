package liamkengineering.led_matrix;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Liam on 2/10/2017.
 */

public class StringRecognizer {


    public static void main(String[] args) {
        StringRecognizer s = new StringRecognizer("lehtr a", "letter", true);
        String result = s.wordRecognizer();
        if(result == null) {
            System.out.println("failed");
        }
        else {
            System.out.println(result);
        }

    }
    private String main;
    private boolean let;
    private String match;

    /*
    main is the string that we're seeing analyzing. match is what we're looking to match it against.
    let is whether or not there is a letter at the end
     */
    public StringRecognizer(String main, String match, boolean let) {
        this.main = main.toLowerCase();
        this.let = let;
        this.match = match.toLowerCase();
    }

    /*
    this method checks to see if the main string passed to the ctor is similar to the match
    string passed to the ctor. Since it is checking for words, it removes any punctuation
    it returns the string that we're searching for if we think match and main are similar, null
    otherwise. Note that in the case of "Letter X", it will return X
     */
    public String wordRecognizer() {
        String letters = "";
        int searchLength = main.length();
        if (let) --searchLength; // don't consider the letter at the end if we're checking against
        // for a string with a letter at the end
        for (int i = 0; i < searchLength; ++i) {
            if (Character.isLetter(main.charAt(i)))
                letters += (main.charAt(i));
        }

        int totScore = 0; // we score the similarity out of a total determined by
        // the number of letters in the match

        int dif = match.length() - letters.length();
        int score = 0;
        Map<Character, Integer> letterCount = new HashMap<>();// if the one we're analyzing is shorter
        for (int i = 0; i < match.length(); ++i) {
            if (!letterCount.containsKey(match.charAt(i))) {
                letterCount.put(match.charAt(i), 1);
            } else {
                letterCount.put(match.charAt(i), letterCount.get(match.charAt(i)) + 1);
            }
        }
        for (int i = 0; i < letters.length(); ++i) {
            if (letterCount.containsKey(letters.charAt(i))) {
                if (letterCount.get(letters.charAt(i)) > 0) {
                    ++score;
                    letterCount.put(letters.charAt(i), letterCount.get(letters.charAt(i))-1);
                }
            }
        }
        if (!(score >= match.length() / 2)) {
            return null;
        }

        if(dif > 0) { // if the string we're analyzing is shorter than the target string
            int cnt1 = 0, cnt2 = 0;
            while(cnt1 < letters.length()) {
                if(letters.charAt(cnt1) == match.charAt(cnt2)) { // if they're equal, inc the score
                    ++totScore;
                    ++cnt2;
                }
                else if(dif <= 0) {
                    if(cnt1+1< letters.length() && letters.charAt(cnt1 + 1) == match.charAt(cnt2)) {
                        ++totScore; // give a chance to skip over to the next letter
                        ++cnt1;
                    }
                    ++cnt2;
                }
                else if(dif > 0) { // if we still have available spaces in between the words
                    while(dif>0) {
                        ++cnt2;
                        --dif;
                        if(letters.charAt(cnt1) == match.charAt(cnt2)) {
                            ++totScore;
                            break;
                        }

                    }
                }
                ++cnt1;

            }
        }

        else {
            int cnt1 = 0, cnt2 = 0; // if the target string is shorter or equal to the analyzed string
            // cnt1 accesses analyzed string, cnt2 accesses target string
            while(cnt2<match.length()) {
                if(letters.charAt(cnt1) == match.charAt(cnt2)) { // if they're equal, inc the score
                    ++totScore;
                    ++cnt1;
                }
                else if(dif <= 0) {
                    if(cnt2+1< match.length() && (match.charAt(cnt2 + 1) == letters.charAt(cnt1))) {
                        ++totScore; // give a chance to skip over to the next letter
                        ++cnt2;
                    }
                    ++cnt1;
                }
                else if(dif > 0) { // if we still have available spaces in between the words
                    while(dif>0) {
                        ++cnt1;
                        --dif;
                        if(letters.charAt(cnt1) == match.charAt(cnt2)) {
                            ++totScore;
                            break;
                        }

                    }
                }
                ++cnt2;

            }
        }
        if(totScore>=match.length()/2) {
            if(!let) {
                return match;
            }
            else {
                return Character.toString(main.charAt(main.length()-1));
            }
        }
        return null;
    }

}