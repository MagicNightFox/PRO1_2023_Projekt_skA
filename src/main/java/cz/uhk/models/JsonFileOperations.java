package cz.uhk.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

public class JsonFileOperations implements FileOperations{
    private Gson gson;
    private static final String FILE_NAME = "./shoppingList.json";
    public JsonFileOperations(){
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public ShoppingList load() {
        try {
            Type targetType = new TypeToken<ShoppingList>(){}.getType();

            FileReader reader = new FileReader(FILE_NAME);
            /* BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }
            reader.close();

            Type targetType = new TypeToken<ShoppingList>(){}.getType();

            ShoppingList shoppingList =
                    gson.fromJson(jsonText.toString(), targetType);*/

            ShoppingList shoppingList = gson.fromJson(reader, targetType);

            return shoppingList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ShoppingList();
    }

    @Override
    public void write(ShoppingList model) {
        String jsonText = gson.toJson(model);
        System.out.println(jsonText);

        try{
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
