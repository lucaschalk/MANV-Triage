#!/bin/sh

unzip $1;

# delete "useless" things for the voice detection

rm -rf location/
rm -rf com/squareup/
rm -rf com/google/glass/bluetooth/
rm -rf com/google/glass/camera/
rm -rf com/google/glass/connectivity/
rm -rf com/google/glass/html/
rm -rf com/google/glass/location
rm -rf com/google/glass/maps
rm -rf com/google/glass/menu
rm -rf com/google/glass/music
rm -rf com/google/glass/net
rm -rf com/google/glass/phone
rm -rf com/google/glass/sound
rm -rf com/google/glass/timeline
rm -rf com/google/protobuf/
rm -rf com/google/glass/sync
rm -rf com/google/glass/protobuf/
rm -rf com/google/glass/settings/
rm -rf com/google/glass/share
rm -rf com/google/glass/proto
rm -rf com/google/glass/ongoing
rm -rf com/google/glass/mosaic
rm -rf com/google/glass/hidden
rm -rf com/google/glass/deferredcontent/
rm -rf com/google/glass/companion/
rm -rf com/google/glass/boutique/
rm -rf com/google/glass/auth/
rm -rf com/google/glass/async/
rm -rf com/google/android/search/
rm -rf com/google/android/gcm/
rm -rf com/google/android/gms
rm -rf com/google/android/s3
rm -rf com/google/android/gtalkservice/
rm -rf com/google/android/libraries
rm -rf com/google/android/gsf/
rm -rf com/google/geo
rm -rf com/google/caribou/
rm -rf com/google/speech/
rm -rf com/google/wireless
rm -rf com/google/majel/
rm -rf com/google/googlex/
rm -rf com/google/glass/widget/
rm -rf com/google/glass/wifi
rm -rf com/google/bionics
rm -rf com/google/audio
rm -rf com/google/webserver
rm -rf com/google/glass/accessibility
rm -rf com/google/glass/admin
rm -rf com/google/glass/fs
rm -rf com/google/glass/android/security/
rm -rf com/google/glass/android/net/
rm -rf com/google/glass/android/location/
rm -rf com/google/glass/android/hardware/



jar cvf $2 android com

rm -rf speech
rm -rf javax
rm -rf android
rm -rf com

echo "Optimization done !"
echo $1 "has been optimized in "$2" !"



# Count nbr of methods before 
# command : adt-bundle-mac-x86_64-20140702/sdk/build-tools/android-4.4W/dx --dex --output=temp.dex opti.jar 
# (we assure that dx is in that directory and your output file is opti.jar)
# command : cat temp.dex | head -c 92 | tail -c 4 | hexdump -e '1/4 "%d\n"'     
# 