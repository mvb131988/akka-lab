SET APPNAME=host

CALL mvn assembly:assembly -DskipTests

echo "Copying host.jar"
echo "Current path is:"
echo %cd%
xcopy .\target\host.jar . /Y

SET APPNAME=