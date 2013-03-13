#!/bin/bash
pid=$(ps -ef | grep WebSirenAgent| awk '{print $2}')
echo "Stopping Agent ..."
if [[ -n $pid ]]; then
    stringarray=($pid)
    agent=${stringarray[0]}
    kill $agent
    echo "Agent Stopped Successfully"
else
    echo "Agent Stop Failed"
fi
