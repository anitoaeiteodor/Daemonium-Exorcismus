package com.daemonium_exorcismus.engine.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseListener;

/**
 * This is the game's window. Everything is happening here.
 */
public class GameWindow
{
    private JFrame  wndFrame;
    private String  wndTitle;
    private int     wndWidth;
    private int     wndHeight;

    private Canvas  canvas;

    public GameWindow(String title, int width, int height){
        wndTitle    = title;
        wndWidth    = width;
        wndHeight   = height;
        wndFrame    = null;
    }

    public void buildGameWindow()
    {
        if(wndFrame != null)
        {
            return;
        }
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setVisible(true);

        canvas = new Canvas();

        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));
        wndFrame.add(canvas);
        wndFrame.pack();
    }

    public void addListeners(KeyAdapter adapter, MouseListener listener) {
        System.out.println("Added adapter: " + adapter);
        System.out.println("Added listener: " + listener);
        canvas.addKeyListener(adapter);
        canvas.addMouseListener(listener);
        canvas.requestFocus();
    }

    public int getWndWidth()
    {
        return wndWidth;
    }

    public int getWndHeight()
    {
        return wndHeight;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
