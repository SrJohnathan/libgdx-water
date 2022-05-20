package io.github.srjohnathan.gdx.water.desktop;

import java.io.IOException;

 public class WaterGdxLouder {

    public static boolean init() {
        {
            try {
                if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
                    NativeUtils.loadLibraryFromJar("/wt.dll");
                    return  true;
                } else {
                   // NativeUtils.loadLibraryFromJar("/libwt.so");
                   System.load("/media/pc/3AF8B8C6F8B881A5/libwater/cmake-linux/cpp/libwt.so");
                    return  true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return  false;
            }



        }
    }
}
