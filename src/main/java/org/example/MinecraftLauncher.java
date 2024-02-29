package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MinecraftLauncher {

    public static void main(String[] args) throws IOException, ParseException {
        String selectedVersion = "1.19.2"; // Például

        // Verzióinformációk beolvasása a versions.json fájlból
        //JSONObject versionJson = (JSONObject) new JSONParser().parse(new FileReader("versions.json"));
        //String gameDirector = (String) versionJson.get(selectedVersion).get("gameDirector");
        //String version = (String) versionJson.get(selectedVersion).get("version");

        String gameDirector = "C:/Users/komar/PycharmProjects/MineLauncher/modpacks/createmod";
        String version = "1.20.1-forge-47.2.0";

        // Bejelentkezési adatok előkészítése
        Map<String, String> login = new HashMap<>();
        login.put("username", "username"); // Helyettesítsd be a felhasználóneveddel
        login.put("executablePath", gameDirector); // Helyettesítsd be a Java elérési útvonalával
        login.put("jvmArguments", "-Xmx4G -Xms4G"); // Példa a memóriabeállításokra

        // Minecraft parancs létrehozása
        String minecraftCommand = String.format(
                "java -Xmx%s -Xms%s -cp %s/libraries/ -Djava.library.path=%s/natives/ -Dversion=%s %s",
                login.get("jvmArguments"),
                login.get("jvmArguments"),
                login.get("executablePath"),
                login.get("executablePath"),
                version,
                gameDirector
        );

        // Minecraft parancs kiírása
        System.out.println(minecraftCommand);

        // Minecraft parancs futtatása (megjegyzés: ez a sor a tényleges futtatáshoz szükséges)
        // Process process = Runtime.getRuntime().exec(minecraftCommand);
    }
}


