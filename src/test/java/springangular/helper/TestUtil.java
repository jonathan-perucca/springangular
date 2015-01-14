package springangular.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class TestUtil {

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}