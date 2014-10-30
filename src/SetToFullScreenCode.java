/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kgmontecinos92
 */
import java.awt.Dimension;
import java.awt.Toolkit;
public class SetToFullScreenCode {
    
    public SetToFullScreenCode(){
    }
    
    public Dimension getFullScreen(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();
        return dimension;
    }
    
}
