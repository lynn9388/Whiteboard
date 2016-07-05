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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Whiteboard extends JFrame implements Serializable {
    String command = "Pen";
    WhiteboardPanel panel;

    public static void main(String[] args) {
        // 由于Swing不是线程安全的，所以要在事件调度线程上处理
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Whiteboard whiteboard = new Whiteboard();
                whiteboard.createAndShow();
            }
        });
    }

    private void createAndShow() {
        this.setFont(new Font("Serif", Font.ITALIC, 80));
        this.setTitle("Whiteboard          By Lynn");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(50, 50, 700, 400);

        // 为JFrame增加一个工具栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu figureMenu = new JMenu("Figure");
        JMenu toolMenu = new JMenu("Tool");
        JMenuItem exitMenu = new JMenuItem("Exit");
        JMenuItem lineMenu = new JMenuItem("Line");
        JMenuItem circleMenu = new JMenuItem("Circle");
        JMenuItem ovalMenu = new JMenuItem("Oval");
        JMenuItem rectangleMenu = new JMenuItem("Rectangle");
        JMenuItem arcMenu = new JMenuItem("Arc");
        JMenuItem penMenu = new JMenuItem("Pen");
        // 为所有的Menu绑定ActionListener监听器，使鼠标操作知道应进行的操作
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = e.getActionCommand();

                if ("Exit".equals(command)) {
                    dispose();
                }
            }
        };
        exitMenu.addActionListener(actionListener);
        lineMenu.addActionListener(actionListener);
        circleMenu.addActionListener(actionListener);
        ovalMenu.addActionListener(actionListener);
        rectangleMenu.addActionListener(actionListener);
        arcMenu.addActionListener(actionListener);
        penMenu.addActionListener(actionListener);
        fileMenu.add(exitMenu);
        figureMenu.add(lineMenu);
        figureMenu.add(circleMenu);
        figureMenu.add(ovalMenu);
        figureMenu.add(rectangleMenu);
        figureMenu.add(arcMenu);
        toolMenu.add(penMenu);
        menuBar.add(fileMenu);
        menuBar.add(figureMenu);
        menuBar.add(toolMenu);
        this.setJMenuBar(menuBar);

        // JPanel为Whiteboard的画图区域，限制画图范围
        panel = new WhiteboardPanel(this);
        this.add(panel);

        // 直接在JPanel上难以进行文字输入，使用JTextFiled，并绑定KeyAdapter监听器
        // 在图形界面任意时刻按下键盘，接受文字，按下ENTER键显示在Whiteboard上
        final JTextField textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String str = textField.getText();
                    FontMetrics fontMetrics = panel.getFontMetrics(panel
                            .getFont());
                    int height = fontMetrics.getHeight();
                    for (char ch : str.toCharArray()) {
                        int width = fontMetrics.charWidth(ch);
                        String str1 = "" + ch;
                        panel.graphics.drawString(str1,
                                panel.mouseOperate.getX(),
                                panel.mouseOperate.getY());
                        panel.repaint();
                        // 更改字符串显示的起始位置横坐标
                        panel.mouseOperate.setX(panel.mouseOperate.getX()
                                + width);
                        // 一行字符满后换行
                        if (panel.mouseOperate.getX() > panel.image.getWidth()) {
                            panel.mouseOperate.setX(0);
                            panel.mouseOperate.setY(panel.mouseOperate.getY()
                                    + height);
                        }
                    }
                    textField.setText("");
                }
            }
        });
        this.add(textField, BorderLayout.SOUTH);
        this.setVisible(true);
    }
}

class WhiteboardPanel extends JPanel {
    public Graphics2D graphics;
    BufferedImage image = null;
    MouseOperate mouseOperate;
    Whiteboard whiteboard;

    public WhiteboardPanel(Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
        // 为JPanel绑定MouseAdaptr监听器
        mouseOperate = new MouseOperate(whiteboard);
        this.addMouseListener(mouseOperate);
        this.addMouseMotionListener(mouseOperate);
        // 使用BufferedImage保存图像
        image = new BufferedImage(whiteboard.getWidth(),
                whiteboard.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
    }

    public void paint(Graphics g) {
        super.paint(g);
        // 将BufferedImage转换成Graphics2d，对其进行绘图
        graphics = image.createGraphics();
        // 设置线条颜色
        graphics.setColor(Color.BLACK);
        // 对图像的线条去锯齿
        // graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 在JPanel上绘制图像,同时设置背景色
        g.drawImage(image, 0, 0, Color.WHITE, this);
    }
}

