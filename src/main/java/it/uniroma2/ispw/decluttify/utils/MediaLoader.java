package it.uniroma2.ispw.decluttify.utils;

import javafx.scene.image.Image;
import java.io.File;
import java.io.InputStream;

public class MediaLoader {

    private static MediaLoader me = null;
    private final String UI_IMAGES_PATH_FROM_RESOURCES = "/it/uniroma2/ispw/decluttify/images/";
    private final String ITEM_IMAGES_PATH_FROM_USER_DIR = File.separator + "uploads" + File.separator + "item_images" + File.separator;

    // Images to cache and reuse for all tiles instead of loading it every time
    public static final Image SHIPPING_ICON = MediaLoader.getInstance().loadUIImage("shipping_icon.png");
    public static final Image ESCROW_ICON = MediaLoader.getInstance().loadUIImage("escrow_icon.png");
    public static final Image DELETE_ICON = MediaLoader.getInstance().loadUIImage("delete_icon.png");
    public static final Image MUSIC_ICON = getInstance().loadUIImage("music_category_icon.png");
    public static final Image TECH_ICON = getInstance().loadUIImage("tech_category_icon.png");
    public static final Image BOOK_ICON = getInstance().loadUIImage("book_category_icon.png");
    public static final Image CLOTH_ICON = getInstance().loadUIImage("cloth_category_icon.png");
    public static final Image SPORT_ICON = getInstance().loadUIImage("sport_category_icon.png");
    public static final Image MISC_ICON = getInstance().loadUIImage("misc_category_icon.png");



    protected MediaLoader(){
    }

    public static synchronized MediaLoader getInstance(){
        if ( me == null ){
            me = new MediaLoader();
        }
        return me;
    }

    public Image loadItemImage(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return getPlaceholderImage();
        }

        String fullPath = System.getProperty("user.dir") + ITEM_IMAGES_PATH_FROM_USER_DIR + fileName;
        File file = new File(fullPath);
        if (file.exists()) {
            String fileUrl = file.toURI().toString();
            // 120x120px, preserveRatio=true, smooth=true, backgroundLoading=true
            return new Image(fileUrl, 600, 600, true, true, true);

        } else {
            return getPlaceholderImage();
        }
    }

    private Image getPlaceholderImage() {
        String placeholderPath = System.getProperty("user.dir") + ITEM_IMAGES_PATH_FROM_USER_DIR + "placeholder_item.png";
        File placeholderFile = new File(placeholderPath);

        if (placeholderFile.exists()) {
            return new Image(placeholderFile.toURI().toString(), 600, 600, true, true, true);
        }

        return null;
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
