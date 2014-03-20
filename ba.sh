echo Luminale | sudo -S mkdir -p /var/lock
chmod +x ./ba.sh
echo Luminale | sudo -S route -nv add -net 224.1.1.1 -interface en0
java -jar /Users/luminale/Documents/workspace/Luminale14.jar
