package com.TechMoveSystems.urbango.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final int MAX_DIMENSION = 1400;
    private static final float JPEG_QUALITY = 0.8f;

    public String storeResized(MultipartFile file, String subFolder, String prefix) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String baseDir = System.getProperty("app.files.base-dir", "photos");
        Path dir = Path.of(baseDir, subFolder);
        Files.createDirectories(dir);

        String extension = resolveExtension(file.getOriginalFilename());
        String filenamePrefix = (prefix != null && !prefix.isBlank()) ? prefix + "-" : "";
        String filename = filenamePrefix + UUID.randomUUID() + "." + extension;
        Path target = dir.resolve(filename);

        saveResizedImage(file, target, extension);
        return subFolder + "/" + filename;
    }

    private String resolveExtension(String name) {
        if (name != null) {
            int idx = name.lastIndexOf('.');
            if (idx >= 0 && idx < name.length() - 1) {
                String ext = name.substring(idx + 1).toLowerCase(Locale.ROOT);
                if (!ext.isBlank()) {
                    return ext;
                }
            }
        }
        return "jpg";
    }

    private void saveResizedImage(MultipartFile file, Path target, String extension) throws IOException {
        BufferedImage original;
        try (InputStream in = file.getInputStream()) {
            original = ImageIO.read(in);
        }

        if (original == null) {
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return;
        }

        BufferedImage processed = resizeIfNeeded(original);
        if (isJpeg(extension)) {
            processed = forceRgb(processed);
            writeJpeg(processed, target);
        } else {
            boolean written = ImageIO.write(processed, extension, target.toFile());
            if (!written) {
                ImageIO.write(processed, "png", target.toFile());
            }
        }
    }

    private BufferedImage resizeIfNeeded(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        if (width <= MAX_DIMENSION && height <= MAX_DIMENSION) {
            return input;
        }
        double scale = Math.min((double) MAX_DIMENSION / width, (double) MAX_DIMENSION / height);
        int newWidth = Math.max(1, (int) Math.round(width * scale));
        int newHeight = Math.max(1, (int) Math.round(height * scale));
        int type = input.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage output = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g2 = output.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(input, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        return output;
    }

    private boolean isJpeg(String extension) {
        String lower = extension.toLowerCase(Locale.ROOT);
        return lower.equals("jpg") || lower.equals("jpeg");
    }

    private BufferedImage forceRgb(BufferedImage image) {
        if (!image.getColorModel().hasAlpha() && image.getType() == BufferedImage.TYPE_INT_RGB) {
            return image;
        }
        BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return rgb;
    }

    private void writeJpeg(BufferedImage image, Path target) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            ImageIO.write(image, "jpg", target.toFile());
            return;
        }
        ImageWriter writer = writers.next();
        try (OutputStream os = Files.newOutputStream(target);
             ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(JPEG_QUALITY);
            }
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }
}
