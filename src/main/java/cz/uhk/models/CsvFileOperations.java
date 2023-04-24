package cz.uhk.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileOperations implements FileOperations{
    private final static String FILE_NAME = "./shoppingList.csv";
    private final static char DELIMETER = ';';

    @Override
    public ShoppingList load() {
        //TODO load from file - 25.4.


        ShoppingList shoppingList = new ShoppingList();

        try{
            BufferedReader buffReader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line= buffReader.readLine()) != null){
                //System.out.println(line);
                String[] parts = line.split(Character.toString(DELIMETER));
                //System.out.println(parts);
                if(parts.length <= 1){
                    shoppingList.setName(parts[0]);
                }
                else{
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int pieces = Integer.parseInt(parts[2]);
                    boolean isBought = Boolean.parseBoolean(parts[3]);
                    ShoppingItem item = new ShoppingItem(name, price, pieces);
                    item.setBought(isBought);
                    shoppingList.addItem(item);
                }


            }
            return shoppingList;
        }catch(IOException e){
            e.printStackTrace();
        }

        return new ShoppingList();
    }

    @Override
    public void write(ShoppingList model) {
        StringBuilder csvText = new StringBuilder();
        csvText.append("name: "+model.getName()).append("\n");

        for (ShoppingItem item:
             model.getItems()) {
            csvText
                    .append(item.getName())
                    .append(DELIMETER)
                    .append(item.getPrice())
                    .append(DELIMETER)
                    .append(item.getPieces())
                    .append(DELIMETER)
                    .append(item.isBought())
                    .append("\n");
        }
        try{
            FileWriter writer = new FileWriter(FILE_NAME);
            writer.write(csvText.toString());
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
