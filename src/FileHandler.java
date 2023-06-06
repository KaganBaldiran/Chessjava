import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    FileHandler() {

    }

    public static void WriteGameDataToJSON(String Arraykey, String filename, String data, String SubElementKey) {

        String filecontent = null;
        try {
            filecontent = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = new JSONObject(filecontent);
        JSONArray jsonArray = jsonObject.getJSONArray(Arraykey);


        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);

            if (!object.getString(SubElementKey).isEmpty())
            {
                object.put(SubElementKey, data);
                break;
            }
        }

        jsonObject.put("GameData",jsonArray);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonObject.toString());
            System.out.println("Game meta data has been written successfully!");
        } catch (IOException e) {
            System.out.println("Error writing the game meta data!");
            e.printStackTrace();
        }
    }

    public static String ReadDataFromJSONFile(String Arraykey,String filename, String DataName) {
        try {
            String filecontent = new String(Files.readAllBytes(Paths.get(filename)));

            JSONObject FileObject = new JSONObject(filecontent);
            JSONArray jsonArray = FileObject.getJSONArray(Arraykey);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has(DataName)) {
                    return jsonObject.getString(DataName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the game meta data! :: key{ " + DataName + " }");
            e.printStackTrace();
        }
        return filename;
    }
}
