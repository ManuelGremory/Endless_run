package it.unicam.cs.mpcg.rpg129203.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gestisce la persistenza del gioco scrivendo e leggendo i dati di salvataggio in formato JSON.
 */
public class JsonSaveSystem implements SaveSystem {
    private static final String FILE_PATH = "savegame.json";

    /**
     * Compone manualmente un file JSON formattato a partire dall'oggetto SaveData
     * e lo scrive nel filesystem.
     */
    @Override
    public void save(SaveData data) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"name\": \"").append(data.name).append("\",\n");
            sb.append("  \"level\": ").append(data.level).append(",\n");
            sb.append("  \"hp\": ").append(data.hp).append(",\n");
            sb.append("  \"maxHp\": ").append(data.maxHp).append(",\n");
            sb.append("  \"attack\": ").append(data.attack).append(",\n");
            sb.append("  \"xp\": ").append(data.xp).append(",\n");
            sb.append("  \"xpToNextLevel\": ").append(data.xpToNextLevel).append(",\n");
            sb.append("  \"kills\": ").append(data.kills).append(",\n");
            sb.append("  \"highScore\": ").append(data.highScore).append(",\n");

            sb.append("  \"skills\": [");
            if (data.skills != null) {
                for (int i = 0; i < data.skills.size(); i++) {
                    SaveData.SkillSaveData s = data.skills.get(i);
                    sb.append("{\"name\": \"").append(s.name).append("\", \"uses\": ").append(s.remainingUses).append("}");
                    if (i < data.skills.size() - 1) sb.append(", ");
                }
            }
            sb.append("]\n}");

            Files.writeString(Paths.get(FILE_PATH), sb.toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Legge il file JSON, ne estrapola i valori grezzi e ricostruisce l'oggetto SaveData.
     */
    @Override
    public SaveData load() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) return null;
            String json = Files.readString(Paths.get(FILE_PATH));

            SaveData data = new SaveData();
            data.name = extractString(json, "\"name\"\\s*:\\s*\"(.*?)\"");
            data.level = extractInt(json, "\"level\"\\s*:\\s*(\\d+)");
            data.hp = extractInt(json, "\"hp\"\\s*:\\s*(\\d+)");
            data.maxHp = extractInt(json, "\"maxHp\"\\s*:\\s*(\\d+)");
            data.attack = extractInt(json, "\"attack\"\\s*:\\s*(\\d+)");
            data.xp = extractInt(json, "\"xp\"\\s*:\\s*(\\d+)");
            data.xpToNextLevel = extractInt(json, "\"xpToNextLevel\"\\s*:\\s*(\\d+)");
            data.kills = extractInt(json, "\"kills\"\\s*:\\s*(\\d+)");
            data.highScore = extractInt(json, "\"highScore\"\\s*:\\s*(\\d+)");

            data.skills = new ArrayList<>();
            Matcher arrayMatcher = Pattern.compile("\"skills\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL).matcher(json);
            if (arrayMatcher.find()) {
                Matcher objMatcher = Pattern.compile("\\{\"name\"\\s*:\\s*\"(.*?)\",\\s*\"uses\"\\s*:\\s*(\\d+)\\}").matcher(arrayMatcher.group(1));
                while (objMatcher.find()) {
                    data.skills.add(new SaveData.SkillSaveData(objMatcher.group(1), Integer.parseInt(objMatcher.group(2))));
                }
            }
            return data;
        } catch (Exception e) { return null; }
    }

    /**
     * Metodo di supporto che estrae una stringa tramite Regex.
     */
    private String extractString(String json, String regex) {
        Matcher m = Pattern.compile(regex).matcher(json);
        return m.find() ? m.group(1) : "Eroe";
    }

    /**
     * Metodo di supporto che estrae un intero tramite Regex.
     */
    private int extractInt(String json, String regex) {
        Matcher m = Pattern.compile(regex).matcher(json);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}