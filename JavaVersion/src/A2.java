import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class A2 {
    public void cleanData(File inFile, File outFile, Set<String> stopWords)
            throws IOException {
        Scanner sc = new Scanner(inFile);
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            ArrayList<String> al1 = new ArrayList<>();
            ArrayList<String> al2 = new ArrayList<>();
            replaceHyphensWithSpaces(line);
            splitLine(line, al1);
            removePunctuation(al1, al2);
            removeWhiteSpaces(al2);
            removeEmptyWords(al2);
            removeSingleLetterWord(al2);
            removeStopWords(al2, stopWords);
            for (int i = 0; i < al2.size(); i++){
                out.write(al2.get(i) + " ");
            }
            out.write("\n");
        }
        out.close();
    }

    public void fillDictionary(File newInFile, Map<String, Pair<Long, Long>> dict)
            throws FileNotFoundException {
        Scanner sc = new Scanner(newInFile);
        while (sc.hasNextLine() && sc.hasNext()){
            String line = sc.nextLine();
            Long rating = Long.valueOf(line.substring(0,1));
            line = line.substring(2);
            while(line.indexOf(" ") != -1){
                int spaceIndex = line.indexOf(" ");
                String w = line.substring(0, spaceIndex);
                line = line.substring(spaceIndex + 1);
                if(!dict.containsKey(w)){
                    dict.put(w,new Pair<>(rating, 1L));
                }
                else {
                    dict.put(w, new Pair<>(dict.get(w).getKey() + rating
                            , dict.get(w).getValue() + 1));
                }
            }
        }
    }

    public void fillStopWords(File inFile, Set<String> stopWords)
            throws FileNotFoundException {
        Scanner sc = new Scanner(inFile);
        while(sc.hasNextLine() && sc.hasNext()){
            String word = sc.next();
            stopWords.add(word);
        }
    }

    public void rateReviews(File testFile,
                            Map<String, Pair<Long, Long>> dict, File ratingsFile)
                            throws IOException {
        Scanner sc = new Scanner(testFile);
        BufferedWriter out = new BufferedWriter(new FileWriter(ratingsFile));
        while (sc.hasNextLine() && sc.hasNext()){
            String line = sc.nextLine();
            int count = 0;
            double rating = 0;
            while(line.contains(" ")){
                int spaceIndex = line.indexOf(" ");
                String w = line.substring(0, spaceIndex);
                line = line.substring(spaceIndex + 1);
                if(!dict.containsKey(w)){
                    rating = rating + 2;
                }
                else {
                    rating = rating + (double)dict.get(w).getKey()/dict.get(w).getValue();
                }
                count++;
            }
            Double result = rating/count;
            out.write(result.toString());
            out.write("\n");
        }
        out.close();
    }

    public void removeEmptyWords(ArrayList<String> tokens){
        for(int i = 0; i < tokens.size(); i++){
            String elem = tokens.get(i);
            if(elem.isEmpty()){
                tokens.remove(i--);
            }
        }
    }

    public void removePunctuation(ArrayList<String> inTokens, ArrayList<String> outTokens){
        for(int i = 0; i < inTokens.size(); i++){
            String word = inTokens.get(i);
            for(int j = 0, length = word.length(); j < length; j++){
                char [] punctuation = {'.' , ',' , ';' , ':', '?' , '!' , '"' , '\'' , ')' , '(', '-'};
                for(char punct : punctuation){
                    if(word.charAt(j) == punct){
                        word = word.substring(0, j) + " " + word.substring(j + 1);
                        if(j != 0){
                            j--;
                        }
                        length = word.length();
                        if(j == length)
                            break;
                    }
                }
            }
            outTokens.add(word);
        }
    }

    public void removeSingleLetterWord(ArrayList<String> tokens){
        for(int i = 0; i < tokens.size(); i++){
            String word = tokens.get(i);
            if(word.length() == 1 && !word.equals("0") && !word.equals("1")
                    && !word.equals("2") && !word.equals("3") && !word.equals("4") && !word.equals("5")
                    && !word.equals("6") && !word.equals("7") && !word.equals("8") && !word.equals("9")){
                tokens.remove(i--);
            }
        }
    }

    public void removeStopWords(ArrayList<String> tokens, Set<String> stopWords){
        for(int i = 0; i < tokens.size(); i++){
            String elem = tokens.get(i);
            if(stopWords.contains(elem)){
                tokens.remove(i--);
            }
        }
    }

    public void removeWhiteSpaces(ArrayList<String> tokens){
        for(int i = 0; i < tokens.size(); i++){
            String trimmed = tokens.get(i).trim();
            tokens.set(i, trimmed);
        }
    }

    public void replaceHyphensWithSpaces(String line){
        int hypenIndex = 0;
        while(hypenIndex != -1){
            hypenIndex = line.indexOf("-");
            if(hypenIndex != -1){
                line = line.substring(0, hypenIndex) + " "
                        + line.substring(hypenIndex + 1);
            }
        }
    }

    public void splitLine(String line, ArrayList<String> words){
        int index = 0;
        while(index < line.length() && index != -1){
            index = line.indexOf(" ");
            if(index != -1){
                String elem = line.substring(0, index);
                line = line.substring(index + 1);
                words.add(elem);
            }
        }
        words.add(line);
    }
}

