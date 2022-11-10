package com.example.serbUber.util;

import com.example.serbUber.exception.EntityUpdateException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static com.example.serbUber.util.Constants.*;

public class PictureHandler {

    private static String generateBase64String(final byte[] pictureData) {

        return Base64.getEncoder().encodeToString(pictureData);
    }

    public static String generatePhotoPath(final String name) {

        return PHOTOS_FILE_PATH + name;
    }

    public static String generateSavePhotoPath(final String name) {

        return TARGET_PHOTO_FILE_PATH + name;
    }

    public static String generatePhotoName(final Long id) {

        return String.format("user-%s.png", id.toString());
    }

    public static byte[] getPictureDataByName(final String name) throws IOException
    {
        return Files.readAllBytes(Paths.get(generatePhotoPath(name)));
    }

    public static String convertPictureToBase64ByName(final String name) {
        try {
            byte[] pictureData = new byte[0];
            pictureData = getPictureDataByName(name);

            return generateBase64String(pictureData);
        } catch (IOException e) {
            return "";
        }
    }

    private static void savePictureFromBase64(final String pictureName, final String base64) throws EntityUpdateException {
        try{
            byte[] image = Base64.getDecoder().decode(base64);
            OutputStream out = new FileOutputStream(generateSavePhotoPath(pictureName));
            out.write(image);
            out.flush();
            out.close();

        } catch (Exception e) {
            throw new EntityUpdateException("Profile picture update failed, try again later.");
        }
    }

    public static String checkPictureValidity(final String base64Opt, final Long id)
        throws EntityUpdateException
    {

        return getProfilePicture(base64Opt).equalsIgnoreCase(DEFAULT_PICTURE) ? DEFAULT_PICTURE :
                savePicture(base64Opt, id);
    }

    public static String savePicture(final String base64Opt, final Long id) throws EntityUpdateException {
        String pictureName = generatePhotoName(id);
        savePictureFromBase64(pictureName, base64Opt);

        return pictureName;
    }
}
