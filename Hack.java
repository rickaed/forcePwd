import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class Hack {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, DigestException {
        String response = null;
        while (response == null) {
            response = openChallengeRequest();

        }

        Map<String, String> responseMap = hashMapFormat(response);
        String id = responseMap.get("id");
        String hash = responseMap.get("hash");
        String salt = responseMap.get("salt");

        String pwd = forcePwd(hash, salt);
        if (!pwd.equals("Fail")) {
            System.out.println("@@@@@@@@ YES @@@@@@@ ," + pwd);
            System.out.println(id);
            sendAnswer(pwd, id);
        } else {
            System.out.println("Et c'est un Epic " + pwd);
        }
        // sendAnswer(pwd, id);

    }

    public static String openChallengeRequest() {
        String openUrl = "https://shallenge.onrender.com/challenges";
        try {
            URL url = new URL(openUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(1000 * 60 * 60);
            con.setReadTimeout(1000 * 60 * 60);
            // con.setRequestProperty("Content-Type", "application/json");
            // con.setRequestProperty("Accept", "application/json");

            // reading response
            String response = FullResponseBuilder.getFullResponse(con);
            System.out.println("retour API : " + response);

            response = response.substring(1, response.length() - 1);
            return response;

        } catch (Exception e) {
            System.out.println("Opening Challenge Fail");
            openChallengeRequest();
        }
        return null;
    }

    public static Map<String, String> hashMapFormat(String content) {
        String[] dataPairs = content.replaceAll("\"", "").split(",");
        Map<String, String> responseMap = new HashMap<>();
        for (String pair : dataPairs) {
            String[] entry = pair.split(":");
            responseMap.put(entry[0].trim(), entry[1].trim());
        }
        return responseMap;
    }

    public static String forcePwd(String hash, String salt)
            throws DigestException, NoSuchAlgorithmException, IOException {

        for (int a = 0; a < 26; a++) {// ch.length
            System.out.println((char) ('a' + a));
            for (int b = 0; b < 26; b++) {
                for (int c = 0; c < 26; c++) {
                    for (int d = 0; d < 26; d++) {
                        for (int e = 0; e < 26; e++) {
                            for (int f = 0; f < 26; f++) {

                                String pwd = "" + (char) ('a' + a) + (char) ('a' + b) + (char) ('a' + c)
                                        + (char) ('a' + d) + (char) ('a' + e) + (char) ('a' + f);

                                String myHash = hashPass(pwd, salt);
                                if (myHash.equals(hash)) {
                                    System.out.println("Match Found" + pwd);
                                    return pwd;
                                }

                            }
                        }
                    }
                }
            }
        }
        return "Fail";
    }

    private static String hashPass(String password, String salt) throws IOException, NoSuchAlgorithmException {
        HexFormat hexFormat = HexFormat.of();
        byte[] saltBytes = hexFormat.parseHex(salt);
        byte[] pwdBytes = password.getBytes();

        byte[] saltPwdToHash = Arrays.copyOf(saltBytes, saltBytes.length + pwdBytes.length);
        System.arraycopy(pwdBytes, 0, saltPwdToHash, saltBytes.length, pwdBytes.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] digest = md.digest(saltPwdToHash);

        return hexFormat.formatHex(digest);
    }

    private static String sendAnswer(String pwd, String id) throws IOException {
        String repUrl = "https://shallenge.onrender.com/challenges/" + id + "/answer";
        System.out.println(repUrl);
        URL url = new URL(repUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonIntpuString = "\"" + pwd + "\"";
        try (OutputStream outputStream = con.getOutputStream()) {
            byte[] input = jsonIntpuString.getBytes("utf-8");
            outputStream.write(input, 0, input.length);
            outputStream.flush();
            outputStream.close();
        }
        // reading response
        System.out.println(con.getResponseCode());

        String response = FullResponseBuilder.getFullResponse(con);
        System.out.println("retour API : " + response);

        response = response.substring(1, response.length() - 1);
        return response;

    }

}
