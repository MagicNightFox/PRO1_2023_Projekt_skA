package cz.uhk.gui;

import cz.uhk.models.ShoppingItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewItemDialog extends JDialog {
    private MainFrame parent;

    private JTextField inputName, inputPrice, inputCount;

    public NewItemDialog(MainFrame parent){
        super(parent, "Zadejte novou položku", true);
        this.parent = parent;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initGui();  // volat před visible true
        pack(); // velikost dialogu podle obsahu
        setVisible(true);
    }
    private void initGui(){
        JPanel mainPanel = new JPanel(new GridLayout(4,2));

        mainPanel.add(new JLabel("Název"));
        inputName = new JTextField(15);
        mainPanel.add(inputName);

        mainPanel.add(new JLabel("Cena"));
        inputPrice = new JTextField(15);
        mainPanel.add(inputPrice);

        mainPanel.add(new JLabel("Počet"));
        inputCount = new JTextField(15);
        mainPanel.add(inputCount);

        mainPanel.add(new JPanel());// prázdná pozice

        JButton btnOk =new JButton("Ok");
        mainPanel.add(btnOk);

        btnOk.addActionListener(e -> {
            var validationMessages = validateInputs();
            if(validationMessages.size()>0){
                StringBuilder errorMsg = new StringBuilder();
                for (String s : validationMessages) {
                    errorMsg.append(s).append("\n");
                }
                JOptionPane.showMessageDialog(this,
                        errorMsg.toString(),
                        "Chyba vstupu",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            System.out.println("validace nové položky OK");

            double price = Double.parseDouble(inputPrice.getText().trim());
            int count = Integer.parseInt(inputCount.getText().trim());

            ShoppingItem newItem = new ShoppingItem(
                    inputName.getText(),
                    price,
                    count
                    );
            parent.addNewItem(newItem);
            dispose(); // zahodíme dialog
        });
        add(mainPanel);
    }
    private List<String> validateInputs(){
        List<String> errors = new ArrayList<>();
        if(inputName.getText().length()<1){
            errors.add("Název je povinný");
        }
        if(inputCount.getText().length()<1){
            errors.add("Počet je povinný");
        }
        else{
            try{
                int count = Integer.parseInt(inputCount.getText().trim());
            }catch(NumberFormatException e){
                errors.add("Počet je ve špatném formátu");
            }
        }
        if(inputPrice.getText().length()<1){
            errors.add("Cena je povinná");
        }
        else{
            try{
                double price = Double.parseDouble(inputPrice.getText().trim());
            }catch (NumberFormatException e){
                errors.add("Cena je ve špatném formátu");
            }
        }
        //TODO validovat i počet
        return errors;
    }
}
