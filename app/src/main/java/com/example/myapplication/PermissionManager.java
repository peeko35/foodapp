package com.example.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionManager {
    private static PermissionManager instance;
    private Context context;

    private PermissionManager(Context context) {
        this.context = context;
    }

    public static PermissionManager getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionManager(context);
        }
        return instance;
    }

    // Method to check permissions
    public boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // Method to ask for permissions
    public void askPermissions(Fragment fragment, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(fragment.getActivity(), permissions, requestCode);
    }
    public boolean handlePermissionResult(Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(fragment.requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}

