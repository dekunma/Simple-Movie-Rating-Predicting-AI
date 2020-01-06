////////////////////////////////////////////////////////////////////////////////
// File Name:      a2.cpp
//
// Author:         Gerald
// CS email:       gerald@cs.wisc.edu
//
// Description:    The source file for a2.
//
// IMPORTANT NOTE: THIS IS THE ONLY FILE THAT YOU SHOULD MODIFY FOR A2.
//                 You SHOULD NOT MODIFY any of the following:
//                   1. Name of the functions/methods.
//                   2. The number and type of parameters of the functions.
//                   3. Return type of the functions.
////////////////////////////////////////////////////////////////////////////////

#include "a2.hpp"
#include "trim.hpp"

#include <algorithm>
#include <cctype>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <iterator>
#include <sstream>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <utility>
#include <vector>

using namespace std;

void cleanData(std::ifstream &inFile, std::ofstream &outFile,
               std::unordered_set<std::string> &stopwords) {
    // TODO: Implement this method.
    // # of lines of code in Gerald's implementation: 13
    // Do the following operations on each review before
    // storing it to the output file:
    //   1. Replace hyphens with spaces.
    //   2. Split the line of text into individual words.
    //   3. Remove the punctuation marks from the words.
    //   4. Remove the trailing and the leading whitespaces in each word.
    //   5. Remove the empty words.
    //   6. Remove words with just one character in them. You should NOT remove
    //      numbers in this step because if you do so, you'll lose the ratings.
    //   7. Remove stopwords.
    string line;
    while (getline(inFile, line)){
        vector<string> v1, v2;
        replaceHyphensWithSpaces(line);
        splitLine(line,v1);
        removePunctuation(v1,v2);
        removeWhiteSpaces(v2);
        removeEmptyWords(v2);
        removeSingleLetterWords(v2);
        removeStopWords(v2,stopwords);
        for(vector<string>::iterator iter = v2.begin(); iter != v2.end(); iter++){
            outFile << *iter << " ";
        }
        outFile << endl;
    }
}

void fillDictionary(std::ifstream &newInFile,
                    std::unordered_map<std::string, std::pair<long, long>> &dict) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 20
    string line;
    while (getline(newInFile, line)){
        stringstream ss (line);
        int rating;
        string w;
        ss >> rating;
        while (ss>>w) {
            if (dict.find(w) == dict.end()) {
                dict[w] = make_pair(rating, 1);
            } else {
                dict[w] = make_pair(dict[w].first + rating, dict[w].second + 1);
            }
        }
    }
}

void fillStopWords(std::ifstream &inFile,
                   std::unordered_set<std::string> &stopwords) {
    string word;
    while (getline(inFile, word)){
        stopwords.insert(word);
    }
}

void rateReviews(std::ifstream &testFile,
                 std::unordered_map<std::string, std::pair<long, long>> &dict,
                 std::ofstream &ratingsFile) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 20
    string line;
    while (getline(testFile, line)){
        stringstream ss (line);
        string w;
        int count = 0;
        double rating = 0;
        while (ss >> w){
            if (dict.find(w) == dict.end()){
                rating = rating + 2;
            }
            else {
                rating = rating + (double)dict[w].first/dict[w].second;
            }
            count++;
        }
        ratingsFile << rating/count << endl;
    }
}

void removeEmptyWords(std::vector<std::string> &tokens) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 5
    auto judgeEmpty = [](string elem){
        return elem.empty();
    };
    tokens.erase(remove_if(tokens.begin(), tokens.end(), judgeEmpty), tokens.end());
}

void removePunctuation(std::vector<std::string> &inTokens,
                       std::vector<std::string> &outTokens) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 10
    for (vector<string>::iterator iter = inTokens.begin() ; iter != inTokens.end(); iter++){
        string word = *iter;
        for (int i = 0, length = word.size(); i < length; i++)
        {
            if (ispunct(word[i]))
            {
                word.erase(i--, 1);
                length = word.size();
            }
        }
        outTokens.push_back(word);
    }
}

void removeSingleLetterWords(std::vector<std::string> &tokens) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 5
    auto judgeSingle = [](string word){
        return (word.length() == 1 && word != "0" && word != "1"
                && word != "2" && word != "3" && word != "4" && word != "5"
                && word != "6" && word != "7" && word != "8" && word != "9");
    };
    tokens.erase(remove_if(tokens.begin(), tokens.end(), judgeSingle), tokens.end());
}

void removeStopWords(std::vector<std::string> &tokens,
                     std::unordered_set<std::string> &stopwords) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 5
    auto judgeStop = [stopwords](string word) {
        return (stopwords.find(word) != stopwords.end());
    };
    tokens.erase(remove_if(tokens.begin(), tokens.end(), judgeStop), tokens.end());
}

void removeWhiteSpaces(std::vector<std::string> &tokens) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 5
    // You may want to use the trim() method from the trim.*pp files in a2.
    for (auto iter = tokens.begin(); iter != tokens.end(); iter++){
        *iter = trim(*iter);
    }
}

void replaceHyphensWithSpaces(std::string &line) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 5
    size_t hyphenIndex = 0;
    while(hyphenIndex != string::npos){
        hyphenIndex = line.find('-');
        if(hyphenIndex != string::npos){
            line.replace(hyphenIndex,1,1,' ');
        }
    }
}

void splitLine(std::string &line, std::vector<std::string> &words) {
    // TODO: Implement this method.
    // approximate # of lines of code in Gerald's implementation: < 10
    int index = 0;
    while((line.find(' ')) != std::string::npos){
        index = line.find(' ');
        string elem = line.substr(0, index);
        words.push_back(elem);
        line.erase(0, index + 1);
    }
    words.push_back(line);
}

