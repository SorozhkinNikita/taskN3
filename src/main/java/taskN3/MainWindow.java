package taskN3;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        setTitle("Установка ПО");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800,600);
           String rez = JOptionPane.showInputDialog(
                null,
                "Введите сложность игры от 1 до 7:",
                "Сложность игры",
                JOptionPane.INFORMATION_MESSAGE
        );
        int diff = rez.charAt(0)-'0';
        if (diff >= 1 && diff <= 7)
            add(new GameField(diff));
        else
            JOptionPane.showMessageDialog(null, "Введена некорректная сложность");
        setVisible(true);
    }

    public static void main(String[] args) {
     MainWindow mw = new MainWindow();
        }
}