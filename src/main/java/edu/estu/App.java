package edu.estu;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.InputStream;
import java.util.Arrays;

public class App {
    @Argument(required = true, usage = "url")
    private static String url;

    public static void main(String[] args) {
        try {
            new App().Parser(args);
            String string = "";
            Document doc = Jsoup.connect(url).get();
            Elements paragraphs = doc.select("p");
            for (Element paragraph : paragraphs) {
                string += paragraph;
            }
            InputStream in = App.class.getResourceAsStream("/en-token.bin");
            TokenizerModel tokenModel = new TokenizerModel(in);

            TokenizerME tokenizer = new TokenizerME(tokenModel);

            String[] tokens = tokenizer.tokenize(string);
            InputStream in2 = App.class.getResourceAsStream("/en-ner-person.bin");
            TokenNameFinderModel model = new TokenNameFinderModel(in2);

            NameFinderME nameFinder = new NameFinderME(model);

            Span[] nameSpans = nameFinder.find(tokens);
            System.out.println(Arrays.toString(Span.spansToStrings(nameSpans, tokens)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Parser(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
    }
}
