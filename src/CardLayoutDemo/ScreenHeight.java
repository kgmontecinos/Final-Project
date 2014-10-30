/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CardLayoutDemo;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author kgmontecinos92
 */
public class ScreenHeight {
    public ScreenHeight(){
    }
    
    private int getScreenHeight(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        int screenHeight = dimension.height;
        return screenHeight;
     }
}
