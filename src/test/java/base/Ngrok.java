package base;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ngrok {

    @Test
    public void test2() {
        try {
            statNgrok();
            System.out.println("Public URL: " + getNgrokPublicURL().toString());
            runOnLinux("killall ngrok");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    private StringBuilder getNgrokPublicURL() throws IOException, InterruptedException {
        Thread.sleep(5000);
        StringBuilder url = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", "curl --silent --show-error http://127.0.0.1:4040/api/tunnels | sed -nE 's/.*public_url\":\"https:..([^\"]*).*/\\1/p'");
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            System.out.println("Success!");
            url = output;
        } else {
            System.out.println("System Error!");
        }
        return url;
    }

    public void statNgrok() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String path = System.getProperty("user.dir") + "/src/test/java/base/";
        processBuilder.command("bash", "-c", "cd " +path+ ";./ngrok http 8080");
        processBuilder.start();
    }

    public void runOnLinux(String command) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", command);
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        int exitVal = process.waitFor();
        System.out.println("Exit Value: " + exitVal);
        if (exitVal == 0) {
            System.out.println(output);
        } else {
            System.out.println("System Error! Exit Value: " + exitVal);
        }
    }

}
