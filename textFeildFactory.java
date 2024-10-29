
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class textFeildFactory {
    public static JTextField create(String toolTip, int x, int y, int width, int height) {
        JTextField feild = new JTextField();

        feild.setBackground(new Color(51, 51, 51));
        feild.setForeground(Color.WHITE);
        feild.setCaretColor(feild.getForeground());

        feild.setBounds(x, y, width, height);

        Border paddingBorder = new EmptyBorder(0, 4, 0, 4);

        feild.setBorder(paddingBorder);

        feild.setToolTipText(toolTip);

        return feild;
    }
}