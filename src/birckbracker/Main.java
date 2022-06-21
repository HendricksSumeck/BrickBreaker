package birckbracker;

import javax.swing.JFrame;

public class Main {
    private static final StringMessage _stringMessage = new StringMessage();

    public static void main(String[] args) {
        var obj = new JFrame();
        var gamePlay = new GamePlay();

        obj.setBounds(10, 10, 700, 600);
        obj.setTitle(_stringMessage.GameName);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
    }
}
