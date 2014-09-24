package tterrag.customthings.common.config;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import tterrag.core.common.util.IOUtils;
import tterrag.customthings.CustomThings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonConfigReader<T>
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser parser = new JsonParser();
    private static final String KEY = "data";

    private File file;
    private Class<T> clazz;
    private JsonObject root;

    public JsonConfigReader(String fullFileName, Class<T> objClass)
    {
        this(new File(fullFileName), objClass);
    }

    public JsonConfigReader(File file, Class<T> objClass)
    {
        this.file = file;
        this.clazz = objClass;
        
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            IOUtils.copyFromJar(CustomThings.class, "customthings/misc/" + file.getName(), file);
        }
        
        refresh();
    }

    public JsonObject parseFile()
    {
        try
        {
            return parser.parse(new FileReader(file)).getAsJsonObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void refresh()
    {
        this.root = parseFile();
    }

    public List<T> getElements()
    {
        JsonArray elements = root.get(KEY).getAsJsonArray();
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < elements.size(); i++)
        {
            System.out.println("Loading element: " + elements.get(i).toString());
            list.add(gson.fromJson(elements.get(i), clazz));
        }
        return list;
    }
}
