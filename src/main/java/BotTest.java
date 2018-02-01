import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotTest {

    private List<String> intentList = Arrays.asList("Check", "account", "balance");
    Account myAccount = new Account("Gustav", 123456789, 50000);


    public void analyzeInput(StanfordCoreNLP pipeline, String text) {

        List<String> verbList = new ArrayList<String>();
        List<String> nounList = new ArrayList<String>();
        boolean vb = false;
        boolean nn = false;

        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                if (pos.equalsIgnoreCase("vb")) {
                    verbList.add(word);
                    vb = true;
                }

                if (pos.equalsIgnoreCase("nn")) {
                    nounList.add(word);
                    nn = true;
                }
            }
        }

        if (vb == true && nn == true) {
            boolean verbFound = false;
            boolean nounFound = false;
            for (String intentWord : intentList) {
                for (String verb : verbList) {
                    if (verb.equalsIgnoreCase(intentWord)) {
                        verbFound = true;
                    }
                }
                for (String noun : nounList) {
                    if (noun.equalsIgnoreCase(intentWord)) {
                        nounFound = true;
                    }
                }
            }
            if (verbFound == true && nounFound == true) {
                System.out.println("Retrieving account balance for " + myAccount.getAccountHolder() + ". You currently have " + myAccount.getAccountBalance() + " SEK on your account.");
            } else {
                System.out.println("I don't understand your intention, please specify what you want to do");
            }
        } else {
            System.out.println("I don't understand your intention, please specify what you want to do");
        }
    }
}

