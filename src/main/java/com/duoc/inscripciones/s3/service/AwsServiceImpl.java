package com.duoc.inscripciones.s3.service;

import com.amazonaws.services.s3.AmazonS3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.duoc.inscripciones.exception.ResourceNotFoundException;
import com.duoc.inscripciones.model.Inscripcion;
import com.duoc.inscripciones.s3.model.Asset;
import com.duoc.inscripciones.s3.repository.S3Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class AwsServiceImpl implements AwsService {

    private final AmazonS3 s3Client;

    private final static Logger log = LoggerFactory.getLogger(AwsServiceImpl.class);

    @Autowired
    private S3Repository s3Repository;

    AwsServiceImpl(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public List<Asset> getS3Files(String bucket){
        return s3Repository.listObjectsInBucket(bucket);
    }

    public String getS3FileContent(String bucketName, String fileName) throws IOException{
        return getAsString(s3Repository.getObject(bucketName, fileName));
    }

    private static String getAsString(InputStream is) throws IOException{
        if (is == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StringUtils.UTF8));
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        } finally {
            is.close();
        }
        return sb.toString();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) throws IOException{
        return s3Repository.downloadFile(bucketName, fileName);
    }

    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey){
        s3Repository.moveObject(bucketName, fileKey, destinationFileKey);
    }

    @Override
    public void deleteObject(String bucketName, String fileKey){
        s3Repository.deleteObject(bucketName, fileKey);
    }

    @Override
    public String uploadFile(String bucketName, String filePath, MultipartFile file){
        File fileObj = convertMultiPartFileToFile(file);
        //String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        return s3Repository.uploadFile(bucketName, filePath, fileObj);
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e){
            log.error("Error converting multipartfile to file", e);
        }
        return convertedFile;
    }

    @Override
    public String subirInscripcion(String bucketName, Inscripcion inscripcion){
        String fileKey = generarFileKeyInscripcion(inscripcion);
        s3Repository.subirInscripcion(bucketName, fileKey, inscripcion);
        return fileKey;
    }
    
    // Metodo que genera una clave (key) de S3 para un resumen de inscripcion
    // La clave se genera usando el ID del estudiante y el ID de la inscripcion
    private String generarFileKeyInscripcion(Inscripcion inscripcion){
        //Se obtiene ID del estudiante
        Long estudianteId = inscripcion.getEstudiante().getId();
        
        //ID de guia
        Long id = inscripcion.getId();

        //Se crea el fileKey del resumen de inscripcion usando las variables anteriores
        String fileKey = String.format("estudiante%d/inscripcion%d.txt", estudianteId, id);

        return fileKey;
    }

    // Metodo que valida que un bucket exista por su nombre
    // Si no existe, lanza una excepcion
    public void validarBucket(String bucketName) throws ResourceNotFoundException{
        if(!s3Client.doesBucketExistV2(bucketName)){
            throw new ResourceNotFoundException("No existe el bucket con nombre: " + bucketName);
        }
    }

    
}
