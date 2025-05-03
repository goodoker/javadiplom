package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private TextColorSchema schema;
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;

    public Converter() {
        schema = new ColorSchema();
    }

    public Converter(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));
        int width = img.getWidth();
        int height = img.getHeight();
        double ratio = (double) width / height;

        System.out.printf("Original Image: width=%d height=%d ratio=%.2f%n", width, height, ratio);
        System.out.printf("Max Constraints: width=%d height=%d ratio=%.2f%n", maxWidth, maxHeight, maxRatio);

        if (maxRatio > 0 && (ratio > maxRatio || (1/ratio) > maxRatio)) {
            throw new BadImageSizeException(
                    Math.round(ratio * 100) / 100d,
                    Math.round(maxRatio * 100) / 100d);
        }

        if ((maxHeight > 0 && height > maxHeight) || (maxWidth > 0 && width > maxWidth)) {
            double widthRatio = (maxWidth > 0) ? (double) maxWidth / width : Double.MAX_VALUE;
            double heightRatio = (maxHeight > 0) ? (double) maxHeight / height : Double.MAX_VALUE;
            double scale = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            System.out.printf("Resizing from %dx%d to %dx%d%n", width, height, newWidth, newHeight);
            img = resize(img, newWidth, newHeight);
            width = newWidth;
            height = newHeight;
        }


        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int rgb = img.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;


                int gray = (int) ((Byte.MIN_VALUE * 3 + red + green + blue) / 3);
                char c = schema.convert((byte) gray);


                sb.append(c).append(c);
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private BufferedImage resize(BufferedImage oldImage, int newWidth, int newHeight) {
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        newImage.createGraphics().drawImage(oldImage, 0, 0, newWidth, newHeight, null);
        newImage.createGraphics().dispose();
        return newImage;
    }
}
