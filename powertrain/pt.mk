# pt powertrain config

## Change as per you application need. 
## ALl variables below are ',' delimited. If the variable value has a ',' in it, replace it with '!!'
## BIGIP_POOL should be created by the Netinfra team before deploying the app.

## TODO Check with DevOps if this env variable can be used. DT_AGENT_PATH=/apps/Dynatrace/agent/lib64/libdtagent.so=name=license_plate_recognition!!server=cj4dyl001.cars.com:9995

JAVA_OPTS=-Xms256m,-Xmx512m,-XX:+PrintFlagsFinal
ENVS=config-api.url=http://composite-st.cars.com/config-api/1.0/rest/config
LABELS=BIGIP_POOL=cars_docker_license_plate_recognition_pt,ENV=pt
INSTANCES=1
VOLUMES=/apps/docker/logs/splunkme/:/apps/docker/logs/splunkme/,/apps/Dynatrace/:/apps/Dynatrace/
LOG_DRIVER=splunk
LOG_OPTS=splunk-token=D9B72688-E8EF-4688-8FA6-846BE918251F,splunk-url=https://splunklog.cars.com:8088,splunk-insecureskipverify=true,tag="{{.ImageName}}!!{{.ID}}",labels=ENV
