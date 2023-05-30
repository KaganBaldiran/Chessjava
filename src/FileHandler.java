import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler
{

    FileHandler()
    {

    }

    public static void WriteGameDataToJSON(String filename , String data , String Key)
    {
        JSONObject ChessData = new JSONObject();
        ChessData.put(Key , data);

        try(FileWriter file = new FileWriter(filename))
        {
            file.write(ChessData.toString());
            System.out.println("Game meta data has been written successfully!");
        }
        catch (IOException e)
        {
            System.out.println("Error writing the game meta data!");
            e.printStackTrace();
        }
    }

    public static String ReadDataFromJSONFile(String filename , String DataName)
    {
        try
        {
            String filecontent = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject ChessData = new JSONObject(filecontent);
            return ChessData.get(DataName).toString();
        }
        catch (IOException e)
        {
            System.out.println("Error reading the game meta data! :: key{ " + DataName + " }" );
            e.printStackTrace();
        }
        return filename;
    }

}
