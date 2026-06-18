package karunasagarlearning.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataReader {

    public List<HashMap<String, String>> getJsonDataToMap() throws IOException {
        // Use File.separator for cross-platform compatibility (Windows/Linux/Mac)
        String jsonPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" 
                        + File.separator + "java" + File.separator + "karunasagarlearning" + File.separator 
                        + "data" + File.separator + "PurchaseOrder.json";
        
        // Read json file to string
        String jsonContent = FileUtils.readFileToString(new File(jsonPath), StandardCharsets.UTF_8);

        // String to HashMap (Jackson databind)
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(jsonContent,
                new TypeReference<List<HashMap<String, String>>>() {});
        
        return data;
    }
}