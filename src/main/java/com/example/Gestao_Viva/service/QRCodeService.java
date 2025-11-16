package com.example.Gestao_Viva.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

    /**
     * @param texto 
     * @param largura 
     * @param altura 
     * @return 
     */
    public String gerarQRCodeBase64(String texto, int largura, int altura) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            
            return Base64.getEncoder().encodeToString(pngData);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code: " + e.getMessage());
        }
    }
}