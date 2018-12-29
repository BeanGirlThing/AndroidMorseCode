package me.merhlim.jessica.morsecode;

import android.util.Log;

public class MorseProcessing {

    public static String[] stringToMorseSequence(String stringOfLetters){
        int lenStringOfLetters = stringOfLetters.length();
        String output[] = new String[2];

        StringBuilder compInterpreted = new StringBuilder();
        for(int i = 0; i < lenStringOfLetters; i++) {
            String focus = String.valueOf(stringOfLetters.charAt(i));
            String nextCharacter = "";
            String morseProcessed = processCharacter(focus);
            if (i != lenStringOfLetters - 1) {
                nextCharacter = String.valueOf(stringOfLetters.charAt(i + 1));
            } else {
                nextCharacter = "END";
            }
            if (morseProcessed.equals("#")){
                compInterpreted.append("#");
                continue;
            } else {
                if(!nextCharacter.equals(" ")){
                    compInterpreted.append(morseProcessed+"@");
                } else {
                    compInterpreted.append(morseProcessed);
                }
            }
            if (nextCharacter.equals("END")){
                compInterpreted.append("!");
            }

        }
        Log.i("MORSE","Computer interpretable morse sequence = " + compInterpreted.toString());
        output[0] = compInterpreted.toString();

        String temp;
        temp = compInterpreted.toString().replace("@"," ");
        temp = temp.replace("#","  ");
        temp = temp.replace("!", "");
        output[1] = temp;

        Log.i("MORSE","Human interpretable morse sequence = " + output[1]);

        return output;
    }

    public static String processCharacter(String character) {
        String value = "";
        String check = character.toLowerCase();

        if (check.equals( "a")){
            value = ".-";

        } else if (check.equals("b")){
            value = "-...";

        } else if (check.equals("c")){
            value = "-.-.";

        } else if (check.equals("d")){
            value = "-..";

        } else if (check.equals("e")){
            value = ".";

        } else if (check.equals("f")){
            value = "..-.";

        } else if (check.equals("g")){
            value = "--.";

        } else if (check.equals("h")){
            value = "....";

        } else if (check.equals("i")){
            value = "..";

        } else if (check.equals("j")){
            value = ".---";

        } else if (check.equals("k")){
            value = "-.-";

        } else if (check.equals("l")){
            value = ".-..";

        } else if (check.equals("m")){
            value = "--";

        } else if (check.equals("n")){
            value = "-.";

        } else if (check.equals("o")){
            value = "---";

        } else if (check.equals("p")){
            value = ".--.";

        } else if (check.equals("q")){
            value = "--.-";

        } else if (check.equals("r")){
            value = ".-.";

        } else if (check.equals("s")){
            value = "...";

        } else if (check.equals("t")){
            value = "-";

        } else if (check.equals("u")){
            value = "..-";

        } else if (check.equals("v")){
            value = "...-";

        } else if (check.equals("w")){
            value = ".--";

        } else if (check.equals("x")){
            value = "-..-";

        } else if (check.equals("y")){
            value = "-.--";

        } else if (check.equals("z")){
            value = "--..";

        } else if (check.equals("1")){
            value = ".----";

        } else if (check.equals("2")){
            value = "..---";

        } else if (check.equals("3")){
            value = "...--";

        } else if (check.equals("4")){
            value = "....-";

        } else if (check.equals("5")){
            value = ".....";

        } else if (check.equals("6")){
            value = "-....";

        } else if (check.equals("7")){
            value = "--...";

        } else if (check.equals("8")){
            value = "---..";

        } else if (check.equals("9")){
            value = "----.";

        } else if (check.equals("0")){
            value = "-----";

        } else if (check.equals(" ")) {
            value = "#";

        } else {
            value = "~";
        }
        return value;
    }
}