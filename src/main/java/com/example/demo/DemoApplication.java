package com.example.demo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// Link para gerar o Qrcode
			String conteudoQrCode = "https://www.youtube.com/";

			String diretorioRaiz = System.getProperty("user.dir");
			String pastaSaida = "qrcodes_output";
			Path caminhoPastaSaida = Paths.get(diretorioRaiz, pastaSaida);

			if (!Files.exists(caminhoPastaSaida)) {
				Files.createDirectories(caminhoPastaSaida);
			}

			String nomeArquivo = UUID.randomUUID().toString() + ".png";
			Path caminhoArquivo = caminhoPastaSaida.resolve(nomeArquivo);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			BitMatrix bitMatrix = qrCodeWriter.encode(conteudoQrCode, BarcodeFormat.QR_CODE, 300, 300, hints);

			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
				}
			}

			ImageIO.write(image, "PNG", caminhoArquivo.toFile());

			System.out.println("QR Code gerado com sucesso: " + caminhoArquivo.toAbsolutePath().toString());

		} catch (Exception e) {
			System.err.println("Erro ao gerar QR Code: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
