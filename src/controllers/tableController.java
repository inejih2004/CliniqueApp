package controllers;
import views.tableView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class tableController {
    private tableView tv;
    public tableController(tableView tv){
        this.tv = tv;
        tv.addCloseFrameListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableController tc = new tableController(tv);
                tv.closeFrame();
            }
        });
    }


}
