package com.duoc.inscripciones.s3.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.duoc.inscripciones.model.Inscripcion;
import com.duoc.inscripciones.s3.model.Asset;

public interface AwsService {

    String getS3FileContent(String bucketName, String fileName) throws IOException;

    List<Asset> getS3Files(String bucketName)throws IOException;

    byte[] downloadFile(String bucketName, String fileName) throws IOException;

    void moveObject(String bucketName, String fileKey, String destinationFileKey);

    void deleteObject(String bucketName, String fileKey);

    String uploadFile(String bucketName, String fileKey, MultipartFile file);

    String subirInscripcion(String bucketName, Inscripcion inscripcion);

    void validarBucket(String bucketName);
}
