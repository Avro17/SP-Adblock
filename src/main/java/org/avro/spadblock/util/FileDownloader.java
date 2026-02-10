package org.avro.spadblock.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.avro.spadblock.SPAdblock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDownloader {

    public void forceDownload(String url, File destination) throws IOException {
        SPAdblock.LOGGER.info("Force downloading {} from {}", destination.getName(), url);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(url);
            getRequest.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            getRequest.addHeader("Pragma", "no-cache");
            getRequest.addHeader("Expires", "0");

            HttpResponse response = client.execute(getRequest);
            HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Server responded with status code: " + statusCode);
            }

            if (entity != null) {
                destination.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(destination)) {
                    entity.writeTo(fos);
                    SPAdblock.LOGGER.info("Successfully downloaded and replaced {}.", destination.getName());
                }
            } else {
                throw new IOException("No content received from " + url);
            }
        } catch (Exception e) {
            throw new IOException("Failed to download file from URL: " + url, e);
        }
    }
}
