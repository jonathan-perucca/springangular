package springangular.web.helper;

import com.google.common.base.Splitter;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;

public class HttpHelper {
    
    public static String getUri(WebRequest request) {
        Splitter splitter = Splitter.on("=").limit(2);
        Iterable<String> split = splitter.split(request.getDescription(false));
        Iterator<String> splitIterator = split.iterator();
        splitIterator.next(); // skip "uri" attribute
        return splitIterator.next();
    }
}
