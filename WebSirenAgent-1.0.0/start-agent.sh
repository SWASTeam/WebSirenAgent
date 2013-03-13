if $JAVA_HOME == ""; then 
    echo Error: path to JAVA_HOME is missing
else
    echo $JAVA_HOME  

    $JAVA_HOME/bin/java -Dfile.encoding=UTF-8 -cp bin:lib/json-simple-1.1.1.jar:lib/org.jwall.web.audit-0.6.3.jar:lib/xstream-1.3.1.jar:lib/commons-logging-1.1.jar:lib/apache-log4j-1.2.16/log4j-1.2.16.jar:lib/apache-activemq-5.7.0/activemq-all-5.7.0.jar:lib/slf4j-1.6.4/slf4j-api-1.6.4.jar:lib/slf4j-1.6.4/slf4j-ext-1.6.4.jar:lib/slf4j-1.6.4/slf4j-log4j12-1.6.4.jar:lib/velocity-tools-2.0/lib/velocity-1.6.2.jar net.modsec.ms.main.WebSirenAgent   	
agent_pid=$!
echo $agent_pid > /root/siren-agent.pid
fi
