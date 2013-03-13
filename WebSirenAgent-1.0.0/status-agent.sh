echo 'Check Agent Status'
process=$(ps -ef | grep WebSirenAgent | grep -v grep)
if [ "$process" ]; then
 echo "Agent is Running ..."
else
 echo "Agent is stopped ... "
fi
