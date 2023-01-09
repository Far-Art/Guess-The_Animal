package animals.services;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import animals.datastructures.tree.BinaryKnowledgeTree;
import animals.localization.Localized;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;

public class KnowledgeSaveService {

    private static String fileType = "json";

    private BinaryKnowledgeTree<String> knowledgeTree;

    public static void setFileType(String fileType) {
        KnowledgeSaveService.fileType = fileType;
    }

    public void save(BinaryKnowledgeTree<String> knowledgeTree) throws IOException {
        this.knowledgeTree = knowledgeTree;
        File file = getFile();
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        toJson();
    }

    public BinaryKnowledgeTree<String> load() throws IOException {
        return toPojo();
    }

    private void toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(getFile(), knowledgeTree);
    }

    private BinaryKnowledgeTree<String> toPojo() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructParametricType(BinaryKnowledgeTree.class, String.class);
        try {
            return new ObjectMapper().readValue(getFile(), type);
        } catch (FileNotFoundException e) {
            // do nothing as it totally ok
        }
        return null;
    }

    private File getFile() {
        return new File(getPath());
    }

    private String getPath() {
        return String.format("animals%s.%s", Locale.getDefault().equals(Localized.DEFAULT.getLocale()) ? "" : "_" + Locale.getDefault(), fileType);
    }

}
