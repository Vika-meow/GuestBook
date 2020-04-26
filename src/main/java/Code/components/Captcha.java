package Code.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

public class Captcha extends VerticalLayout {

    private File[] files;
    private Random random = new Random(System.currentTimeMillis());

    private Image image;
    private String currentCode;

    private TextField captchaCheck;

    public Captcha() {
        File dir = new File(getClass().getResource("/captchaImages").getFile());
        files = dir.listFiles();

        captchaCheck = new TextField("Captcha Check");
        captchaCheck.setRequired(true);
        captchaCheck.setErrorMessage("Invalid Captcha");

        image = generateImage();

        addComponentAtIndex(0, image);

        add(captchaCheck);
    }

    private Image generateImage(){
        File currentFile = files[nextRandom()];
        String source = currentFile.getAbsolutePath();
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(new File(source));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", bos );
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] imageBytes = bos.toByteArray();
        StreamResource resource = new StreamResource("", () -> new ByteArrayInputStream(imageBytes));
        Image image = new Image(resource, "");

        currentCode = currentFile.getName().substring(0, 5);
        return image;
    }

    public void refresh(){
        removeAll();
        image = generateImage();
        add(image);
        captchaCheck.setValue("");
        add(captchaCheck);
    }

    public boolean isValid(){
        if(captchaCheck.getValue().equals(currentCode)) {
            return true;
        }
        else {
            refresh();
            captchaCheck.setInvalid(true);
            return false;
        }
    }

    private int nextRandom(){
        return Math.abs(random.nextInt()%(files.length));
    }

}
