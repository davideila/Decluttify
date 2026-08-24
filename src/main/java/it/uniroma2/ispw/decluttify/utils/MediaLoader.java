package it.uniroma2.ispw.decluttify.utils;

import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MediaLoader {

    private static MediaLoader me = null;
    private final String UI_IMAGES_PATH_FROM_RESOURCES = "/it/uniroma2/ispw/decluttify/images/";
    private final String ITEM_IMAGES_PATH_FROM_USER_DIR = File.separator + "uploads" + File.separator + "item_images" + File.separator;

    protected MediaLoader(){
    }

    public static synchronized MediaLoader getInstance(){
        if ( me == null ){
            me = new MediaLoader();
        }
        return me;
    }

    public Image loadItemImage(String fileName){
        String imagePath = System.getProperty("user.dir") + ITEM_IMAGES_PATH_FROM_USER_DIR + fileName;
        try (InputStream is = new FileInputStream(imagePath)) {
            return new Image(is);
        } catch (IOException e) {
            //If no image found, then use placeholder
            String placeholderPath = System.getProperty("user.dir") + ITEM_IMAGES_PATH_FROM_USER_DIR + "placeholder_item.png";
            try (InputStream is = new FileInputStream(placeholderPath)) {
                return new Image(is);
            } catch (IOException ex) {
                //if also placeholder image cannot be loaded, then do nothing and leave it empty
                return new Image("No image");
            }
        }
    }

    public Image loadUIImage(String fileName){
        String resourcePath = UI_IMAGES_PATH_FROM_RESOURCES + fileName;
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is != null) {
            return new Image(is);
        }
        return null;
    }
}
