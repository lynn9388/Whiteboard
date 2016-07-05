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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseOperate extends MouseAdapter {
    int x1, y1, x2, y2;
    Whiteboard whiteboard;
    private String command;
    private Boolean pressed = false;

    public MouseOperate(Whiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.command = whiteboard.command;
        // 根据鼠标在短时间内接受的大量坐标值绘制随意线条
        if ("Pen".equals(command)) {
            if (!pressed) {
                x1 = e.getX();
                y1 = e.getY();
                pressed = true;
            } else {
                x2 = e.getX();
                y2 = e.getY();
                whiteboard.panel.graphics.drawLine(x1, y1, x2, y2);
                x1 = x2;
                y1 = y2;
            }
            pressed = true;
        }
        whiteboard.panel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        pressed = false;
        this.command = whiteboard.command;

        // 一系列图形绘制操作
        if ("Line".equals(command)) {
            whiteboard.panel.graphics.drawLine(x1, y1, x2, y2);
        } else if ("Circle".equals(command)) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            int z = Math.min(Math.abs(x2 - x1), Math.abs(y2 - y1));
            whiteboard.panel.graphics.drawOval(x, y, z, z);
        } else if ("Oval".equals(command)) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            whiteboard.panel.graphics.drawOval(x, y, Math.abs(x2 - x1),
                    Math.abs(y2 - y1));
        } else if ("Rectangle".equals(command)) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            whiteboard.panel.graphics.drawRect(x, y, Math.abs(x2 - x1),
                    Math.abs(y2 - y1));
        } else if ("Arc".equals(command)) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            whiteboard.panel.graphics.drawArc(x, y, Math.abs(x2 - x1),
                    Math.abs(y2 - y1), 45, 135);
        }
        whiteboard.panel.repaint();
        x1 = x2;
        y1 = y2;
    }

    public int getX() {
        return x1;
    }

    public void setX(int x) {
        x1 = x;
    }

    public int getY() {
        return y1;
    }

    public void setY(int y) {
        y1 = y;
    }
}
