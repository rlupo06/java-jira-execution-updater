package utils;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.GherkinDocument;
import gherkin.pickles.Pickle;
import gherkin.pickles.Compiler;
import gherkin.pickles.PickleTag;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

public class Utilities {

    public static FileReader fileReader(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(file + " is not found " + e);
        }
    }

    public static List<GherkinDocument> getAllGerkinDocuments(String pathToFeatureFiles) {
        List<GherkinDocument> gherkinDocuments = new ArrayList<>();
        List<File> files = getFeatures(pathToFeatureFiles);
        files.forEach(file -> {
            GherkinDocument gherkinDocument = parseFeatureFile(file);
            gherkinDocuments.add(gherkinDocument);
        });
        return gherkinDocuments;
    }

    public static List<File> getFeatures(String pathToFeatureFiles) {
        try {
            File folder = new File(pathToFeatureFiles);
            File[] filesAndDirectory = folder.listFiles();
            return Arrays.stream(filesAndDirectory).filter(file -> file.isFile()).collect(Collectors.toList());
        }catch(NullPointerException e){
            throw new IllegalArgumentException("Unable to find feature files at this location " + pathToFeatureFiles);
        }
    }

    public static GherkinDocument parseFeatureFile(File featureFile) {
        FileReader featureReader = Utilities.fileReader(featureFile);
        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        GherkinDocument  gherkinDocument = parser.parse(featureReader);
        return gherkinDocument;
    }

    public static List<Pickle> parseGerkinDocument(GherkinDocument gherkinDocument) {
        return new Compiler().compile(gherkinDocument);
    }


    public static int extractJiraIssueId(Pickle pickle) {
        try {
            return extractJiraIssueId(pickle.getTags());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("%s %s, %s", "Scenario:", pickle.getName(), "is missing a jira issue id tag"));
        }
    }

    private static int extractJiraIssueId(List<PickleTag> tags) throws NoSuchElementException {
        PickleTag jiraIssueIdTag = tags.stream().filter(tag -> tag.getName().replace("@", "").matches("^-?\\d+$")).findFirst().get();
        return Integer.parseInt(jiraIssueIdTag.getName().replace("@", ""));
    }
}
