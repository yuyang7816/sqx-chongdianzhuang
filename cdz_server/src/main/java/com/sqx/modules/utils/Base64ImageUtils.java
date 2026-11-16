package com.sqx.modules.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 * Base64与图片互转工具类
 */
public class Base64ImageUtils {

    /**
     * 将Base64字符串转换为图片并保存到指定路径
     * @param base64Image 包含Data URI前缀的Base64字符串 (例如: data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD...)
     * @param outputPath 输出图片文件路径
     * @throws IOException 如果保存图片时发生错误
     */
    public static void convertBase64ToImage(String base64Image, String outputPath) throws IOException {
        // 去除Data URI前缀 (例如: data:image/jpeg;base64,)
        String pureBase64 = extractBase64Data(base64Image);

        // 解码Base64数据
        byte[] imageData = Base64.getDecoder().decode(pureBase64);

        // 确定图片格式
        String formatName = determineImageFormat(base64Image);

        // 保存图片
        saveImage(imageData, outputPath, formatName);
    }

    /**
     * 将图片文件转换为Base64字符串
     * @param imagePath 图片文件路径
     * @return 包含Data URI前缀的Base64字符串
     * @throws IOException 如果读取图片时发生错误
     */
    public static String convertImageToBase64(String imagePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(imagePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // 使用缓冲区读取文件（兼容Java 8）
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageData = baos.toByteArray();

            // 编码为Base64
            String base64Encoded = Base64.getEncoder().encodeToString(imageData);

            // 确定图片MIME类型
            String mimeType = determineMimeTypeFromExtension(imagePath);

            // 添加Data URI前缀
            return "data:" + mimeType + ";base64," + base64Encoded;
        }
    }

    /**
     * 从包含Data URI前缀的Base64字符串中提取纯Base64数据
     */
    private static String extractBase64Data(String base64Image) {
        int commaIndex = base64Image.indexOf(',');
        if (commaIndex != -1) {
            return base64Image.substring(commaIndex + 1);
        }
        return base64Image;
    }

    /**
     * 从Data URI前缀确定图片格式
     */
    private static String determineImageFormat(String base64Image) {
        // 默认JPEG
        String format = "jpg";

        // 查找MIME类型
        int startIndex = base64Image.indexOf("data:image/");
        if (startIndex != -1) {
            int endIndex = base64Image.indexOf(';', startIndex);
            if (endIndex != -1) {
                String mimeType = base64Image.substring(startIndex + 11, endIndex);
                switch (mimeType.toLowerCase()) {
                    case "png":
                        format = "png";
                        break;
                    case "gif":
                        format = "gif";
                        break;
                    case "bmp":
                        format = "bmp";
                        break;
                    case "webp":
                        format = "webp";
                        break;
                }
            }
        }

        return format;
    }

    /**
     * 从文件扩展名确定MIME类型
     */
    private static String determineMimeTypeFromExtension(String filePath) {
        String lowerCasePath = filePath.toLowerCase();
        if (lowerCasePath.endsWith(".png")) {
            return "image/png";
        } else if (lowerCasePath.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCasePath.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerCasePath.endsWith(".webp")) {
            return "image/webp";
        }
        // 默认JPEG
        return "image/jpeg";
    }

    /**
     * 保存图片数据到文件
     */
    private static void saveImage(byte[] imageData, String outputPath, String formatName) throws IOException {
        // 确保目录存在
        File file = new File(outputPath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("无法创建目录: " + parentDir.getAbsolutePath());
            }
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            // 尝试使用ImageIO写入（保持图片格式）
            BufferedImage image = ImageIO.read(bis);
            if (image != null) {
                ImageIO.write(image, formatName, fos);
                return;
            }

            // 如果ImageIO无法解析，直接写入字节数据
            System.out.println("警告: 无法确定图片格式，直接写入原始数据");
            fos.write(imageData);
        }
    }


}