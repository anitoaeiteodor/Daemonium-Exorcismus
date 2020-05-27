package com.daemonium_exorcismus.engine.core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyboardManager extends KeyAdapter {

    public static HashSet<Integer> keysPressed = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                keysPressed.add(KeyEvent.VK_W);
                break;
            case KeyEvent.VK_S:
                keysPressed.add(KeyEvent.VK_S);
                break;
            case KeyEvent.VK_A:
                keysPressed.add(KeyEvent.VK_A);
                break;
            case KeyEvent.VK_D:
                keysPressed.add(KeyEvent.VK_D);
                break;
            case KeyEvent.VK_F:
                keysPressed.add(KeyEvent.VK_F);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                keysPressed.remove(KeyEvent.VK_W);
                break;
            case KeyEvent.VK_S:
                keysPressed.remove(KeyEvent.VK_S);
                break;
            case KeyEvent.VK_A:
                keysPressed.remove(KeyEvent.VK_A);
                break;
            case KeyEvent.VK_D:
                keysPressed.remove(KeyEvent.VK_D);
                break;
            case KeyEvent.VK_F:
                keysPressed.remove(KeyEvent.VK_F);
                break;
        }
    }
}
