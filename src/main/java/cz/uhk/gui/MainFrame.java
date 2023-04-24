package cz.uhk.gui;

import cz.uhk.models.FileOperations;
import cz.uhk.models.ShoppingItem;
import cz.uhk.models.ShoppingList;
import cz.uhk.models.TableModel;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {
    private ShoppingList shoppingList;
    private TableModel tableModel;
    private FileOperations fileOperations;

    private JLabel lblSummaryInfo;

    public MainFrame(int width, int height, FileOperations fileOperations){
        super("PRO1 2023");
        this.fileOperations = fileOperations;
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        shoppingList = fileOperations.load();   // načtu data ze souboru
        //seedShoppingList(); // testovací data

        initMenu(); // volat před setVisible(true)
        initGui(); // volat před setVisible(true)

        setVisible(true);


        Runnable refreshDataFromInternet = ()->{
          try{
              while (true){
                  // načtu něco z internetu
                  System.out.println("načteno");
                  TimeUnit.SECONDS.sleep(5);
              }
          }catch (Exception ex){
              ex.printStackTrace();
          }
        };
        Thread refreshDataFromInternetThread = new Thread(refreshDataFromInternet);
        refreshDataFromInternetThread.start();
    }
    private void initMenu(){
        JMenuBar bar = new JMenuBar();
        JMenu menu1 = new JMenu("Seznam");
        bar.add(menu1);
        JMenuItem menuNewItem = new JMenuItem("Nová položka");
        menuNewItem.addActionListener(e -> {
            System.out.println("clicked new item");
            NewItemDialog dialog = new NewItemDialog(this);
        });
        menu1.add(menuNewItem);

        JMenuItem menuNewItem2 = new JMenuItem("Dialog ukázka 2");
        menuNewItem2.addActionListener(e -> {
            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            panel.add(new JLabel("Nějaký text"));
            panel.add(new JTextField(20));
            panel.add(new JButton("Tlačítko"));
            dialog.add(panel);
            dialog.pack();
            dialog.setVisible(true);
        });
        menu1.add(menuNewItem2);

        JMenuItem saveToFileItem = new JMenuItem("Uložit do souboru");
        saveToFileItem.addActionListener(e -> {
            System.out.println("save to file clicked");
            fileOperations.write(shoppingList);
            JOptionPane.showMessageDialog(this,
                    "Seznam úspěšně uložen",
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        menu1.add(saveToFileItem);

        JMenuItem loadFromFileItem = new JMenuItem("Načíst ze souboru");/*
        loadFromFileItem.addActionListener(e -> {
            Runnable loadData = ()->{
                try{
                    System.out.println("load from file clicked");
                    TimeUnit.SECONDS.sleep(5); // simulace toho, že to trvá
                    shoppingList.setItems(fileOperations.load().getItems());
                    JOptionPane.showMessageDialog(this,
                            "Seznam úspěšně načten",
                            "Ok",
                            JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            };
            Thread loadDataThread = new Thread(loadData);
            loadDataThread.start();
        });*/
        menu1.add(loadFromFileItem);

        setJMenuBar(bar);
    }
    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.add(initNorthPanel(), BorderLayout.NORTH);
        panelMain.add(initCenterPanel(), BorderLayout.CENTER);
        panelMain.add(initSouthPanel(), BorderLayout.SOUTH);
        add(panelMain);
    }
    private JPanel initNorthPanel(){
        JPanel panelNorth = new JPanel(new FlowLayout());
        panelNorth.add(new JLabel("Label 1"));
        JTextField input1 = new JTextField(20);
        panelNorth.add(input1);
        JButton btn1 = new JButton("Tlačítko");
        btn1.addActionListener(e -> System.out.println("Zadán text: " + input1.getText()));
        btn1.addActionListener(e -> {
            System.out.println("výpis 2");

            JOptionPane.showMessageDialog(null,
                    "Zpráva pro uživatele",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

        });

        panelNorth.add(btn1);
        return panelNorth;
    }
    private JPanel initCenterPanel() {
        JPanel panelCenter = new JPanel();
        /*Object[][] data = new Object[][] {
                {"0,0", "0,1", "0,2"},
                {"1,0", "1,1", "1,2"},
                {"2,0", "2,1", "2,2"}
        };
        String[] colNames = new String[] {"Col1", "Col2", "Col3"};
        JTable table = new JTable(data, colNames);*/
        tableModel = new TableModel(shoppingList);
        shoppingList.addActionListener(e -> {
            if(e.getID() != 1)
                return;
            tableModel.fireTableDataChanged();
        });
        JTable table = new JTable();
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panelCenter.add(scrollPane);

        return panelCenter;
    }
    private JPanel initSouthPanel(){
        JPanel panel = new JPanel();
        lblSummaryInfo = new JLabel("Info");
        shoppingList.addActionListener(e -> {
            updateSummaryInfo();
        });
        panel.add(lblSummaryInfo);
        updateSummaryInfo();
        return panel;
    }
    public void addNewItem(ShoppingItem newItem){
        System.out.println("nová položka");
        shoppingList.addItem(newItem);
    }
    private void seedShoppingList(){
        shoppingList.getItems().add(new ShoppingItem("Máslo",53,2));
        shoppingList.getItems().add(new ShoppingItem("Chléb",30,1));
        shoppingList.getItems().add(new ShoppingItem("Pomazánka",25,3));
        shoppingList.getItems().add(new ShoppingItem("Šunka",31,1));
        shoppingList.getItems().add(new ShoppingItem("Sýr",29,1));
    }

    private void updateSummaryInfo(){
        StringBuilder text = new StringBuilder();
        text.append("Počet položek: ");
        text.append(shoppingList.getItems().stream().count());

        text.append(", Celková počet ks: ");
        int countSum = 0;
        for (ShoppingItem item :
                shoppingList.getItems()) {
            countSum += item.getPieces();
        }
        text.append(countSum);
        lblSummaryInfo.setText(text.toString());
    }
}
