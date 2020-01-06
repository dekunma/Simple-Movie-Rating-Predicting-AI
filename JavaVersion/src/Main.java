import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;
public class Main {
    public static File rawReviewsInputFile;
    public static File rawReviewRatingsInputFile;
    public static Set<String> stopWords;
    public static File stopWordsFile;
    public static File cleanReviewRatingsOutputFile;
    public static Map<String, Pair<Long, Long>> dictionary;
    public static File cleanReviewsInputFile;
    public static File ratingsOutputFile;
    public static File clearReviewRatingsInputFile;
    public static File cleanReviewsOutputFile;
    public static void main(String[] args) throws Exception {
        A2 main = new A2();
        if(args.length != 2){
            throw new Exception("need two arguments");
        }

        rawReviewRatingsInputFile = new File("src/"+args[0]);

        cleanReviewRatingsOutputFile = new File("src/cleanReviewRatings.txt");

        stopWords = new HashSet<String>();

        stopWordsFile = new File("src/stopwords.txt");

        main.fillStopWords(stopWordsFile, stopWords);

        main.cleanData(rawReviewRatingsInputFile, cleanReviewRatingsOutputFile, stopWords);

        dictionary = new HashMap<>();

        clearReviewRatingsInputFile = new File("src/cleanReviewRatings.txt");

        main.fillDictionary(clearReviewRatingsInputFile, dictionary);

        rawReviewsInputFile = new File("src/" + args[1]);

        cleanReviewsOutputFile = new File("src/cleanReviews.txt");

        main.cleanData(rawReviewsInputFile, cleanReviewsOutputFile, stopWords);

        cleanReviewsInputFile = new File("src/cleanReviews.txt");

        ratingsOutputFile = new File("src/ratings.txt");

        main.rateReviews(cleanReviewsInputFile, dictionary, ratingsOutputFile);
    }
}
