package com.daemonium_exorcismus.engine.input;

import com.daemonium_exorcismus.engine.utils.Vec2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Queue;

public class MouseManager implements MouseListener {

    public static Vec2D mousePos = new Vec2D(-1, -1);

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
//        System.out.println("Mouse pressed at: " + mouseEvent.getX() + " " + mouseEvent.getY());
        mousePos = new Vec2D(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
//        if (mousePos.size() != 0) {
//            mousePos.remove(0);
//        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
