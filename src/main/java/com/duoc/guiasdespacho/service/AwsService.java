package com.duoc.guiasdespacho.service;

import com.duoc.guiasdespacho.model.Asset;
import com.duoc.guiasdespacho.model.Guia;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AwsService {

    String getS3FileContent(String bucketName, String fileName) throws IOException;

    List<Asset> getS3Files(String bucketName)throws IOException;

    byte[] downloadFile(String bucketName, String fileName) throws IOException;

    void moveObject(String bucketName, String fileKey, String destinationFileKey);

    void deleteObject(String bucketName, String fileKey);

    String uploadFile(String bucketName, String fileKey, MultipartFile file);

    void subirGuia(String bucketName, Guia guia);

    void eliminarGuia(String bucketName, Guia guia);

    void validarBucket(String bucketName);

    Asset buscarGuia(String bucketName, Guia guia) throws IOException;

}
