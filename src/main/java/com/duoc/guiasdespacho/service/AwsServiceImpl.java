package com.duoc.guiasdespacho.service;

import com.amazonaws.services.s3.AmazonS3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.guiasdespacho.repository.S3Repository;
import com.amazonaws.util.StringUtils;
import com.duoc.guiasdespacho.exception.ResourceNotFoundException;
import com.duoc.guiasdespacho.model.Asset;
import com.duoc.guiasdespacho.model.Guia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.duoc.guiasdespacho.config.CONSTANTS.*;

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

    public void subirGuia(String bucketName, Guia guia){
        String fileKey = generarFileKeyGuia(guia);
        s3Repository.subirGuia(bucketName, fileKey, guia);
    }

    public void eliminarGuia(String bucketName, Guia guia){
        String fileKey = generarFileKeyGuia(guia);
        if(s3Client.doesObjectExist(bucketName, fileKey)){
            s3Repository.deleteObject(bucketName, fileKey);
        }
    }
    
    private String generarFileKeyGuia(Guia guia){
        //Se formatea fecha de despacho para nombre de directorio en bucket S3
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFOLDERFORMAT);
        String fecha = guia.getFecha().format(formatter);

        //Se obtiene ID de transportista
        int transportista = guia.getTransportista().getId();
        
        //ID de guia
        int id = guia.getId();

        //Se obtiene el fileKey de la guia de despacho usando las variables anteriores
        String fileKey = String.format("%s/transportista%d/guia%d.txt", fecha, transportista, id);

        return fileKey;
    }

    public void validarBucket(String bucketName) throws ResourceNotFoundException{
        if(!s3Client.doesBucketExistV2(bucketName)){
            throw new ResourceNotFoundException("No existe el bucket con nombre: " + bucketName);
        }
    }

    public Asset buscarGuia(String bucketName, Guia guia) throws IOException{
        String fileKey = generarFileKeyGuia(guia);
        return s3Repository.getObjectInfo(bucketName, fileKey);
    }
}
