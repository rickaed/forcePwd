import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

public class FullResponseBuilder {
    public static String getFullResponse(HttpURLConnection request) throws IOException {
        StringBuilder fullResponseBuilder = new StringBuilder();

        // read status and message
        fullResponseBuilder.append(request.getResponseCode())
                .append(" ")
                .append(request.getResponseMessage())
                .append("\n");

        // read headers

        // read response content
        int status = request.getResponseCode();
        // status
        Reader streamReader = null;
        if (status > 299) {
            streamReader = new InputStreamReader(request.getErrorStream());
        } else {
            streamReader = new InputStreamReader(request.getInputStream());
        }

        // body
        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();

        return content.toString();
    }
}