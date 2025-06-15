package grupo12.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static String copiarImagem(File imagemFonte, String diretorioDestino) throws IOException {
        File pastaDestino = new File(diretorioDestino);
        if (!pastaDestino.exists()) {
            pastaDestino.mkdirs();
        }
        Path destino = Paths.get(pastaDestino.getAbsolutePath(), imagemFonte.getName());
        Files.copy(imagemFonte.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get(diretorioDestino, imagemFonte.getName()).toString();
    }
}