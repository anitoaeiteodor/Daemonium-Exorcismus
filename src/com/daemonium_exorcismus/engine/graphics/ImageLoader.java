package com.daemonium_exorcismus.engine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.System.exit;

public class ImageLoader
{

    public static BufferedImage LoadImage(String path)
    {
        try
        {
            /// Clasa ImageIO contine o serie de metode statice pentru file IO.
            /// Metoda read() are ca argument un InputStream construit avand ca referinta
            /// directorul res, director declarat ca director de resurse in care se gasesc resursele
            /// proiectului sub forma de fisiere sursa.
            return ImageIO.read(new FileInputStream(path));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }}

