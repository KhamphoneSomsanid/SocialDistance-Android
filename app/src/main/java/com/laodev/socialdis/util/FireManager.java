package com.laodev.socialdis.util;

//import android.graphics.Bitmap;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//
//import static java.util.UUID.randomUUID;


public class FireManager {

    //is this user is logged in
//    public static boolean isLoggedIn() {
//        return FirebaseAuth.getInstance().getCurrentUser() != null;
//    }
//
//    //get this user's uid
//    public static String getUid() {
//        if (FirebaseAuth.getInstance().getCurrentUser() != null)
//            return FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        return null;
//    }
//
//    public static void uploadImage(Bitmap bitmap, String folder, FireManagerInterface callback) {
//        String filename = randomUUID().toString() + ".jpg";
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference childRef = storageRef.child(filename);
//
//        String storagePath = folder + "/" + filename;
//        final StorageReference mountainImagesRef = storageRef.child(storagePath);
//
//        childRef.getName().equals(mountainImagesRef.getName());    // true
//        childRef.getPath().equals(mountainImagesRef.getPath());    // false
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = mountainImagesRef.putBytes(data);
//        uploadTask.addOnSuccessListener(taskSnapshot -> mountainImagesRef.getDownloadUrl().addOnSuccessListener(downloadPhotoUrl -> {
//            String url = downloadPhotoUrl.toString();
//            callback.onSuccess(url);
//        }))
//                .addOnFailureListener(e -> callback.onFailed(e.getMessage()));
//    }
//
//    public interface FireManagerInterface {
//        default void onSuccess(String url) {}
//        default void onFailed(String error) {}
//    }

}
