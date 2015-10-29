package cuchaz.enigma.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author caellian
 */
public class Style {
    public static void loadDefaultStyle() {
        UIManager.put("nimbusFocus", NimbusLight.STYLE_NIMBUS_FOCUS);
        UIManager.put("nimbusBorder", NimbusLight.STYLE_NIMBUS_BORDER);
        UIManager.put("nimbusOrange", NimbusLight.STYLE_NIMBUS_FOCUS);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }

    public static class NimbusLight {
        public static final Color STYLE_NIMBUS_FOCUS = new Color(0, 41, 214);
        public static final Color STYLE_NIMBUS_BORDER = new Color(0, 21, 64);
    }
}
