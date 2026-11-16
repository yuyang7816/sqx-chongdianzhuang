package com.sqx.modules.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class ImageToBase64 {


    // 在调用getImageAsBase64前添加以下代码
    static {
        try {
            // 创建信任所有证书的信任管理器
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            // 安装信任管理器
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 允许所有主机名验证
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertImageUrlToBase64(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP请求失败，状态码: " + responseCode);
        }

        String contentType = connection.getContentType();
        String mimeType = contentType != null ? contentType.split(";")[0] : "image/jpeg";

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 使用缓冲区读取输入流（兼容Java 8）
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageData = outputStream.toByteArray();

            String base64Encoded = Base64.getEncoder().encodeToString(imageData);
            return "data:" + mimeType + ";base64," + base64Encoded;
        }
    }

    private static String formatAsJson(String base64Image) {
        return "\"base64\":\"" + base64Image + "\"";
    }


    public static void main(String[] args) {
        // 请替换为你的图片URL
        String imageUrl = "https://tu.mianyazuju.com/20250528/b1a12a879b4a42bfb79ac587bf9920f0.jpg";
        try {
            String base64Image = convertImageUrlToBase64(imageUrl);
            String s="d:/converted_image.jpg";
            Base64ImageUtils.convertBase64ToImage(base64Image,s);
            System.out.println(formatAsJson(base64Image));
        } catch (IOException e) {
            System.err.println("处理图片时出错: " + e.getMessage());
        }
    }

}
