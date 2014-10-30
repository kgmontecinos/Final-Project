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
public class ScreenWidth {
    public ScreenWidth(){
    }
     public Dimension getScreenWidth(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        return dimension;
     }
}
