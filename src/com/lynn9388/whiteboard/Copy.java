/*
 * Copyright (C) 2016  Lynn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lynn9388.whiteboard;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Copy {
    Copy() {
        final Frame frame = new Frame("Copy");
        frame.setBounds(50, 50, 300, 150);
        frame.setLayout(new GridLayout(3, 1));
        // Frame创建的界面点击close图标无法关闭，
        // 需要添加窗口监听器使其能关闭
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Panel panelA = new Panel();
        Panel panelB = new Panel();
        Panel panelC = new Panel();
        final Label label = new Label("clipboard");
        final TextField textField = new TextField(30);
        Button button = new Button("OK");
        // 为Button对象绑定监听器，在触发后得到TextField的内容
        // 将文本设置成Label对象的内容
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(textField.getText());
            }
        });

        frame.add(panelA);
        frame.add(panelB);
        frame.add(panelC);
        panelA.add(label);
        panelB.add(textField);
        panelC.add(button);

        frame.setVisible(true);
    }

    public static void main(String args[]) {
        new Copy();
    }
}