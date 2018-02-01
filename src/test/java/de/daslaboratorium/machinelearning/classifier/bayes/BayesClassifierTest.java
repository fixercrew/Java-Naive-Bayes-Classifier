package de.daslaboratorium.machinelearning.classifier.bayes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import de.daslaboratorium.machinelearning.classifier.RestartData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;

import java.util.Map;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BayesClassifierTest {

    private static final double EPSILON = 0.001;
    private static final String CATEGORY_NEGATIVE = "negative";
    private static final String CATEGORY_POSITIVE = "positive";
    private Classifier<String, String> bayes;

    @Before
    public void setUp() {
        /*
         * Create a new classifier instance. The context features are Strings
         * and the context will be classified with a String according to the
         * featureset of the context.
         */
        bayes = new BayesClassifier<String, String>();

        /*
         * The classifier can learn from classifications that are handed over to
         * the learn methods. Imagin a tokenized text as follows. The tokens are
         * the text's features. The category of the text will either be positive
         * or negative.
         */
        final String[] positiveText = "I love sunny days".split("\\s");
        bayes.learn(CATEGORY_POSITIVE, Arrays.asList(positiveText));

        final String[] negativeText = "I hate rain".split("\\s");
        bayes.learn(CATEGORY_NEGATIVE, Arrays.asList(negativeText));
    }

    @Test
    public void testStringClassification() {
        final String[] unknownText1 = "today is a sunny day".split("\\s");
        final String[] unknownText2 = "there will be rain".split("\\s");

        Assert.assertEquals(CATEGORY_POSITIVE, bayes.classify(Arrays.asList(unknownText1)).getCategory());
        Assert.assertEquals(CATEGORY_NEGATIVE, bayes.classify(Arrays.asList(unknownText2)).getCategory());
    }

    @Test
    public void testStringClassificationInDetails() {

        final String[] unknownText1 = "today is a sunny day".split("\\s");

        Collection<Classification<String, String>> classifications = ((BayesClassifier<String, String>) bayes)
                .classifyDetailed(Arrays.asList(unknownText1));

        List<Classification<String, String>> list = new ArrayList<Classification<String, String>>(classifications);

        Assert.assertEquals(CATEGORY_NEGATIVE, list.get(0).getCategory());
        Assert.assertEquals(0.0078125, list.get(0).getProbability(), EPSILON);

        Assert.assertEquals(CATEGORY_POSITIVE, list.get(1).getCategory());
        Assert.assertEquals(0.0234375, list.get(1).getProbability(), EPSILON);
    }

    @Test
    public void testSerialization() throws IOException {

        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(bayes);
    }

    @Test
    public void testRestart() {
        Dictionary featureCountPerCategory = bayes.getFeatureCountPerCategory();
        Dictionary totalFeatureCount = bayes.getTotalFeatureCount();
        Dictionary totalCategoryCount = bayes.getTotalCategoryCount();
        BayesClassifier bayes2 = new BayesClassifier<String, String>();
        bayes2.restart(featureCountPerCategory, totalFeatureCount, totalCategoryCount);
        final String[] unknownText1 = "today is a sunny day".split("\\s");
        final String[] unknownText2 = "there will be rain".split("\\s");

        Assert.assertEquals(CATEGORY_POSITIVE, bayes2.classify(Arrays.asList(unknownText1)).getCategory());
        Assert.assertEquals(CATEGORY_NEGATIVE, bayes2.classify(Arrays.asList(unknownText2)).getCategory());
    }

    @Test
    public void testReadJson() throws IOException {
        String Json = bayes.getJson();
        BayesClassifier newBayes = new BayesClassifier<String, String>();
        newBayes.readJson(Json);

        newBayes.classify(Arrays.asList("sunny")).getCategory();

        Assert.assertEquals(bayes.getFeatureCountPerCategory(), newBayes.getFeatureCountPerCategory());
        Assert.assertEquals(bayes.getTotalFeatureCount(), newBayes.getTotalFeatureCount());
        Assert.assertEquals(bayes.getTotalCategoryCount(), newBayes.getTotalCategoryCount());

    }

}