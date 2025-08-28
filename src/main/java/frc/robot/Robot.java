package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;
import org.opencv.core.Core;

/**
 * Robot class modified to run locally on the notebook.
 */
public class Robot extends TimedRobot {
  
  private VideoCapture camera;

  // Carregar a biblioteca nativa (DLL) do OpenCV
  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Isso carrega o DLL necessário para o OpenCV
  }

  public Robot() {
    // Inicia a captura da câmera padrão do notebook (índice 0)
    camera = new VideoCapture(0); 
    if (!camera.isOpened()) {
      System.out.println("Erro ao abrir a câmera.");
    } else {
      System.out.println("Câmera aberta com sucesso.");
    }
  }

  @Override
  public void robotPeriodic() {
    Mat frame = new Mat();
    if (camera.read(frame)) {
      // Converte a imagem para escala de cinza
      Mat gray = new Mat();
      Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

      // Aplica um filtro de limiar (threshold) para binarizar a imagem
      Mat threshold = new Mat();
      Imgproc.threshold(gray, threshold, 100, 255, Imgproc.THRESH_BINARY);

      // Encontra os contornos na imagem binarizada
      java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
      Mat hierarchy = new Mat();
      Imgproc.findContours(threshold, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

      // Desenha os contornos encontrados na imagem original
      for (int i = 0; i < contours.size(); i++) {
        // Calcula o retângulo delimitador do contorno
        MatOfPoint contour = contours.get(i);
        org.opencv.core.Rect rect = Imgproc.boundingRect(contour);

        // Verifica se o retângulo atende aos critérios (exemplo: tamanho mínimo)
        if (rect.width > 50 && rect.height > 50) { // Ajuste os valores conforme necessário
          // Desenha o retângulo na imagem
          Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2); // Verde para o retângulo

          // Imprime uma mensagem na tela
          System.out.println("Retângulo verde detectado!");
        }
      }

      // Exibe a imagem processada com os contornos
      HighGui.imshow("Processed Image", frame);
      HighGui.waitKey(1);
    }
  }
}