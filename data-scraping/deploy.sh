#!/bin/bash

# crontab config
# 0 23 21 * * /usr/bin/python3 /opt/python/brickset-data-scraping.py

ssh $1 "pip3 install PyYAML"
ssh $1 "pip3 install requests"
ssh $1 "pip3 install beautifulsoup4"
ssh $1 "pip3 install pymongo"
ssh $1 "pip3 install dnspython"

ssh $1 "sudo mkdir -p /opt/python"
ssh $1 "sudo mkdir -p /etc/lego"
ssh $1 "sudo mkdir -p /var/opt/lego"
ssh $1 "sudo mkdir -p /var/log/lego"
ssh $1 "sudo chown -R pi:pi /opt/python/"
ssh $1 "sudo chown -R pi:pi /etc/lego/"
ssh $1 "sudo chown -R pi:pi /var/opt/lego"
ssh $1 "sudo chown -R pi:pi /var/log/lego"

scp config/brickset-raspberry.yaml $1:/etc/lego/

scp brickset-data-scraping.py $1:/opt/python/