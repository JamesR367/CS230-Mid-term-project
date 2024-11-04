//Authors: James Ramsey & Ethan Buckner

package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class pepasm {
    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder output = new StringBuilder();
        int lineNum = 1, functionStart = 0;
        File myObj = new File("src\\main\\resources\\program4.pep"); //change this to args[0] before submitting
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if (!data.isEmpty()) {
                data = removeCommentAndSpaces(data);
                functionStart += getBytesForFunctionStart(lineNum, data);
                data = removeFunctionCall(data);
                data = removeFunctionBranch(data);
                output.append(getOperator(data));
                output.append(getOperandForFunction(data, functionStart));
                data = removeOperator(data);
                output.append(getOperand(data)).append("\n");
            }
            lineNum++;
        }
        System.out.println(output);
    }

    private static String getOperator(String line) {
        HashMap<String, String> Operator;
        String output;
        Operator = createOperatorHashmap();
        output = Operator.get(line.substring(0, 4)) + getIndicator(line);

        return output;
    }

    private static StringBuilder getOperand(String line) {
        StringBuilder output = new StringBuilder(line);
        if (!output.isEmpty()) {
            while (output.length() < 4) {
                output.insert(0, "0");
            }
            output.insert(2, ' ');
        }
        return output;
    }

    private static String removeCommentAndSpaces(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ';') {
                line = line.substring(0, line.lastIndexOf(";"));
            }
        }
        line = line.replace(" ", "");
        return line;
    }

    private static String removeFunctionCall(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ':') {
                line = line.substring(line.lastIndexOf(":") + 1);
            }
        }
        return line;
    }

    private static int getBytesForFunctionStart(int lineNum, String line) {
        if (line.indexOf(':') > 0) {
            return (lineNum - 1) * 3;
        }
        return 0;
    }

    private static String removeFunctionBranch(String line) {
        if (line.startsWith("BR")) {
            line = line.substring(0, 4);
        }
        return line;
    }

    private static String getOperandForFunction(String line, int bytes) {
        String output = "";
        if (line.startsWith("BRNE")) {
            output = "00 0" + bytes;
        }
        return output;
    }

    private static String removeOperator(String line) {
        if (line.length() > 4) {
            return line.substring(6, line.length() - 2);
        }
        return "";
    }

    public static HashMap<String, String> createOperatorHashmap() {
        HashMap<String, String> Operands = new HashMap<>();
        Operands.put("LDBA", "D");
        Operands.put("STBA", "F");
        Operands.put("LDWA", "C");
        Operands.put("STWA", "E");
        Operands.put("ANDA", "8");
        Operands.put("ADDA", "6");
        Operands.put("ASLA", "0A");
        Operands.put("ASRA", "0C");
        Operands.put("CPBA", "B");
        Operands.put("BRNE", "1A ");
        Operands.put("STOP", "00");
        Operands.put(".END", "zz");

        return Operands;
    }

    public static String getIndicator(String line) {
        if (line.charAt(line.length() - 1) == 'd') {
            return "1 ";
        } else if (line.charAt(line.length() - 1) == 'i') {
            return "0 ";
        }
        return "";
    }


}
