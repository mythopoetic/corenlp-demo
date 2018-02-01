import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.util.CoreMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AnalyzeText {
    private StanfordCoreNLP pipeline;
    private String text;
    private Properties props;
    private DependencyParser dp;

    public AnalyzeText(StanfordCoreNLP pipeline, String text, DependencyParser dp) {
        this.pipeline = pipeline;
        this.text = text;
        this.dp = dp;
    }

    public void analyze() {

        System.out.println("---");
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {

            //Print out the sentiment of each sentence
            //printSentiment(sentence);

            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                System.out.println(String.format("Word: [%s], Pos: [%s], Ne: [%s]", word, pos, ne));
            }
        }

        System.out.println("---");

        //Print out coref chains and mentions
        printCorefChains(document, sentences);

        //Print out the dependency tree
        printDependencyTree(sentences, dp);

        //Print out time
        printTime(document);
    }

    private void printSentiment(CoreMap sentence) {

        System.out.println("---");
        System.out.println("Sentiment:");
        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
        System.out.println(sentiment + "\t" + sentence);
        System.out.println("---");
    }

    private void printCorefChains(Annotation document, List<CoreMap> sentences) {

        System.out.println("---");
        System.out.println("coref chains");

        for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
            System.out.println("\t" + cc);
        }
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            System.out.println("mentions");
            for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                System.out.println("\t" + m);
            }
        }
    }

    private void printDependencyTree(List<CoreMap> sentences, DependencyParser dp) {

        for (CoreMap sentence : sentences) {
            GrammaticalStructure gs = dp.predict(sentence);
            System.out.println("Dependency tree: " + gs.toString());
        }
        System.out.println("---");
    }

    private void printTime(Annotation document) {

        //Get and format current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(new Date());
        document.set(CoreAnnotations.DocDateAnnotation.class, date + "T" + time);

        pipeline.annotate(document);

        System.out.println("Sentence: " + document.get(CoreAnnotations.TextAnnotation.class));
        List<CoreMap> timexAnnsAll = document.get(TimeAnnotations.TimexAnnotations.class);
        System.out.println("Time:");

        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            System.out.println(cm.get(TimeExpression.Annotation.class).getTemporal());
        }
        System.out.println("---");
    }

    public void analyzeSwedish(String text) {

        MaxentTagger maxentTagger = new MaxentTagger("C:/Users/E603772/Desktop/stanford-corenlp-full-2017-06-09/swedish.tagger");
        String tag = maxentTagger.tagString(text);
        String[] eachTag = tag.split("\\s+");
        System.out.println("Word      " + "Standford tag");
        System.out.println("----------------------------------");
        for(int i = 0; i< eachTag.length; i++) {
            System.out.println(eachTag[i].split("_")[0] +"           "+ eachTag[i].split("_")[1]);
        }
    }
}
