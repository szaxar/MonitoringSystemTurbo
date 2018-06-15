package monitoringsystemturbo.utils;

import javafx.embed.swing.SwingFXUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

public class IconConverter {

    public static javafx.scene.image.Image iconToFxImage(Icon icon) {
        BufferedImage bufferedImage = imageToBufferedImage(iconToImage(icon));
        javafx.scene.image.Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        return image;
    }

    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        else {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            GraphicsEnvironment graphicsEnvironment =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
            GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
            BufferedImage image = graphicsConfiguration.createCompatibleImage(iconWidth, iconHeight);
            Graphics2D graphics = image.createGraphics();
            icon.paintIcon(null, graphics, 0, 0);
            graphics.dispose();
            return image;
        }
    }


    public static BufferedImage imageToBufferedImage(Image image) {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bufferedImage = null;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;

            if (hasAlpha == true)
                transparency = Transparency.BITMASK;

            // Create the buffered image
            GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
            GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();

            bufferedImage = graphicsConfiguration.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        } //No screen

        if (bufferedImage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;

            if (hasAlpha == true) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics graphics = bufferedImage.createGraphics();

        // Paint the image onto the buffered image
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return bufferedImage;
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage)
            return ((BufferedImage) image).getColorModel().hasAlpha();

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        return pixelGrabber.getColorModel().hasAlpha();
    }
}
