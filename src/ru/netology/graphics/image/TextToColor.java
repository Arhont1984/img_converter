package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class TextToColor implements TextGraphicsConverter {
    private double maxRatio;
    private int width;
    private int height;
//    GetTextColorSchema colorSchema = new GetTextColorSchema();
    TextColorSchema colorSchema = new GetTextColorSchema();

    //Вынес конвертер в отдельный метод, что бы не повторялся в теле основого метода convert
    private static void converter(BufferedImage image, TextColorSchema colorSchema, StringBuilder result) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                int brightness = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                char symbol = colorSchema.convert(brightness);
                result.append(symbol);
            }
            result.append("\n");
        }
    }

    //Ресайзер
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        URL urls = new URL(url);
        BufferedImage imageIncome = ImageIO.read(urls);
        StringBuilder result = new StringBuilder();

        if (imageIncome.getWidth() / imageIncome.getHeight() > maxRatio) {
            throw new BadImageSizeException(imageIncome.getWidth() / imageIncome.getHeight(), maxRatio);
        }
        if (imageIncome.getHeight() / imageIncome.getWidth() > maxRatio) {
            throw new BadImageSizeException(imageIncome.getHeight() / imageIncome.getWidth(), maxRatio);
        }

        if (imageIncome.getWidth() > width || imageIncome.getHeight() > height) {
            if (imageIncome.getWidth() > imageIncome.getHeight()) {
                float proportions = (float) imageIncome.getWidth() / width;
                int ImageNewWidth = (int) (imageIncome.getWidth() / proportions);
                int ImageNewHeight = (int) (imageIncome.getHeight() / proportions);
                BufferedImage image = resizeImage(imageIncome, ImageNewWidth * 3, ImageNewHeight);
                converter(image, colorSchema, result);
            }
            if (imageIncome.getHeight() > imageIncome.getWidth()) {
                float proportions = (float) imageIncome.getHeight() / height;
                int ImageNewWidth = (int) (imageIncome.getWidth() / proportions);
                int ImageNewHeight = (int) (imageIncome.getHeight() / proportions);
                BufferedImage image = resizeImage(imageIncome, ImageNewWidth * 3, ImageNewHeight);
                converter(image, colorSchema, result);
            }

        } else {
            BufferedImage image = resizeImage(imageIncome, imageIncome.getWidth() * 3, imageIncome.getHeight());
            converter(image, colorSchema, result);

        }

        return result.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;

    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;

    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.colorSchema = schema;

    }

}




