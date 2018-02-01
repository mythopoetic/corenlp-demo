import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotator;

import java.util.Properties;
import java.util.Scanner;

public class CoreNlpExample {

    public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention, coref, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String modelPath = DependencyParser.DEFAULT_MODEL;
        DependencyParser dp = DependencyParser.loadFromModelFile(modelPath);
        Scanner sc = new Scanner(System.in);

        //BotTest testBot = new BotTest();

        //Add annotators for SUTime
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));

        //String text = "Kosgi Santosh sent fifty-six letters to Stanford University on the 14th of June 1887, the first at 7:45 AM. All of them were ignored. Now he is very angry!";
        //String text = "President Xi Jingping of China, on his first state visit to the united States, showed off his familiarity with American history and pop culture on Tuesday night.";
        //String text = "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today. The first date occured on February 18th 21 years ago.";
        while(true) {
            System.out.print("Text: ");
            String text = sc.nextLine();
            if(text.equalsIgnoreCase("q")){
                break;
            }

            AnalyzeText analyze = new AnalyzeText(pipeline, text, dp);
            //analyze.analyze();

            //testBot.analyzeInput(pipeline, text);

            analyze.analyzeSwedish(text);

        }
    }
}
