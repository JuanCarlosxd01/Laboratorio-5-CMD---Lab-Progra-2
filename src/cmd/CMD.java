
package cmd;

import javax.swing.SwingUtilities;
public class CMD {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            consolaGUI consola = new consolaGUI();
            consola.setVisible(true);
        });
    }

}
