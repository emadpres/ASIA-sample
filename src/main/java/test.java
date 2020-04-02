import api.ASIASim;
import api.LoadStartingData;
import bean.RetrievedDocument;
import ir.Preprocessing;

import java.util.Map;

public class test {
    public static void main(String[] args) throws Exception {


        Preprocessing preprocessing = LoadStartingData.getPreprocessing("./resources/stopword/stop-words-english.txt",
                "./resources/stopword/stop-words-java.txt",
                "./resources/androidAPI/android-API.csv",
                "./resources/androidAPI/android_constants.txt");
        ASIASim.Initialize(preprocessing, "./resources/idf-code-only-entire-dataset");

        String s1 = "ConnectivityManager mgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); Method dataMtd = ConnectivityManager.class.getDeclaredMethod(setMobileDataEnabled, boolean.class); dataMtd.setAccessible(true); dataMtd.invoke(mgr, false);";
        String s1_noAndroid = "preprocessing.LoadStartingData(stopWords)";
        String s2 = "ConnectivityManager connManager = getSystemService(); Method dataMtd = ConnectivityManager.class.getDeclaredMethod(setMobileDataEnabled, boolean.class); dataMtd.setAccessible(false); connManager.invoke(mgr, true);";
        String s2_noAndroid = "preprocessing.LoadEndingData(CONNECTIVITY, SERVICE);";


        SampleUsage(s1,s2, preprocessing);
        System.out.println("---------------------------");
        SampleUsage(s1_noAndroid,s2, preprocessing);
        System.out.println("---------------------------");
        SampleUsage(s1_noAndroid,s2_noAndroid, preprocessing);
    }

    static void SampleUsage(String s1, String s2, Preprocessing preprocessing) throws Exception {
        {
            RetrievedDocument res = ASIASim.CalculateSim(s1, s2);
            System.out.printf("Similarity = %f (Android: %.2f)(IR: %.2f)\n", res.getSimilarityScore(), res.getAndroidSimilarity(), res.getIrSimilarity());
        }


        {
            String cleanedText1 = preprocessing.CleanText(s1);
            String cleanedText2 = preprocessing.CleanText(s2);
            String[] tokens1 = preprocessing.SplitForAndroidSim(cleanedText1);
            String[] tokens2 = preprocessing.SplitForAndroidSim(cleanedText2);
            Map<String, Double> splitTokens1 = preprocessing.SplitAndTokenize(cleanedText1);
            Map<String, Double> splitTokens2 = preprocessing.SplitAndTokenize(cleanedText2);
            RetrievedDocument res2 = ASIASim.CalculateSim(tokens1, tokens2, splitTokens1, splitTokens2);
            System.out.printf("Similarity = %f (Android: %.2f)(IR: %.2f)\n", res2.getSimilarityScore(), res2.getAndroidSimilarity(), res2.getIrSimilarity());
        }
    }
}
